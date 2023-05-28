package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.domain.GetRestaurant;
import com.example.myapplication.domain.ListViewData;
import com.example.myapplication.domain.MenuListviewData;
import com.example.myapplication.domain.MenuRequest;
import com.example.myapplication.retrofit.NetworkHelper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantInfo extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ArrayList<MenuListviewData> MenuList;
    ImageView restaurant_iv;
    TextView restaurantName_tv;
    TextView runTime_tv;
    TextView address_tv;
    MenuAdapter menuAdapter;
    private List<MenuRequest> menuRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        restaurantName_tv = findViewById(R.id.restaurantName);
        runTime_tv = findViewById(R.id.runTime);
        address_tv = findViewById(R.id.address);

        restaurant_iv = findViewById(R.id.imageView);

        Toolbar toolbar5 = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar5);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String restaurantName_query = getIntent().getStringExtra("restaurantName_query"); // 식당이름가져오기
        restaurantName_tv.setText(restaurantName_query);
        // 식당 목록 나열해주는 변수 및 함수
        ListView listView = findViewById(R.id.menuListView);
        MenuList = new ArrayList<MenuListviewData>();
        menuAdapter = new MenuAdapter(this, MenuList);
        listView.setAdapter(menuAdapter);
        searchByRestaurantNameList(restaurantName_query);


    }

    // 식당 정보 가져오기
    private void searchByRestaurantNameList(String restaurantName){
        Call<GetRestaurant> call = NetworkHelper.getInstance().getApiService().getRestaurantData(restaurantName);
        call.enqueue(new Callback<GetRestaurant>() {
            @Override
            public void onResponse(Call<GetRestaurant> call, Response<GetRestaurant> response) {
                if (response.isSuccessful()) {
                    // 데이터를 정상적으로 받아온 경우 처리할 로직을 작성합니다.

                    // 식당이미지
                    GetRestaurant data = response.body();
                    byte[] imageBytes = android.util.Base64.decode(data.getRestaurantImg(), android.util.Base64.DEFAULT);
                    Bitmap restaurantbitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    restaurant_iv.setImageBitmap(restaurantbitmap);
                    restaurant_iv.setPadding(10, 10, 0, 10);

                    //운영시간, 주소 텍스트뷰에 입력
                    runTime_tv.setText(data.getRestaurantOperatingTime());
                    address_tv.setText(data.getRestaurantLocation());

                    //메뉴정보 레이아웃에 적용
                    menuRequest = data.getMenuList();
                    for (MenuRequest menu : menuRequest){
                        byte[] imageByte = android.util.Base64.decode(menu.getMenuImg(), android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                        int i=0;
                        MenuList.add(new MenuListviewData(i+1,bitmap , menu.getMenuName(), menu.getMenuPrice()));

                        i++;
                    }
                    menuAdapter.notifyDataSetChanged();
                } else {
                    // 데이터를 받아오는 도중 오류가 발생한 경우 처리할 로직을 작성합니다.
                    Log.e(TAG, "Response failed"); //username 중복
                }
            }

            @Override
            public void onFailure(Call<GetRestaurant> call, Throwable t) {
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