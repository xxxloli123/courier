package com.android.slowlifecourier.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.MainActivity;
import com.android.slowlifecourier.activity.RegisterActivity;
import com.android.slowlifecourier.activity.ResetPasswordActivity;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.util.Common;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseFragment;
import com.slowlife.lib.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sgrape on 2017/5/24.
 * e-mail: sgrape1153@gmail.com
 */

public class FragLoginPwd extends BaseFragment {
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.verification_code_edit)
    EditText verificationCodeEdit;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.login_resetPwd)
    TextView loginResetPwd;
    @BindView(R.id.login_regisiter)
    TextView loginRegisiter;

    public FragLoginPwd() {
        super();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_login_pwd;
    }

    @OnClick({R.id.login_regisiter, R.id.login_bt, R.id.login_resetPwd})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_regisiter:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.login_resetPwd:
                startActivity(new Intent(getContext(), ResetPasswordActivity.class));
                break;
            case R.id.login_bt:
                login();
                break;
        }
    }


    private void login() {
        String phone = phoneEdit.getText().toString().trim();
        String pwd = verificationCodeEdit.getText().toString().trim();
        if (!Common.matchePhone(phone)) {
            Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(pwd)) {
            Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        JSONObject user = new JSONObject();
        try {
//            userStr：包含phone,password,type,phoneType(Android-安卓 Ios-IOS),token(推送唯一标示)
            user.put("phone", phone);
            user.put("password", MD5.md5Pwd(pwd));
            user.put("phoneType", "Android");
            user.put("type", "Courier");
            user.put("token", ((MyApplication) getContext().getApplicationContext()).getToken());
            map.put("userStr", user);
            newCall(Config.Url.getUrl(Config.LOGIN), map);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.LOGIN:
                Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                ((MyApplication) getContext().getApplicationContext()).setInfo(new Gson().fromJson(json.getString("user"), Info.class));
                if (getActivity() != null) getActivity().finish();
                break;
        }
    }

}
