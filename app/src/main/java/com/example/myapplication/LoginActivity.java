package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


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
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
//                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(loginIntent);
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

    // 로그인시
    // AsyncTask 클래스를 상속하여 서버와 통신하는 코드를 작성
    private class LoginTask extends AsyncTask<Void, Void, String> {

        // 서버 주소
        private static final String SERVER_ADDRESS = "http://172.30.1.32:8080";

        // 서버 API 경로
        private static final String API_PATH = "/api/login";

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            try {
                // 서버 API에 POST 요청을 보내기 위한 설정
                URL url = new URL(SERVER_ADDRESS + API_PATH);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // 요청 바디에 전송할 데이터
                String requestBody = "{\"username\": \"" + username_edit.getText().toString() +
                        "\", \"password\": \"" + password_edit.getText().toString() + "\"}";

                // 단순 확인용 프린트
                System.out.println(username_edit.getText().toString());
                System.out.println(password_edit.getText().toString());
                // 요청 바디 전송
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(requestBody);
                osw.flush();
                osw.close();

                // 서버 응답 받기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                return sb.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // 서버에서 받은 결과를 처리하는 코드
                // 예시: 서버에서 받은 결과가 "success"일 경우에만 로그인 성공 처리
                if (result.equals("success")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                    saveLogin(); // 로그인 정보 저장
                }
            }
        }
        private void saveLogin() {
            SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogin", true);
            editor.apply();
        }
    }
}
