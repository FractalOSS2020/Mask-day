package com.example.maskday.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maskday.R;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class AppInfoActivity extends AppCompatActivity {

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        LinearLayout layout = findViewById(R.id.app_info_layout);
        layout.setBackgroundColor(getResources().getColor(R.color.white));

        startActivity(new Intent(this, OssLicensesMenuActivity.class));

        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
