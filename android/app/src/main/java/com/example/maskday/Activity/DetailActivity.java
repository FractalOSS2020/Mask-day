package com.example.maskday.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maskday.Adapter.CommentAdapter;
import com.example.maskday.Model.CommentModel;
import com.example.maskday.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView, contentTextView, userTextView;
    private EditText inputComment;
    private ImageView backBtn, saveCommentBtn;
    private View view;
    private FirebaseFirestore firebaseFirestore;
    private Map<String, Object> commentMap;
    private List<CommentModel> commentModelList;
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;

    private String title, content, user, docId;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();

        Intent intent = getIntent();

        docId = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        user = intent.getStringExtra("email");

        titleTextView.setText(title);
        contentTextView.setText(content);
        userTextView.setText(user);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComment();
            }
        });

        readComment();

    }

    private void init() {
        titleTextView = (TextView) findViewById(R.id.detail_title);
        contentTextView = (TextView) findViewById(R.id.detail_content);
        userTextView = (TextView) findViewById(R.id.detail_user);
        backBtn = (ImageView) findViewById(R.id.detail_back_btn);
        view = (View) findViewById(R.id.detail_divider);
        view.setVisibility(View.GONE);

        inputComment = (EditText) findViewById(R.id.input_comment);
        saveCommentBtn = (ImageView) findViewById(R.id.save_comment);
        commentRecyclerView = (RecyclerView) findViewById(R.id.comment_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void saveComment() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        commentMap = new HashMap<>();

        comment = inputComment.getText().toString();
        CollectionReference collectionReference = firebaseFirestore.collection("Content").document(docId).collection("Comment");
        commentMap.put("user email", user.getEmail());
        commentMap.put("comment", comment);

        collectionReference.add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    inputComment.setText("");
                    Toast.makeText(DetailActivity.this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    readComment();
                } else {
                    Log.d("DetailActivity", "comment save ERROR");
                    Toast.makeText(DetailActivity.this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void readComment() {
        CollectionReference collectionReference = firebaseFirestore.collection("Content").document(docId).collection("Comment");
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documentSnapshots = task.getResult();
                commentModelList = new ArrayList<>();
                commentMap = new HashMap<>();
                for (QueryDocumentSnapshot document : documentSnapshots) {
                    CommentModel commentModel = new CommentModel();
                    commentMap = document.getData();

                    commentModel.commentContent = (String) commentMap.get("comment");
                    commentModel.commentUser = (String) commentMap.get("user email");

                    commentModelList.add(commentModel);

                }
                commentAdapter = new CommentAdapter(commentModelList);
                commentRecyclerView.setAdapter(commentAdapter);

            } else {
                Log.d("DetailActivity", "get failed with," + task.getException());
            }
        });
    }

}
