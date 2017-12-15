package com.android.slowlifecourier.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.Common;
import com.android.slowlifecourier.view.TimeButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class NewPhoneNumberActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.send_code_text)
    TimeButton sendCodeText;
    @BindView(R.id.verification_code)
    EditText verificationCode;
    @BindView(R.id.ensure_bt)
    Button ensureBt;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_new_phone_number);
        unbinder = ButterKnife.bind(this);
        verificationCode.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.send_code_text, R.id.ensure_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.send_code_text:
                sendCodeText.setTextAfters("重发验证码（").setTextAfter("）").setTextBefore("发送验证码").setLenght(60 * 1000);
                sendCodeText.setEnabled(false);
                sendCodeText.setTextColor(getResources().getColor(R.color.hint2_text_color));
                break;
            case R.id.ensure_bt:
                if(Common.isNull(verificationCode.getText().toString().trim())){
                    Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
                }else if(Common.isNull(phone.getText().toString().trim())){
                    Toast.makeText(this,"手机号为空",Toast.LENGTH_SHORT).show();
                }else {
                    delgeteDialog();
                }
                break;
        }
    }
    /**
     * 电话号码监听事件editText监听事件
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
            if(!Common.isNull(verificationCode.getText().toString().trim())&&!Common.isNull(phone.getText().toString().trim())){
                ensureBt.setTextColor(getResources().getColor(R.color.white));
            }else{
                ensureBt.setTextColor(getResources().getColor(R.color.hint2_text_color));
            }
            if (!Common.isNull(verificationCode.getText().toString().trim())) {
                verificationCode.setEnabled(true);
            }
        }
    };
    /**
     * dialog窗口
     * */
    private void delgeteDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
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
}
