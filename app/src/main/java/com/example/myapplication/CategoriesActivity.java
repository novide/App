package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        SearchView searchView = findViewById(R.id.search); //SearchView 객체 생성
        String query = getIntent().getStringExtra("search_query"); //Intent로 전달된 값을 가져옴
        searchView.setQuery(query, false); //SearchView에 입력된 값을 적용함
    }
}