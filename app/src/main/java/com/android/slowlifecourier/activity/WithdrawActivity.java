package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.PayUtil;
import com.android.slowlifecourier.R;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class WithdrawActivity extends BaseActivity {

    public static final String BALANCE = "balance";
    private double balance;


    @Override
    protected void init() {
        balance = getIntent().getDoubleExtra(BALANCE, -1);
        if (balance == -1) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        (((TextView) findViewById(R.id.cash))).setText(String.valueOf(balance));
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.ali:
                startActivity(new Intent(this, CashAliActivity.class));
                break;
            case R.id.car:
                Toast.makeText(this, "暂未开通", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        PayUtil.clear();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }
}
