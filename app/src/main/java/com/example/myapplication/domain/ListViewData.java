package com.example.myapplication.domain;

import android.graphics.Bitmap;

public class ListViewData  {
    int listViewID;
    Bitmap restaurantImg;
    String restaurantName;

    public int getListViewID() {
        return listViewID;
    }

    public Bitmap getRestaurantImg() {
        return restaurantImg;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public ListViewData(int listViewID, Bitmap restaurantImg, String restaurantName) {
        this.restaurantImg = restaurantImg;
        this.restaurantName = restaurantName;
        this.listViewID = listViewID;
    }

}
