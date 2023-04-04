package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginSuccess extends AppCompatActivity {

    private Button logoutBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        logoutBtn = findViewById(R.id.logout_bt2);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        editor = sharedPreferences.edit();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences에서 로그인 정보 삭제
                editor.clear();
                editor.apply();

                // LoginActivity.java로 이동
                Intent intent = new Intent(LoginSuccess.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}