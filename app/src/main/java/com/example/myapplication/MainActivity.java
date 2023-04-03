package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

//바꾸었어요 선생님
public class MainActivity extends AppCompatActivity {
    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info_bt = findViewById(R.id.info_bt);
        reservation_bt = findViewById(R.id.reservation_bt);
        home_bt = findViewById(R.id.home_bt);

        info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(infoIntent);
            }
        });

        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        reservation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(reservationIntent);
            }
        });

    }


}