package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity {
    private Button login_register_bt;
    private Button login_bt;

    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 뒤로가기
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        login_register_bt = findViewById(R.id.login_register_bt);
        login_bt = findViewById(R.id.login_bt);
        info_bt = findViewById(R.id.info_bt2);
        reservation_bt = findViewById(R.id.reservation_bt2);
        home_bt = findViewById(R.id.home_bt2);
        login_register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });

        info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(infoIntent);
            }
        });

        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        reservation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(reservationIntent);
            }
        });
    }

}
