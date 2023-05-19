package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.domain.Menu;
import com.example.myapplication.domain.RestaurantRequest;
import com.example.myapplication.retrofit.NetworkHelper;
import com.google.gson.Gson;

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

    // 식당 이미지 불러오기 위한 위젯 변수들
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton restaurant_img_btn;
    private TextView restaurant_img;

    // 메뉴 이미지 불러오기 위한 위젯 변수들
    private static final int PICK_IMAGE_REQUEST1 = 2;
    private ImageButton restaurant_menu_btn;
    private TextView restaurant_menu_img;

    private EditText restaurant_name;
    private EditText restaurant_loc;
    private EditText restaurant_runtime;
    private Spinner category_spn;
    private EditText restaurant_menu;
    private EditText restaurant_price;
    private Button restaurant_register_btn;

    // 식당 메뉴, 가격 등록 위젯 버튼
    private ImageButton restaurant_addmenu_btn;

    // 등록한 식당 메뉴, 가격 아래에 나오게 해주는 위젯
    private ScrollView scrollView2;

    private LinearLayout linearLayout;
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
        category_spn = findViewById(R.id.spn_SPList);

        restaurant_menu = findViewById(R.id.restaurant_menu);
        restaurant_price = findViewById(R.id.restaurant_price);
        restaurant_addmenu_btn = findViewById(R.id.restaurant_addmenu_btn);

        restaurant_img_btn = findViewById(R.id.restaurant_img_btn);
        restaurant_img = findViewById(R.id.restaurant_img);

        restaurant_menu_btn = findViewById(R.id.restaurant_menu_btn);
        restaurant_menu_img = findViewById(R.id.restaurant_menu_img);
        restaurant_register_btn = findViewById(R.id.restaurant_register_btn);

        scrollView2 = findViewById(R.id.scrollView2);
        linearLayout = findViewById(R.id.linearLayout);

        Spinner spn = (Spinner) findViewById(R.id.spn_SPList);

        ArrayAdapter spn_adapter = ArrayAdapter.createFromResource(this, R.array.FoodVariety, android.R.layout.simple_spinner_item);
        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(spn_adapter);

        // 카테고리 리스트 함수
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String a = spn.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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
                String menuName = restaurant_menu.getText().toString().trim();
                String price = restaurant_price.getText().toString().trim();
                // 메뉴 아이템을 추가할 뷰 생성
                LinearLayout itemLayout = new LinearLayout(RestaurantRegisterActivity.this);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(  // LinearLayout의 레이아웃 속성을 설정
                        LinearLayout.LayoutParams.MATCH_PARENT,       // 첫번째 인수 : 레이아웃의 너비
                        LinearLayout.LayoutParams.WRAP_CONTENT));     // 두번째 인수 : 레이아웃의 높이
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);   // 가로로 정렬되게( 메뉴, 가격 순으로)

                // 메뉴 텍스트뷰 생성
                TextView menuTextView = new TextView(RestaurantRegisterActivity.this);
                menuTextView.setText(restaurant_menu.getText().toString());
                menuTextView.setTextSize(18);
                menuTextView.setPadding(20,20,0,0);
                menuTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,             // 가로 방향에서 1의 비율을 가짐을 의미
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); //  LinearLayout의 다른 자식 View와 함께 놓였을 때,
                // LinearLayout 내에서의 가로 방향의 비율을 1:1로 맞출 수 있음

                // 가격 텍스트뷰 생성
                TextView priceTextView = new TextView(RestaurantRegisterActivity.this);
                priceTextView.setText(restaurant_price.getText().toString());
                priceTextView.setTextSize(18);
                priceTextView.setPadding(0,20,35,0);
                priceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // "restaurant_menu"와 "restaurant_price" 뷰를 linearLayout에 추가
                itemLayout.addView(menuTextView);
                itemLayout.addView(priceTextView);
                linearLayout.addView(itemLayout);

                // EditText 비우기
                restaurant_menu.setText("");
                restaurant_price.setText("");
                restaurant_menu_img.setText("");

                // 이미지 경로를 리스트에 추가
                if (menuImgUriList == null) {
                    menuImgUriList = new ArrayList<>();
                    menuImgUriList.add(menuImgUri);
                } else {
                    menuImgUriList.add(menuImgUri);
                }
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
                restaurantRegister();
            }
        });
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
        String category = category_spn.getSelectedItem().toString();
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
        if (category.isEmpty()){
            Toast.makeText(RestaurantRegisterActivity.this, "카테고리를 선택하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (menuList.isEmpty()) {
            Toast.makeText(RestaurantRegisterActivity.this, "메뉴를 최소 1개이상 추가해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // Restaurant 객체를 생성하고 필드 값들을 설정한 후 서버로 전송
        RestaurantRequest restaurantRequest = new RestaurantRequest(restaurantName,restaurantLoc,restaurantRuntime,category,menuList);
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(restaurantRequest));

        // 식당이미지Uri 파일화
        File restaurantImgFile = getRealPathFromURI(restaurantImgUri);
        if (restaurantImgFile == null){
            System.out.println("파일이 등록안됨");
        }
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), restaurantImgFile);
        MultipartBody.Part restaurantImgPart = MultipartBody.Part.createFormData("restaurantImg", restaurantImgFile.getName(), imageRequestBody);

        // 메뉴이미지Uri 파일화
        List<File> menuImgFiles = new ArrayList<>();
        for (Uri uri : menuImgUriList) {
            File imgFile = getRealPathFromURI(uri);
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
        Call<String> call = NetworkHelper.getInstance().getApiService().restaurantRegister(requestBody,restaurantImgPart,menuImgParts);
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


    /// 이미url 파일 Path로 변환
    private File getRealPathFromURI(Uri imageUri) {
        String filePath = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상에서는 MediaStore API를 사용하여 파일에 액세스해야합니다.
            try (Cursor cursor = getContentResolver().query(imageUri,
                    new String[] { MediaStore.Images.Media.DISPLAY_NAME },
                    null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    String fileName = cursor.getString(0);
//                    File imgFile = new File(Environment.getExternalStorageDirectory(), fileName);
//                    File imgFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
//                    File imgFile = new File(getFilesDir(), fileName);
                    File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);  //최종 가상 디바이스 Download파일 내 이미지
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
