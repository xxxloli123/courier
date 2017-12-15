package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.FragPagerAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.fragment.FragLoginPwd;
import com.android.slowlifecourier.fragment.FragLoginSms;
import com.android.slowlifecourier.view.PagerSlidingTabStrip;
import com.sgrape.BaseActivity;
import com.sgrape.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.login_viewpager)
    ViewPager viewPager;
    List<BaseFragment> frags = new ArrayList<>();
    private int rob;

    /**
     * 初始化控件
     */
    protected void init() {
        if (((MyApplication)getApplication()).getInfo()!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
            return;
        }

        frags.add(new FragLoginPwd());
        frags.add(new FragLoginSms());
        viewPager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), frags, "密码登录", "短信登录"));
        tabs.setViewPager(viewPager);
    }
    

    @Override
    protected void initListener() {
        tabs.setOnPagerTitleItemClickListener(new PagerSlidingTabStrip.OnPagerTitleItemClickListener() {
            @Override
            public void onSingleClickItem(int position) {
            }

            @Override
            public void onDoubleClickItem(int position) {
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }


}
