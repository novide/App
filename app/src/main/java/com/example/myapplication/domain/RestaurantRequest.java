package com.example.myapplication.domain;

import java.util.List;

public class RestaurantRequest {
    private String restaurantName;   // 식당이름 정보
    private String restaurantLocation;// 식당 위치 정보
    private String restaurantOperatingTime; // 운영시간 정보
    private String category; //카테고리
    private List<Menu> menuList; // 추가할메뉴리스트 저장시 위에 값들만 저장

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getRestaurantOperatingTime() {
        return restaurantOperatingTime;
    }

    public void setRestaurantOperatingTime(String restaurantOperatingTime) {
        this.restaurantOperatingTime = restaurantOperatingTime;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
    public RestaurantRequest(String restaurantName, String restaurantLocation, String restaurantOperatingTime,String category, List<Menu> menuList) {
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.restaurantOperatingTime = restaurantOperatingTime;
        this.category = category;
        this.menuList = menuList;
    }
}
