package com.android.slowlifecourier.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.MainActivity;
import com.android.slowlifecourier.activity.RegisterActivity;
import com.android.slowlifecourier.activity.ResetPasswordActivity;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.util.Common;
import com.android.slowlifecourier.view.TimeButton;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseFragment;

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

public class FragLoginSms extends BaseFragment {

    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.verification_code_edit)
    EditText verificationCodeEdit;
    @BindView(R.id.verification_code)
    TimeButton verificationCode;
    private String smsId = "";

    public FragLoginSms() {
        super();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_login_sms;
    }

    @Override
    protected void init() {
        verificationCode.setEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        verificationCode.setOnClickListener(this);
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Common.matchePhone(phoneEdit.getText().toString().trim())) {
                    verificationCode.setEnabled(true);
                } else verificationCode.setEnabled(false);
            }
        });
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
            case R.id.verification_code:
                final String phone = phoneEdit.getText().toString().trim();
                if (!Common.matchePhone(phone)) {
                    Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                verificationCode.setTextAfters("").setTextAfter("秒后重发").setTextBefore("获取验证码").setLenght(60 * 1000);
                verificationCode.start();
                newCall(Config.Url.getUrl(Config.SMS_CODE), Common.getCode(phone, "0"));
                break;
        }
    }

    private void login() {
        if (isEmpty(smsId)) {
            Toast.makeText(getContext(), "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = phoneEdit.getText().toString().trim();
        if (!Common.matchePhone(phone)) {
            Toast.makeText(getContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = verificationCodeEdit.getText().toString().trim();
        if (isEmpty(code)) {
            Toast.makeText(getContext(), verificationCodeEdit.getHint(), Toast.LENGTH_SHORT).show();
            return;
        }
//        userStr：包含phone,password,type,phoneType(Android-安卓 Ios-IOS),token(推送唯一标示)
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("phone", phone);
            userJson.put("phoneType", "Android");
            userJson.put("token", ((MyApplication) getActivity().getApplication()).getToken());
            userJson.put("type", "Courier");
            JSONObject smsJson = new JSONObject();
            smsJson.put("phone", phone);
            smsJson.put("code", code);
            smsJson.put("id", smsId);
            Map<String, Object> map = new HashMap<>();
            map.put("userStr", userJson);
            map.put("smsStr", smsJson);
            newCall(Config.Url.getUrl(Config.LOGIN), map);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.SMS_CODE:
                this.smsId = json.getJSONObject("sms").getString("id");
                break;
            case Config.LOGIN:
                Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                ((MyApplication) getContext().getApplicationContext()).setInfo(new Gson().fromJson(json.getString("user"), Info.class));
                if (getActivity() != null) getActivity().finish();
                break;
        }
    }
}
