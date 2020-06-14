package com.example.maskday.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maskday.Activity.DetailActivity;
import com.example.maskday.Adapter.BoardAdapter;
import com.example.maskday.R;
import com.example.maskday.Model.UserModel;
import com.example.maskday.RecyclerClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QnAFragment extends Fragment {

    private RecyclerView qnaBoardRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<UserModel> userModelList;
    private BoardAdapter boardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Map<String, Object> contentMap;

    RecyclerView.LayoutManager layoutManager;

    public QnAFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qn_a, container, false);

        init(view);
        readQnABoard();
        selectBoard(view.getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readQnABoard();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    private void init(View view) {
        qnaBoardRecyclerView = (RecyclerView) view.findViewById(R.id.qna_recycler_view);
        qnaBoardRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.qna_swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        layoutManager = new LinearLayoutManager(getActivity());
        qnaBoardRecyclerView.setLayoutManager(layoutManager);
    }

    private void readQnABoard() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        CollectionReference collectionReference = firebaseFirestore.collection("Content");
        collectionReference.whereEqualTo("board", "증상질문게시판").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documentSnapshots = task.getResult();
                userModelList = new ArrayList<>();
                contentMap = new HashMap<>();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    UserModel userModel = new UserModel();
                    contentMap = document.getData();

                    userModel.id = document.getId();
                    userModel.title = (String) contentMap.get("title");
                    userModel.content = (String) contentMap.get("content");
                    userModel.userEmail = (String) contentMap.get("user email");
                    userModel.timestamp = (String) contentMap.get("timestamp");

                    userModelList.add(userModel);
                }

                boardAdapter = new BoardAdapter(userModelList);
                qnaBoardRecyclerView.setAdapter(boardAdapter);
            } else {
                Log.d("QnAFragment", String.valueOf(task.getException()));
            }
        });
    }

    private void selectBoard(Context ctx) {
        qnaBoardRecyclerView.addOnItemTouchListener(new RecyclerClickListener.RecyclerTouchListener(getContext(), qnaBoardRecyclerView, new RecyclerClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent detailIntent = new Intent(ctx, DetailActivity.class);
                detailIntent.putExtra("id", userModelList.get(position).id);
                detailIntent.putExtra("title", userModelList.get(position).title);
                detailIntent.putExtra("content", userModelList.get(position).content);
                detailIntent.putExtra("timestamp", userModelList.get(position).timestamp);
                detailIntent.putExtra("email", userModelList.get(position).userEmail);
                ctx.startActivity(detailIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
