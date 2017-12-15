package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.view.View;

import com.Observable;
import com.Observer;
import com.alipay.sdk.PayUtil;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BindAliPayActivity extends BaseActivity implements Observer<Boolean> {
    public void onClick(View v) {
        if (v.getId() == R.id.back_rl) {
            finish();
            return;
        }
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", info.getId());
        map.put("type", "Alipay");
        newCall(Config.Url.getUrl(Config.ORDERBIND), map);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_ali_pay;
    }


    private void pay(String orderid) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderIds", orderid);
        map.put("way", "alipay");
        newCall(Config.Url.getUrl("slowlife/apppay/createtrade"), map);
    }

    @Override
    public void update(Observable observable, Boolean aBoolean) {
        if (aBoolean) {
            PayUtil.deleteObserver(this);
            finish();
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.ORDERBIND:
//                        {"orderId":"402881f05cc866d9015cc86b1c7b0002","message":"创建账户绑定订单成功(013)","statusCode":200}
                pay(json.getString("orderId"));
                break;
            case "slowlife/apppay/createtrade":
                PayUtil.addObserver(BindAliPayActivity.this);
                PayUtil.aliPay(BindAliPayActivity.this, json.getString("alipayBody"));
                break;
        }
    }
}
