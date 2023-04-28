package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.domain.ListViewData;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ListViewData> sample;

    public MyAdapter(Context context, ArrayList<ListViewData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int listViewId) {
        return listViewId;
    }

    @Override
    public ListViewData getItem(int listViewId) {
        return sample.get(listViewId);
    }

    @Override
    public View getView(int listViewId, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.restaurant_list_view, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.restaurantImg);
        TextView movieName = (TextView)view.findViewById(R.id.restaurantName);

        imageView.setImageBitmap(sample.get(listViewId).getRestaurantImg());
        imageView.setPadding(10, 10, 0, 10);
        movieName.setText(sample.get(listViewId).getRestaurantName());
        movieName.setPadding(30, 50, 0, 0);
        movieName.setTextSize(25);
        movieName.setTextColor(Color.BLACK);
        return view;
    }
}