package com.android.slowlifecourier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.android.slowlifecourier.fragment.PickUpFragment;
import com.android.slowlifecourier.fragment.ServedFragment;
import com.android.slowlifecourier.fragment.UnPayedFrag;
import com.android.slowlifecourier.fragment.WaitingListFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class OrdersAdapter extends FragmentPagerAdapter {
    private ArrayList<String> list;
    private Fragment waitingListFragment, pickUpFragment, unpayedFrag, servedFragment;

    public OrdersAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (waitingListFragment == null)
                    waitingListFragment = new WaitingListFragment("rob", null);
                return waitingListFragment;
            case 1:
                if (pickUpFragment == null)
                    pickUpFragment = new PickUpFragment(null, "ReceivedOrder");
                return pickUpFragment;
            case 2:
                if (unpayedFrag == null)
                    unpayedFrag = new UnPayedFrag(null, "UnPayed");
                return unpayedFrag;

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
