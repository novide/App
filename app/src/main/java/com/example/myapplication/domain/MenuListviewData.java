package com.example.myapplication.domain;

import android.graphics.Bitmap;

public class MenuListviewData {
    int listViewID;
    Bitmap menuImg;
    String menuName;
    String menuPrice;

    public MenuListviewData(int listViewID, Bitmap menuImg, String menuName, String menuPrice) {
        this.listViewID = listViewID;
        this.menuImg = menuImg;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public int getListViewID() {
        return listViewID;
    }

    public Bitmap getMenuImg() {
        return menuImg;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

}
