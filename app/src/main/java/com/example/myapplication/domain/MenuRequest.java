package com.example.myapplication.domain;

public class MenuRequest {
    private String menuName;
    private String menuPrice;
    private String menuImg;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }
}
