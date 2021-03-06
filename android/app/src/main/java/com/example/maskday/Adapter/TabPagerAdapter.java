package com.example.maskday.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.maskday.Fragment.FreeBoardFragment;
import com.example.maskday.Fragment.NewsFragment;
import com.example.maskday.Fragment.QnAFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FreeBoardFragment freeBoardFragment = new FreeBoardFragment();
                return freeBoardFragment;
            case 1:
                QnAFragment qnAFragment = new QnAFragment();
                return qnAFragment;

            case 2:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
