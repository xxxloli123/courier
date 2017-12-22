package com.android.slowlifecourier.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.OrdersAdapter;
import com.android.slowlifecourier.bluetoothprint.SearchBluetoothActivity;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.view.PagerSlidingTabStrip;
import com.sgrape.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class DistributionFragment extends BaseFragment {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.printer_tv)
    TextView printerTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_distribution;
    }

    @Override
    protected void init() {
        initPagers();
        if (!TextUtils.isEmpty(AppInfo.btAddress)){
            printerTv.setText("已连接");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(AppInfo.btAddress)){
            if (printerTv!=null)printerTv.setText("已连接");
        }
    }

    /**
     * 根据选择的不同界面选择不同tabs
     */
    private void initPagers() {
        ArrayList<String> list = new ArrayList<>();
        list.add("待抢单");
        list.add("待取货");
        list.add("待付款");
        list.add("待送达");

        pager.setAdapter(new OrdersAdapter(getActivity().getSupportFragmentManager(), list));
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
    }

    @OnClick(R.id.printer_tv)
    public void onClick() {
        startActivity(new Intent(getActivity(),SearchBluetoothActivity.class));
    }
}
