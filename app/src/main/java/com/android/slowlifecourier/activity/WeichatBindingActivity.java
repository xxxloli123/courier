package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class WeichatBindingActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.real_name)
    EditText realName;
    @BindView(R.id.submit_bt)
    Button submitBt;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_weichat_binding);
        unbinder = ButterKnife.bind(this);
        account.addTextChangedListener(textWatcher);
        name.addTextChangedListener(textWatcher);
        realName.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.submit_bt:
                if(account.getText().toString().trim().length()>0&&name.getText().toString().trim().length()>0
                        &&realName.getText().toString().trim().length()>0){
                    Common.weichat="1";
                    Intent intent=new Intent(this,WeichatBindingFinishActivity.class);
                    intent.putExtra("tag","weichat");
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }
    /**
     * editText监听事件
     */
    private TextWatcher textWatcher = new TextWatcher() {
        //text  输入框中改变后的字符串信息
        //start 输入框中改变后的字符串的起始位置
        //before 输入框中改变前的字符串的位置 默认为0
        //count 输入框中改变后的一共输入字符串的数量
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
            //s输入结束呈现在输入框中的信息
            if(account.getText().toString().trim().length()>0&&name.getText().toString().trim().length()>0
                    &&realName.getText().toString().trim().length()>0){
                submitBt.setBackgroundResource(R.drawable.background_bgall_bule);
            }else{
                submitBt.setBackgroundResource(R.drawable.bacground_bgall_light_gray);
            }
        }
    };
}
