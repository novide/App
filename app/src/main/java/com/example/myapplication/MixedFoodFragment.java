package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.domain.ListViewData;
import com.example.myapplication.retrofit.NetworkHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MixedFoodFragment extends Fragment {
    private static final String TAG = "MainActivity";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    ArrayList<ListViewData> restaurantDataList;
    MyAdapter myAdapter;

    public MixedFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static MixedFoodFragment newInstance(String param1, String param2) {
        MixedFoodFragment fragment = new MixedFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mixed_food, null);

        // 식당 목록 나열해주는 변수 및 함수
        ListView listView = view.findViewById(R.id.mixedFoodListView);
        restaurantDataList = new ArrayList<ListViewData>();
        myAdapter = new MyAdapter(context, restaurantDataList);
        listView.setAdapter(myAdapter);
        searchByMixedFoodList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mixed_food, container, false);
    }

    private void searchByMixedFoodList(){
        String category = "양식";
        Call<Map<String, Object>> call = NetworkHelper.getInstance().getApiService().getCategorySearchData(category);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    // 데이터를 정상적으로 받아온 경우 처리할 로직을 작성합니다.
                    Map<String, Object> data = response.body();
                    List<String> restaurantNameList = (List<String>) data.get("restaurantNameList");
                    List<String> imageList = (List<String>) data.get("imageList");
                    List<Bitmap> imgBitmapList = new ArrayList<>();
                    for (String image : imageList) {
                        byte[] imageBytes = android.util.Base64.decode(image, android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imgBitmapList.add(bitmap);
                    }
                    for (int i = 0; i < imgBitmapList.size(); i++) {
                        restaurantDataList.add(new ListViewData(i+1, imgBitmapList.get(i), restaurantNameList.get(i)));
                    }
                    myAdapter.notifyDataSetChanged(); // 리스트뷰 갱신
                } else {
                    // 데이터를 받아오는 도중 오류가 발생한 경우 처리할 로직을 작성합니다.
                    Log.e(TAG, "Response failed"); //username 중복
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
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