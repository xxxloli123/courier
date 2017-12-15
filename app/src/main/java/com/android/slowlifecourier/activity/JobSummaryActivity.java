package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.JobSummaryAdapter;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.view.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class JobSummaryActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_job_summary);
        unbinder = ButterKnife.bind(this);
        initPagers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.search_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.search_rl:
                break;
        }
    }
    /**
     * 根据选择的不同界面选择不同tabs
     * */
    private void initPagers() {
        ArrayList<String> list = new ArrayList<>();
        list.add("全部订单");
        list.add("已完成单");
//        list.add("异常单");
//        list.add("取消单");
        list.add("待付款");
        pager.setAdapter(new JobSummaryAdapter(getSupportFragmentManager(),list));
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
    }
}
