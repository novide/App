package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.domain.RegisterRequest;
import com.example.myapplication.domain.ValidatefDuplicateUsername;
import com.example.myapplication.retrofit.NetworkHelper;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button register_bt;
    private Button validatefDuplicate_bt;

    // 회원가입 변수
    private static final String TAG = "MainActivity";
    private EditText username_edit;                // id 에디트
    private EditText password_edit;                // pw 에디트
    private EditText name_edit;                // name 에디트
    private EditText password_check_edit;     // pw 체크 에디트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //화면의 입력을 찾는 코드
        username_edit = (EditText)findViewById(R.id.register_ID);    // id 에디트를 찾음.
        password_edit = (EditText)findViewById(R.id.register_pass);    // pw 에디트를 찾음.
        password_check_edit = (EditText)findViewById(R.id.register_checkPass);
        name_edit = (EditText)findViewById(R.id.register_name);

        register_bt = findViewById(R.id.register_bt);

        register_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        validatefDuplicate_bt = findViewById(R.id.validatefDuplicate_bt);
        validatefDuplicate_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validatefDuplicate();
            }

        });
    }

    // 회원가입시시
    //retrofit 통신
    private void register() {
        String username = username_edit.getText().toString();
        String password = password_edit.getText().toString();
        String passwordCheck = password_check_edit.getText().toString();
        String name = name_edit.getText().toString();

        // 모든 입력 했는지 확인
        if (username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordCheck.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "비밀번호 확인을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "이름를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 비밀번호 확인
        if (password.equals(passwordCheck) == false){
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ID,Pass 최소 최대입력 제한 ID 5~20자리  Pass 8~16자리
        if(username.length() < 5){
            Toast.makeText(RegisterActivity.this, "5~20자리의 아이디만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        } else if (username.length() > 20) {
            Toast.makeText(RegisterActivity.this, "5~20자리의 아이디만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 8){
            Toast.makeText(RegisterActivity.this, "8~16자리의 비밀번호만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() > 16) {
            Toast.makeText(RegisterActivity.this, "8~16자리의 비밀번호만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest(username, password, name);
        //  retrofit통신
        Call<String> call = NetworkHelper.getInstance().getApiService().register(registerRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody != null && responseBody.equals("success")) {
                        // "success" 문자열이 도착한 경우 처리할 코드 작성
                        // 서버로부터 정상적인 응답을 받은 경우 처리하는 코드
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        // 서버로부터 다른 응답이 도착한 경우 처리할 코드 작성
                        Log.e(TAG, "Invalid response");
                    }
                } else {
                    // 서버로부터 오류 응답이 도착한 경우 처리할 코드 작성
                    Log.e(TAG, "Response failed"); //username 중복
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
    // 중복 확인
    private void validatefDuplicate() {
        String username = username_edit.getText().toString();

        // 모든 입력 했는지 확인
        if (username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // ID 최소 최대입력 제한  5~20자리
        if(username.length() < 5){
            Toast.makeText(RegisterActivity.this, "5~20자리의 아이디만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        } else if (username.length() > 20) {
            Toast.makeText(RegisterActivity.this, "5~20자리의 아이디만 사용가능", Toast.LENGTH_SHORT).show();
            return;
        }
        ValidatefDuplicateUsername validatefDuplicateUsername = new ValidatefDuplicateUsername(username);
        // retrofit통신
        Call<String> call = NetworkHelper.getInstance().getApiService().validateDuplicate(validatefDuplicateUsername);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody != null && responseBody.equals("DuplicateCheck")) {
                        // "success" 문자열이 도착한 경우 처리할 코드 작성
                        // 서버로부터 정상적인 응답을 받은 경우 처리하는 코드
                        Toast.makeText(RegisterActivity.this, "사용 가능한 아이디", Toast.LENGTH_SHORT).show();
                    } else {
                        // 서버로부터 다른 응답이 도착한 경우 처리할 코드 작성
                        Log.e(TAG, "Invalid response");
                    }
                } else {
                    // 서버로부터 오류 응답이 도착한 경우 처리할 코드 작성
                    Toast.makeText(RegisterActivity.this, "이미 사용중인 아이디", Toast.LENGTH_SHORT).show();
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