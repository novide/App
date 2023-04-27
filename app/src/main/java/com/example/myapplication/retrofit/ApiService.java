package com.example.myapplication.retrofit;

import com.example.myapplication.domain.LoginRequest;
import com.example.myapplication.domain.RegisterRequest;
import com.example.myapplication.domain.RestaurantRequest;
import com.example.myapplication.domain.ValidatefDuplicateUsername;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<String> restaurantRegister(@Part("restaurant") RequestBody restaurantRequest, @Part MultipartBody.Part restaurantImg, @Part List<MultipartBody.Part> menuImgs);
    @Multipart
    @POST("api/test")
    Call<String> test(@Part MultipartBody.Part restaurantImg);
    @GET("/getRestaurantData")
    Call<Map<String, Object>> getRestaurantData();

}