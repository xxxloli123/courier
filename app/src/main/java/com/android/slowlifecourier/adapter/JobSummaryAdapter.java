package com.android.slowlifecourier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlifecourier.fragment.CompleteFrag;
import com.android.slowlifecourier.fragment.FragOrderList;
import com.android.slowlifecourier.fragment.UnPayedFrag;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class JobSummaryAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private Fragment allOrderFragment;

    public JobSummaryAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                allOrderFragment = new FragOrderList(null,
                        "ReceivedOrder,GoodsDelivery,CancelOrder,Complete,UnPayed", "CityWide,Intercity", false);
                return allOrderFragment;
            case 1:
                allOrderFragment = new CompleteFrag();
                return allOrderFragment;
            case 2:
                allOrderFragment = new UnPayedFrag(null, "UnPayed");
                return allOrderFragment;
//            case 2:
//                allOrderFragment = new FragOrderList(null, "CancelOrder", "CityWide,Intercity", false);
//                return allOrderFragment;
//            case 3:
//                allOrderFragment = new FragOrderList(null, "CancelOrder", "CityWide,Intercity", false);
//                return allOrderFragment;
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
