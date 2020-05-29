package com.example.maskday.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.maskday.BoardAdapter;
import com.example.maskday.R;
import com.example.maskday.UserModel;
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


public class FreeBoardFragment extends Fragment {

    private RecyclerView freeBoardRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<UserModel> userModelList;
    private BoardAdapter boardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Map<String, Object> contentMap;

    RecyclerView.LayoutManager layoutManager;

    public FreeBoardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_board, container, false);

        init(view);
        readFreeBoard();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readFreeBoard();
            }
        });

        return view;
    }

    private void init(View view) {
        freeBoardRecyclerView = (RecyclerView) view.findViewById(R.id.free_recycler_view);
        freeBoardRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.free_swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        layoutManager = new LinearLayoutManager(getActivity());
        freeBoardRecyclerView.setLayoutManager(layoutManager);
    }

    private void readFreeBoard() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        CollectionReference collectionReference = firebaseFirestore.collection("Free Board");
        collectionReference.get().addOnCompleteListener(task -> {
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

                    userModelList.add(userModel);
                }

                boardAdapter = new BoardAdapter(userModelList);
                freeBoardRecyclerView.setAdapter(boardAdapter);
            } else {
                Log.d("FreeBoardFragment", String.valueOf(task.getException()));
            }
        });
    }
}
