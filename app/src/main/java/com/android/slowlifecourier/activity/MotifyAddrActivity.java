package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MotifyAddrActivity extends BaseActivity {

    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.address)
    EditText address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_motify_addr;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.done:
                submit();
                break;
        }
    }

    private void submit() {
        String addr = address.getText().toString().trim();
        if (isEmpty(addr)) {
            Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> params = new HashMap<>();
        newCall(Config.Url.getUrl(Config.MOTIFYADDRESS), params);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
