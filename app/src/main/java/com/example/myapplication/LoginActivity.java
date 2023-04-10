package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.domain.LoginRequest;
import com.example.myapplication.retrofit.ApiService;
import com.example.myapplication.retrofit.NetworkHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    private Button login_register_bt;
    private Button login_bt;

    private Button info_bt;
    private Button reservation_bt;
    private Button home_bt;

    //  로그인을 하기위한 변수
    private static final String TAG = "MainActivity";
    private EditText username_edit;                // id 에디트
    private EditText password_edit;                // pw 에디트


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 뒤로가기
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //화면의 버튼들을 찾는 코드
        login_register_bt = findViewById(R.id.login_register_bt);
        login_bt = findViewById(R.id.login_bt);
        info_bt = findViewById(R.id.info_bt2);
        reservation_bt = findViewById(R.id.reservation_bt2);
        home_bt = findViewById(R.id.home_bt3);
        //화면의 입력을 찾는 코드
        username_edit = (EditText)findViewById(R.id.login_id);    // id 에디트를 찾음.
        password_edit = (EditText)findViewById(R.id.login_pass);    // pw 에디트를 찾음.

        //회원가입 버튼 처리
        login_register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //로그인 버튼 처리
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        // 정보 버튼 처리
        info_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(infoIntent);
            }
        });

        // 홈 버튼 처리
        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        // 찜 버튼 처리
        reservation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(reservationIntent);
            }
        });
    }
    // 실시간 로그인 저장    !! 적용안됨
    private void saveLogin() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLogin", true);
        editor.apply();
    }

    // 로그인시
    //retrofit 통신
    private void login() {
        String username = username_edit.getText().toString();
        String password = password_edit.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(LoginActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<String> call = NetworkHelper.getInstance().getApiService().login(loginRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody != null && responseBody.equals("success")) {
                        // "success" 문자열이 도착한 경우 처리할 코드 작성
                        // 서버로부터 정상적인 응답을 받은 경우 처리하는 코드
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        // 서버로부터 다른 응답이 도착한 경우 처리할 코드 작성
                        Log.e(TAG, "Invalid response");
                    }
                } else {
                    // 서버로부터 오류 응답이 도착한 경우 처리할 코드 작성
                    Log.e(TAG, "Response failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.e(TAG, "Network failure");
                } else {
                    Log.e(TAG, "Unexpected failure");
                }
            }
        });
    }
}
