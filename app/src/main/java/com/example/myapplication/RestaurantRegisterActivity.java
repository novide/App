package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantRegisterActivity extends AppCompatActivity {
    private EditText menuEditText;
    private EditText priceEditText;
    private ListView menuListView;
    private ListView priceListView;
    private Button registerBtn;

    private ArrayList<String> menuList;
    private ArrayList<String> priceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);

        Toolbar toolbar6 = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar6);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuEditText = findViewById(R.id.restaurant_menu);
        priceEditText = findViewById(R.id.restaurant_price);
        menuListView = findViewById(R.id.menu_list_view);
        priceListView = findViewById(R.id.price_list_view);
        registerBtn = findViewById(R.id.restaurant_register_btn);

        menuList = new ArrayList<>();
        priceList = new ArrayList<>();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menu = menuEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();

                // 메뉴와 가격을 리스트에 추가
                menuList.add(menu);
                priceList.add(price);

                // 리스트뷰 어댑터 생성
                ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(RestaurantRegisterActivity.this, android.R.layout.simple_list_item_1, menuList);
                ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(RestaurantRegisterActivity.this, android.R.layout.simple_list_item_1, priceList);

                // 리스트뷰에 어댑터 설정
                menuListView.setAdapter(menuAdapter);
                priceListView.setAdapter(priceAdapter);

                // 입력 필드 초기화
                menuEditText.setText("");
                priceEditText.setText("");
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(RestaurantRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            }
        return super.onOptionsItemSelected(item);
    }
}