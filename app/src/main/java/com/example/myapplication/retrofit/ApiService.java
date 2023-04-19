package com.example.myapplication.retrofit;

import com.example.myapplication.domain.LoginRequest;
import com.example.myapplication.domain.RegisterRequest;
import com.example.myapplication.domain.Restaurant;
import com.example.myapplication.domain.ValidatefDuplicateUsername;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiService {
    @POST("/api/login")
    Call<String> login(@Body LoginRequest loginRequest);
    @POST("/api/register")
    Call<String> register(@Body RegisterRequest registerRequest);
    @POST("api/validateDuplicate")
    Call<String> validateDuplicate(@Body ValidatefDuplicateUsername validatefDuplicateUsername);
    @Multipart
    @POST("api/RestaurantRegister")
    Call<String> restaurantRegister(@Part("restaurant") Restaurant restaurant, @Part MultipartBody.Part restaurantImg, @Part List<MultipartBody.Part> menuImgs);
    @Multipart
    @POST("api/test")
    Call<String> test(@Part MultipartBody.Part restaurantImg);
}