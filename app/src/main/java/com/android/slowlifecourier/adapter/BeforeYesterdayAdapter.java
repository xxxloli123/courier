package com.android.slowlifecourier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlifecourier.fragment.BeforeYesterdayOrderFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class BeforeYesterdayAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private BeforeYesterdayOrderFragment beforeYesterdayOrderFragment;
    public BeforeYesterdayAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                beforeYesterdayOrderFragment = new BeforeYesterdayOrderFragment();
                return beforeYesterdayOrderFragment;
            case 1:
                beforeYesterdayOrderFragment = new BeforeYesterdayOrderFragment();
                return beforeYesterdayOrderFragment;
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
