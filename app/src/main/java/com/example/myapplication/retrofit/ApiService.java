package com.example.myapplication.retrofit;

import com.example.myapplication.domain.LoginRequest;
import com.example.myapplication.domain.RegisterRequest;
import com.example.myapplication.domain.ValidatefDuplicateUsername;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/login")
    Call<String> login(@Body LoginRequest loginRequest);

    @POST("/api/register")
    Call<String> register(@Body RegisterRequest registerRequest);

    @POST("/api/validateDuplicate")
    Call<String> validateDuplicate(@Body ValidatefDuplicateUsername validatefDuplicateUsername);
}