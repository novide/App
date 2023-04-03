package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

//바꾸었어요 선생님
public class MainActivity extends AppCompatActivity {
    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info_bt = findViewById(R.id.info_bt);
        reservation_bt = findViewById(R.id.reservation_bt);
        home_bt = findViewById(R.id.home_bt);

        searchView = findViewById(R.id.search);
        String query = searchView.getQuery().toString(); //입력된 값을 가져옴

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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