package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.domain.Menu;
import com.example.myapplication.domain.RegisterRequest;
import com.example.myapplication.domain.Restaurant;
import com.example.myapplication.retrofit.NetworkHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegisterActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //서버로 보낼 데이터
    private Uri restaurantImgUri;
    private Uri menuImgUri;
    private List<Uri> menuImgUriList;
    private List<Menu> menuList;
    //-----------------------

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton restaurant_img_btn;
    private TextView restaurant_img;

    private static final int PICK_IMAGE_REQUEST1 = 2;

    private ImageButton restaurant_menu_btn;

    private TextView restaurant_menu_img;
    private EditText restaurant_name;
    private EditText restaurant_loc;
    private EditText restaurant_runtime;
    private EditText menuEditText;
    private EditText priceEditText;
    private ListView menuListView;
    private ListView priceListView;
    private ImageButton restaurant_addmenu_btn;
    private Button restaurant_register_btn;

    private ArrayList<String> menuNameList;
    private ArrayList<String> priceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);

        Toolbar toolbar6 = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar6);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        restaurant_name = findViewById(R.id.restaurant_name);
        restaurant_loc = findViewById(R.id.restaurant_loc);
        restaurant_runtime = findViewById(R.id.restaurant_runtime);

        menuEditText = findViewById(R.id.restaurant_menu);
        priceEditText = findViewById(R.id.restaurant_price);
        menuListView = findViewById(R.id.menu_list_view);
        priceListView = findViewById(R.id.price_list_view);
        restaurant_addmenu_btn = findViewById(R.id.restaurant_addmenu_btn);

        menuNameList = new ArrayList<>();
        priceList = new ArrayList<>();

        restaurant_img_btn = findViewById(R.id.restaurant_img_btn);
        restaurant_img = findViewById(R.id.restaurant_img);

        restaurant_menu_btn = findViewById(R.id.restaurant_menu_btn);
        restaurant_menu_img = findViewById(R.id.restaurant_menu_img);
        restaurant_register_btn = findViewById(R.id.restaurant_register_btn);

        // 식당 이미지 선택 창 띄우기
        restaurant_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        // 메뉴 이미지 선택 창 띄우기
        restaurant_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
            }
        });

        // 식당 메뉴추가
        restaurant_addmenu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuName = menuEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();

                // 메뉴와 가격을 리스트에 추가
                menuNameList.add(menuName);
                priceList.add(price);

                // 이미지 경로를 리스트에 추가
                if (menuImgUriList == null) {
                    menuImgUriList = new ArrayList<>();
                    menuImgUriList.add(menuImgUri);
                } else {
                    menuImgUriList.add(menuImgUri);
                }

                // 리스트뷰 어댑터 생성
                ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(RestaurantRegisterActivity.this, android.R.layout.simple_list_item_1, menuNameList);
                ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(RestaurantRegisterActivity.this, android.R.layout.simple_list_item_1, priceList);

                // 리스트뷰에 어댑터 설정
                menuListView.setAdapter(menuAdapter);
                priceListView.setAdapter(priceAdapter);

                // 입력 필드 초기화
                menuEditText.setText("");
                priceEditText.setText("");

                // 이미지 경로가 저장된 변수 초기화
                menuImgUri = null;
                // 서버에 보낼 메뉴객체에 추가
                Menu menu = new Menu();
                menu.setMenuName(menuName);
                menu.setMenuPrice(price);
                if (menuList == null) {
                    menuList = new ArrayList<>();
                    menuList.add(menu);
                } else {
                    menuList.add(menu);
                }
            }
        });
        restaurant_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                restaurantRegister();
                test();
            }
        });
//        //저장소 접근권한
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//        }
    }

    // 갤러리에서 이미지 선택 후 돌아왔을 때 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 식당 이미지
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            // 선택한 이미지의 경로(Uri) 객체 얻기
            Uri imageUri = data.getData();

            // 선택한 이미지의 이름 얻기
            String imageName = getImageNameFromUri(imageUri);

            // 이미지 이름을 화면에 보여주기
            restaurant_img.setText(imageName);

            // 식당 이미지 경로를 보낼 변수에 저장
            restaurantImgUri = imageUri;

        }
        // 메뉴 이미지
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST1)
        {
            // 선택한 이미지의 경로(Uri) 객체 얻기
            Uri imageUri = data.getData();

            // 선택한 이미지의 이름 얻기
            String imageName = getImageNameFromUri(imageUri);

            // 이미지 이름을 화면에 보여주기
            restaurant_menu_img.setText(imageName);

            //메뉴 이미지 경로를 변수에 저장
            menuImgUri = imageUri;
        }
    }

    // Uri로부터 이미지 파일 이름 얻기
    private String getImageNameFromUri(Uri uri) {
        String result;
        // Uri로부터 이미지 파일 이름을 얻기 위한 메서드
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) { // query failed
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            //MediaStore.Images.ImageColumns.DISPLAY_NAME를 이용해 이미지 파일 이름 얻어서 반환
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(RestaurantRegisterActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    // 식당등록시
    //retrofit 통신
    private void restaurantRegister() {
        String restaurantName = restaurant_name.getText().toString();
        String restaurantLoc = restaurant_loc.getText().toString();
        String restaurantRuntime = restaurant_runtime.getText().toString();
        // 모든 입력 했는지 확인
        if (restaurantImgUri == null) {
            Toast.makeText(RestaurantRegisterActivity.this, "이미지를 등록하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (restaurantName.isEmpty()) {
            Toast.makeText(RestaurantRegisterActivity.this, "식당이름를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (restaurantLoc.isEmpty()) {
            Toast.makeText(RestaurantRegisterActivity.this, "식당 위치를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (restaurantRuntime.isEmpty()) {
            Toast.makeText(RestaurantRegisterActivity.this, "식당 운영시간을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (menuList.isEmpty()) {
            Toast.makeText(RestaurantRegisterActivity.this, "메뉴를 최소 1개이상 추가해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // Restaurant 객체를 생성하고 필드 값들을 설정한 후 서버로 전송
        Restaurant restaurant = new Restaurant(restaurantName,restaurantLoc,restaurantRuntime,menuList);

        // 식당이미지Uri 파일화
//        File restaurnatImgFile = new File(getRealPathFromURI(restaurantImgUri));
        File restaurnatImgFile = new File(restaurantImgUri.getPath());
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), restaurnatImgFile);
        MultipartBody.Part restaurnatImgPart = MultipartBody.Part.createFormData("restaurantImg", restaurnatImgFile.getName(), imageRequestBody);

        if (restaurnatImgFile == null){
            System.out.println("파일이 등록안됨");
        }

        // 메뉴이미지Uri 파일화
        List<File> menuImgFiles = new ArrayList<>();
        for (Uri uri : menuImgUriList) {
//            File imgFile = new File(getRealPathFromURI(uri));
            File imgFile = new File(uri.getPath());
            menuImgFiles.add(imgFile);
        }


        // imageFiles 리스트에 이미지 파일들을 추가
        List<MultipartBody.Part> menuImgParts = new ArrayList<>();
        for (File imageFile : menuImgFiles) {
            imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("menuImgList", imageFile.getName(), imageRequestBody);
            menuImgParts.add(imagePart);
        }
        //  retrofit통신
        Call<String> call = NetworkHelper.getInstance().getApiService().restaurantRegister(restaurant,restaurnatImgPart,menuImgParts);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody != null && responseBody.equals("success")) {
                        // "success" 문자열이 도착한 경우 처리할 코드 작성
                        // 서버로부터 정상적인 응답을 받은 경우 처리하는 코드
                        Intent intent = new Intent(RestaurantRegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RestaurantRegisterActivity.this, "식당등록 성공", Toast.LENGTH_SHORT).show();
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








    // 테스트
    //retrofit 통신
    private void test() {

        // 식당이미지Uri 파일화
//        File restaurnatImgFile = new File(getRealPathFromURI(restaurantImgUri));
        File restaurnatImgFile = getRealPathFromURI(restaurantImgUri);
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), restaurnatImgFile);
        MultipartBody.Part restaurnatImgPart = MultipartBody.Part.createFormData("restaurantImg", restaurnatImgFile.getName(), imageRequestBody);

        //  retrofit통신
        Call<String> call = NetworkHelper.getInstance().getApiService().test(restaurnatImgPart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();
                    if (responseBody != null && responseBody.equals("success")) {
                        // "success" 문자열이 도착한 경우 처리할 코드 작성
                        // 서버로부터 정상적인 응답을 받은 경우 처리하는 코드
                        Intent intent = new Intent(RestaurantRegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RestaurantRegisterActivity.this, "테스트 이미지 저장 성공", Toast.LENGTH_SHORT).show();
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
                    t.printStackTrace();
                } else {
                    Log.e(TAG, "Unexpected failure");
                }
            }
        });
    }
    /// 이미url 파일 Path로 변환
    private File getRealPathFromURI(Uri imageUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 MediaStore API를 사용하여 파일에 액세스해야합니다.
            try (Cursor cursor = getContentResolver().query(imageUri,
                    new String[] { MediaStore.Images.Media.DISPLAY_NAME },
                    null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    String fileName = cursor.getString(0);
                    File imgFile = new File(Environment.getExternalStorageDirectory(), fileName);
//                    File imgFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
                    // imgFile을 사용하여 파일 작업을 수행합니다.
                    return imgFile;
                }
            } catch (NullPointerException e) {
                // 파일이 없거나 경로가 잘못된 경우 처리
                e.printStackTrace();
            } catch (SecurityException e) {
                // 보안 예외 처리
                e.printStackTrace();
            }
        } else {
            // Android 9 이하에서는 uri.getPath()를 사용하여 파일에 액세스할 수 있습니다.
            File imgFile = new File(imageUri.getPath());
            // imgFile을 사용하여 파일 작업을 수행합니다.
            // ...
            return imgFile;
        }
        return null;
    }

}
