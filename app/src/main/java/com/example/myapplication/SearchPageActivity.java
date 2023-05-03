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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.domain.ListViewData;
import com.example.myapplication.retrofit.NetworkHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPageActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;

    ArrayList<ListViewData> restaurantDataList;
    MyAdapter myAdapter;

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
        searchView.setQuery(query, false); //SearchView에 입력된 값을 적용함.

        // 식당 목록 나열해주는 변수 및 함수
        ListView listView = findViewById(R.id.restaurantListView);
        restaurantDataList = new ArrayList<ListViewData>();
        myAdapter = new MyAdapter(this, restaurantDataList);
        listView.setAdapter(myAdapter);
        searchByRestaurantNameList(query);

        info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent;
                if (isLogin()) { // 로그인한 상태라면
                    infoIntent = new Intent(SearchPageActivity.this, LoginSuccess.class);
                } else { // 로그인하지 않은 상태라면
                    infoIntent = new Intent(SearchPageActivity.this, LoginActivity.class);
                }
                startActivity(infoIntent);
            }
        });

        // 홈 버튼 처리
        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(SearchPageActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        // 찜 버튼 처리
        reservation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(SearchPageActivity.this, SearchPageActivity.class);
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


    // 검색된 식당 데이터 가져오기
    private void searchByRestaurantNameList(String restaurantName){
        Call<Map<String, Object>> call = NetworkHelper.getInstance().getApiService().getRestaurantSearchData(restaurantName);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    // 데이터를 정상적으로 받아온 경우 처리할 로직을 작성합니다.
                    Map<String, Object> data = response.body();
                    List<String> restaurantNameList = (List<String>) data.get("restaurantNameList");
                    List<String> imageList = (List<String>) data.get("imageList");
                    List<Bitmap> imgBitmapList = new ArrayList<>();
                    for (String image : imageList) {
                        byte[] imageBytes = android.util.Base64.decode(image, android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imgBitmapList.add(bitmap);
                    }
                    for (int i = 0; i < imgBitmapList.size(); i++) {
                        restaurantDataList.add(new ListViewData(i+1, imgBitmapList.get(i), restaurantNameList.get(i)));
                    }
                    myAdapter.notifyDataSetChanged(); // 리스트뷰 갱신
                } else {
                    // 데이터를 받아오는 도중 오류가 발생한 경우 처리할 로직을 작성합니다.
                    Log.e(TAG, "Response failed"); //username 중복
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.e(TAG, "Network failure");
                    t.printStackTrace();
                } else {
                    Log.e(TAG, "Unexpected failure");
                }
            }
        });
    }
}