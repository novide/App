package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.domain.ListViewData;
import com.example.myapplication.domain.MenuListviewData;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<MenuListviewData> sample;

    public MenuAdapter(Context context, ArrayList<MenuListviewData> data) {
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
    public MenuListviewData getItem(int listViewId) {
        return sample.get(listViewId);
    }

    @Override
    public View getView(int listViewId, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.menu_list_view, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.menuImg);
        TextView menuName = (TextView)view.findViewById(R.id.menuName);
        TextView menuPrice = (TextView)view.findViewById(R.id.menuPrice);

        imageView.setImageBitmap(sample.get(listViewId).getMenuImg());
        imageView.setPadding(10, 10, 0, 10);

        menuName.setText(sample.get(listViewId).getMenuName());
        menuName.setPadding(100, 50, 0, 0);
        menuName.setTextSize(25);
        menuName.setTextColor(Color.BLACK);

        menuPrice.setText(sample.get(listViewId).getMenuPrice());
        menuPrice.setPadding(30, 50, 0, 0);
        menuPrice.setTextSize(25);
        menuPrice.setTextColor(Color.BLACK);
        return view;
    }
}
