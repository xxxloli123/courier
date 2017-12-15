package com.android.slowlifecourier;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2017/2/20 0020.
 * 项目中所有的activity继承基类BaseActivity做统一处理
 */

public  class BaseActivity extends AppCompatActivity {



    protected <T extends View> T findView(@IdRes int id){
        return (T)findViewById(id);
    }
}
