package com.android.slowlifecourier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlifecourier.fragment.YesterdayOrderFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class YesterdayAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private YesterdayOrderFragment yesterdayOrderFragment;
    public YesterdayAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                yesterdayOrderFragment = new YesterdayOrderFragment();
                return yesterdayOrderFragment;
            case 1:
                yesterdayOrderFragment = new YesterdayOrderFragment();
                return yesterdayOrderFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
