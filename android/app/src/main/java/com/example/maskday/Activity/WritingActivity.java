package com.example.maskday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.maskday.R;
import com.example.maskday.Model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class WritingActivity extends AppCompatActivity {

    private ImageView backBtn, saveBtn;
    private EditText editTitle, editContent;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RadioGroup boardSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        init();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = editTitle.getText().toString();
                final String content = editContent.getText().toString();
                final int radioId = boardSelect.getCheckedRadioButtonId();
                saveContent(title, content, radioId);
            }
        });
    }

    private void init(){
        backBtn = (ImageView) findViewById(R.id.writing_back_button);
        saveBtn = (ImageView) findViewById(R.id.writing_save_button);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);
        boardSelect = (RadioGroup) findViewById(R.id.board_select);
    }

    private void saveContent(String title, String content, int radioId){
        UserModel userModel = new UserModel();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        RadioButton boardName = (RadioButton) findViewById(radioId);

        userModel.userEmail = setUserName(user.getEmail());
        userModel.title = title;
        userModel.content = content;
        userModel.board = boardName.getText().toString();

        if (userModel.title.isEmpty() || userModel.content.isEmpty()) {
            Toast.makeText(WritingActivity.this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("업로드중...");
        progressDialog.show();

        Map<String, Object> contentMap = new HashMap<>();

        contentMap.put("user email", userModel.userEmail);
        contentMap.put("title", userModel.title);
        contentMap.put("content", userModel.content);
        contentMap.put("board", userModel.board);

        CollectionReference reference = firebaseFirestore.collection("User");
        reference.whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                for (QueryDocumentSnapshot queryDocumentSnapshot : snapshots) {
                    firebaseFirestore.collection("Content").add(contentMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "성공적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String error = e.getMessage();
                                    Log.d("WritingActivity", "Error :" + error);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Log.d("WritingActivity", "get failed with ", task.getException());
            }
        });


    }

    private String setUserName(String userName) {

        String[] email = userName.split("@");
        String editEmail = email[0];

        return editEmail.substring(0,3) + "**";

    }
}
