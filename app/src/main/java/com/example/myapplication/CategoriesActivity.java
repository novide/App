package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

public class CategoriesActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar4 = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar4);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        info_bt = findViewById(R.id.info_bt3);
        reservation_bt = findViewById(R.id.reservation_bt3);
        home_bt = findViewById(R.id.home_bt3);

//        SearchView searchView = findViewById(R.id.search); //SearchView 객체 생성
//        String query = getIntent().getStringExtra("search_query"); //Intent로 전달된 값을 가져옴
//        searchView.setQuery(query, false); //SearchView에 입력된 값을 적용함.


        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent;
                if (isLogin()) { // 로그인한 상태라면
                    infoIntent = new Intent(CategoriesActivity.this, LoginSuccess.class);
                } else { // 로그인하지 않은 상태라면
                    infoIntent = new Intent(CategoriesActivity.this, LoginActivity.class);
                }
                startActivity(infoIntent);
            }
        });

            // 홈 버튼 처리
            home_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent homeIntent = new Intent(CategoriesActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                }
            });

            // 찜 버튼 처리
            reservation_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent reservationIntent = new Intent(CategoriesActivity.this, CategoriesActivity.class);
                    startActivity(reservationIntent);
                }
            });
    }
    // 뒤로가기 버튼 누르면 뒤로
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed(); // 현재 액티비티 종료
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isLogin() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        return preferences.getBoolean("isLogin", false);
    }
}