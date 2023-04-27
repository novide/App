package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class CategoriesActivity extends AppCompatActivity {
    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;

    ScrollView scrollView = findViewById(R.id.scrollView3);

    LinearLayout linearLayout = findViewById(R.id.linearLayout2);


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

        SearchView searchView = findViewById(R.id.search); //SearchView 객체 생성
        String query = getIntent().getStringExtra("search_query"); //Intent로 전달된 값을 가져옴
        searchView.setQuery(query, false); //SearchView에 입력된 값을 적용함

        List<File> imgFileList = new ArrayList<>();

        List<String> restaurantNameList= new ArrayList<>();

        showImgAndRestaurantName(imgFileList, restaurantNameList);

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
//                Intent infoIntent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(infoIntent);
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
                Intent reservationIntent = new Intent(CategoriesActivity.this, MainActivity.class);
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

    private void showImgAndRestaurantName(List<File> imgFileList, List<String> restaurantNameList){
        for (int i = 0; i < imgFileList.size(); i++) {
            // 이미지를 가져와서 ImageView에 설정합니다.
            File imgFile = imgFileList.get(i);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 16);
            imageView.setLayoutParams(layoutParams);

            // 식당 이름을 가져와서 TextView에 설정합니다.
            String restaurantName = restaurantNameList.get(i);
            TextView textView = new TextView(this);
            textView.setText(restaurantName);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setLayoutParams(layoutParams);

            // LinearLayout에 ImageView와 TextView를 추가합니다.
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
        }
    }
}