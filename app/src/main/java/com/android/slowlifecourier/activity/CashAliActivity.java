package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Observable;
import com.Observer;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 提现到支付宝
 */
public class CashAliActivity extends BaseActivity implements Observer<Boolean> {

    @BindView(R.id.cash_sum)
    EditText sum;
    @BindView(R.id.cash_name)
    EditText name;
    @BindView(R.id.isbind)
    TextView textView;
    private boolean binded;
    private Info info;
    private Double balance;

    @Override
    protected void init() {
        info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadAccountInfo();
    }

    private void loadAccountInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", info.getId());
        newCall(Config.Url.getUrl(Config.ACCOUNT), map);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.cashAliPay:
                cash();
                break;
            case R.id.cash_bind:
                if (!binded)
                    startActivity(new Intent(this, BindAliPayActivity.class));
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_ali;
    }

    private void cash() {
        if (!binded) {
            Toast.makeText(this, "请先绑定支付宝账号", Toast.LENGTH_SHORT).show();
            return;
        }
        String txt = sum.getText().toString().trim();
        if (isEmpty(txt) || !txt.matches("^\\d$")) {
            Toast.makeText(this, "请输入正确的提现金额", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = this.name.getText().toString().trim();
        if (isEmpty(name)) {
            Toast.makeText(this, "请输入开户人姓名", Toast.LENGTH_SHORT).show();
            return;
        }

//        [userId, type, userName, amount]
        Map<String, Object> map = new HashMap<>();
        map.put("userId", info.getId());
        map.put("type", "Alipay");
        map.put("userName", name);
        map.put("amount", txt);
        newCall(Config.Url.getUrl(Config.CASH), map);
    }


    @Override
    public void update(Observable observable, Boolean aBoolean) {
        if (aBoolean) finish();
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.ACCOUNT:
                balance = Double.valueOf(json.getString("balance"));      //  余额
                binded = json.getString("alipayBinding").equals("yes");
                if (binded) {
                    textView.setText("已绑定");
                } else textView.setText("未绑定");
                break;
            case Config.CASH:
                Toast.makeText(CashAliActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
