package com.android.slowlifecourier.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class NameActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.sure_bt)
    Button sureBt;
    private MyApplication app;
    private Info info;

    @Override
    protected void initListener() {
    }

    @Override
    protected void init() {
        super.init();
        app = (MyApplication) getApplication();
        info = app.getInfo();
    }

    @OnClick({R.id.back_rl, R.id.sure_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.sure_bt:
                submit();
                break;
        }
    }

    private void submit() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", ((MyApplication) getApplication()).getInfo().getId());
            json.put("nickName", name.getText().toString().trim());
            Map<String, Object> params = new HashMap<>();
            params.put("userStr", json);
            info.setNickName(name.getText().toString().trim());
            newCall(Config.Url.getUrl(Config.NICKNAME), params);
        } catch (JSONException e) {
            Toast.makeText(this, "解析数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_name;
    }


    /**
     * dialog窗口
     */
    private void delgeteDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_name, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        TextView textView = (TextView) mDialog.findViewById(R.id.name_text);
        textView.setText("用户名只能修改一次确认将用户名修改为[" + name.getText().toString().trim() + "]");
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.NICKNAME:
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                app.setInfo(info);
                finish();
                break;
        }
    }
}
