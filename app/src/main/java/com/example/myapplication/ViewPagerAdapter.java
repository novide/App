package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
        arrayList.add(new KoreanFoodFragment());
        arrayList.add(new JapanFoodFragment());
        arrayList.add(new ChinaFoodFragment());
        arrayList.add(new MixedFoodFragment());

        name.add("한식");
        name.add("중식");
        name.add("일식");
        name.add("양식");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return name.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }
}
