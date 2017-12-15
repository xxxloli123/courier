package com.android.slowlifecourier.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.fragment.BeforeYesterdayFragment;
import com.android.slowlifecourier.fragment.TodayFragment;
import com.android.slowlifecourier.fragment.YesterdayFragment;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.view.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class ComplainOrderActivity extends FragmentActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.menus_frame)
    FrameLayout menusFrame;
    @BindView(R.id.before_yesterday)
    RadioButton beforeYesterday;
    @BindView(R.id.yesterday)
    RadioButton yesterday;
    @BindView(R.id.today)
    RadioButton today;
    private Button selectButton;
    private RadioButton[] rButton;

    private final static String TAG_KEY = "TAG_KEY";
    private final static String TODAY = "today";
    private final static String YESTERDAY = "yesterday";
    private final static String BEFORE_YESTERDAY = "before_yesterday";
    private TodayFragment todayFragment;
    private YesterdayFragment yesterdayFragment;
    private BeforeYesterdayFragment beforeYesterdayFragment;
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            button((Button) v);
        }
    };

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.blue);//状态栏设置为蓝色
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(Color.TRANSPARENT);//导航栏设置为透明色
        }
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_complain_order);
        unbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /*
         * onSaveInstanceState方法会在什么时候被执行，有这么几种情况：
        1、当用户按下HOME键时。
        这是显而易见的，系统不知道你按下HOME后要运行多少其他的程序，自然也不知道activity A是否会被销毁，故系统会调用onSaveInstanceState，让用户有机会保存某些非永久性的数据。以下几种情况的分析都遵循该原则
        2、长按HOME键，选择运行其他的程序时。
        3、按下电源按键（关闭屏幕显示）时。
        4、从activity A中启动一个新的activity时。
        5、屏幕方向切换时，例如从竖屏切换到横屏时。
        onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用，这是系统的责任，因为它必须要提供一个机会让你保存你的数据
         */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_KEY, getIndex());
    }

    private int getIndex() {
        int index = -1;
        for (int i = 0; i < rButton.length; i++) {
            if (selectButton == rButton[i]) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        today.setOnClickListener(onClickListener);
        yesterday.setOnClickListener(onClickListener);
        beforeYesterday.setOnClickListener(onClickListener);
        rButton=new RadioButton[] {today,yesterday,beforeYesterday};

        int index=0;
        if(savedInstanceState!=null){
            index=savedInstanceState.getInt(TAG_KEY,0);
        }else{
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null){
                index=bundle.getInt(TAG_KEY,0);
            }
        }

        if(index<0||index>rButton.length-1){
            index=0;
        }
        button(rButton[index]);
    }
    /**
     * 点击时需要切换相应的页面
     * @param b 当前需要传入的按钮
     */
    private void button(Button b){
        if(selectButton!=null&&selectButton==b){
            return;
        }
        selectButton=b;
        Bundle bundle=new Bundle();
        if(selectButton==today){
            if(todayFragment==null){
                todayFragment=addFragment(TodayFragment.class,TODAY,bundle);
            }
            changeFragment(todayFragment);
        }else if(selectButton==yesterday){
            if(yesterdayFragment==null){
                yesterdayFragment=addFragment(YesterdayFragment.class, YESTERDAY, bundle);
            }
            changeFragment(yesterdayFragment);
        }else if(selectButton==beforeYesterday){
            if(beforeYesterdayFragment==null){
                beforeYesterdayFragment=addFragment(BeforeYesterdayFragment.class, BEFORE_YESTERDAY, bundle);
            }
            changeFragment(beforeYesterdayFragment);
        }
    }

    /**
     * 添加管理fragment 并返回
     * @param fragmentClass  传入的fragment类
     * @param tag fragment标识
     * @param bundle
     * @return
     */
    private <T> T addFragment(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle){
        //Fragment 管理者
        FragmentManager manager=getSupportFragmentManager();
    	/*
		 * FragmentTransaction方法对应Fragment生命周期方法：
		1. add() : onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
		2. remove() : onPause -> onStop -> onDestoyView -> onDestory -> onDetach
		3. attach() :  onCreateView -> onActivityCreated -> onStart -> onResume
		4. detach() : onPause -> onStop -> onDestoryView
		5. show() : 没有
		6. hide() : 没有
		 *
		 */
        FragmentTransaction transaction=manager.beginTransaction();
        Fragment fragment=manager.findFragmentByTag(tag);
        if(fragment==null){
            fragment=Fragment.instantiate(this, fragmentClass.getName(), bundle);
            if(bundle!=null){
                bundle.putString("fragment", fragmentClass.getName());
                fragment.setArguments(bundle);
            }
            transaction.add(R.id.menus_frame,fragment, tag);
            transaction.commit();
        }
        return (T) fragment;
    }

    /**
     * 切换fragment
     * @param fragment 传入当前切换的fragment
     */
    private void changeFragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();

        Fragment todayFragment=manager.findFragmentByTag(TODAY);
        if(todayFragment!=null&&todayFragment!=fragment){
            transaction.hide(todayFragment);
            todayFragment.setUserVisibleHint(false);
        }

        Fragment yesterdayFragment=manager.findFragmentByTag(YESTERDAY);
        if(yesterdayFragment!=null&&yesterdayFragment!=fragment){
            transaction.detach(yesterdayFragment);
            yesterdayFragment.setUserVisibleHint(false);
        }

        Fragment beforeYesterdayFragment=manager.findFragmentByTag(BEFORE_YESTERDAY);
        if(beforeYesterdayFragment!=null&&beforeYesterdayFragment!=fragment){
            transaction.detach(beforeYesterdayFragment);
            beforeYesterdayFragment.setUserVisibleHint(false);
        }
        if(fragment!=null){
            if(fragment!=todayFragment&&fragment.isDetached()){
                transaction.attach(fragment);
            }else if(fragment==todayFragment&&fragment.isHidden()){
                transaction.show(fragment);
            }
            fragment.setUserVisibleHint(true);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.next:
                break;
        }
    }
}
