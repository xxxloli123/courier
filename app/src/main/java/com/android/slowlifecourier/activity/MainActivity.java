package com.android.slowlifecourier.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.fragment.DistributionFragment;
import com.android.slowlifecourier.fragment.PersonalCenterFragment;
import com.android.slowlifecourier.util.InstallUtils;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private Button selectButton;
    private RadioButton distribution, personalCenter;
    private RadioButton[] rButton;

    private final static String TAG_KEY = "TAG_KEY";
    private final static String HOME = "DISTRIBUTION";
    private final static String INFORMATION = "PERSONALCENTER";


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            button((Button) v);
        }
    };

    private DistributionFragment distributionFragment;
    private PersonalCenterFragment personalCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
//        update(getVersionCode(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public  void update(final int versionCode) {
        Request req = new Request.Builder()
                .tag("")
                .url(Config.Url.getUrl(Config.UPDATE)).build();
        new OkHttpClient().newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int ver = json.getJSONObject("appVersion").getInt("version");
                    if (ver > versionCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                upData2();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static Handler handler = new Handler();

    public void upData2(){
        Toast.makeText(this, "检测到新的版本，自动为您下载。。。", Toast.LENGTH_SHORT).show();
        new InstallUtils(this, Config.Url.getUrl("/slowlife/share/appdownload?type=android"), "惠递",
                new InstallUtils.DownloadCallBack() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(String path) {
                        if (distribution!=null)distribution.setText("订单处理");
                        InstallUtils.installAPK(MainActivity.this, path, getPackageName() + ".provider", new InstallUtils.InstallCallBack() {
                            @Override
                            public void onSuccess() {

                                Toast.makeText(MainActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(MainActivity.this, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("onFail","安装失败:" + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        if (distribution!=null)distribution.setText((int) (current * 100 / total)+"%");
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(MainActivity.this, "下载失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("onFail","下载失败:" + e.toString());
                    }

                }).downloadAPK();
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
    private void init(Bundle savedInstanceState) {
        distribution = (RadioButton) findViewById(R.id.distribution);
        personalCenter = (RadioButton) findViewById(R.id.paren);
        distribution.setOnClickListener(onClickListener);
        personalCenter.setOnClickListener(onClickListener);
        rButton = new RadioButton[]{distribution, personalCenter};

        int index = 0;
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(TAG_KEY, 0);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                index = bundle.getInt(TAG_KEY, 0);
            }
        }

        if (index < 0 || index > rButton.length - 1) {
            index = 0;
        }
        button(rButton[index]);
    }

    /**
     * 点击时需要切换相应的页面
     *
     * @param b 当前需要传入的按钮
     */
    private void button(Button b) {
        if (selectButton != null && selectButton == b) {
            return;
        }
        selectButton = b;
        Bundle bundle = new Bundle();
        if (selectButton == distribution) {
            if (distributionFragment == null) {
                distributionFragment = addFragment(DistributionFragment.class, HOME, bundle);
            }
            changeFragment(distributionFragment);
        } else if (selectButton == personalCenter) {
            if (personalCenterFragment == null) {
                personalCenterFragment = addFragment(PersonalCenterFragment.class, INFORMATION, bundle);
            }
            changeFragment(personalCenterFragment);
        }
    }

    /**
     * 添加管理fragment 并返回
     *
     * @param fragmentClass 传入的fragment类
     * @param tag           fragment标识
     * @param bundle
     * @return
     */
    private <T> T addFragment(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle) {
        //Fragment 管理者
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, fragmentClass.getName(), bundle);
            if (bundle != null) {
                bundle.putString("fragment", fragmentClass.getName());
                fragment.setArguments(bundle);
            }

            transaction.add(R.id.menus_frame, fragment, tag);
            transaction.commit();
        }
        return (T) fragment;
    }

    /*

        Fragment homeFragment=manager.findFragmentByTag(HOME);
        if(homeFragment!=null&&homeFragment!=fragment){
            transaction.hide(homeFragment);
            homeFragment.setUserVisibleHint(false);
        }

        Fragment informationFragment=manager.findFragmentByTag(INFORMATION);
        if(informationFragment!=null&&informationFragment!=fragment){
            transaction.detach(informationFragment);
            informationFragment.setUserVisibleHint(false);
        }

        if(fragment!=null){
            if(fragment!=homeFragment&&fragment.isDetached()){
                transaction.attach(fragment);
            }else if(fragment==homeFragment&&fragment.isHidden()){
                transaction.show(fragment);
            }
            fragment.setUserVisibleHint(true);
        }

     */

    /**
     * 切换fragment
     *
     * @param fragment 传入当前切换的fragment
     */
    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (distributionFragment == fragment) {
            if (personalCenterFragment != null && !personalCenterFragment.isHidden())
                transaction.hide(personalCenterFragment);
        } else {
            if (distributionFragment != null && !distributionFragment.isHidden())
                transaction.hide(distributionFragment);
        }
        if (fragment.isDetached())
            transaction.attach(fragment);
        else if (fragment.isHidden()) transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }
}
