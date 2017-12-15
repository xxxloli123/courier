package com.android.slowlifecourier.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.id_number)
    EditText idNumber;
    @BindView(R.id.bank_card)
    EditText bankCard;
    @BindView(R.id.territoriality)
    EditText territoriality;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.bank_deposit)
    EditText bankDeposit;
    @BindView(R.id.submit_bt)
    Button submitBt;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_add_bank_card);
        unbinder = ButterKnife.bind(this);
        name.addTextChangedListener(textWatcher);
        idNumber.addTextChangedListener(textWatcher);
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
                if(Common.isNull(name.getText().toString().trim())){
                    Toast.makeText(this,"输入持卡人姓名",Toast.LENGTH_SHORT).show();
                }else if(Common.isNull(idNumber.getText().toString().trim())){
                    Toast.makeText(this,"输入身份证号",Toast.LENGTH_SHORT).show();
                }else if(Common.isNull(bankCard.getText().toString().trim())){
                    Toast.makeText(this,"输入银行卡号",Toast.LENGTH_SHORT).show();
                }else if(Common.isNull(phone.getText().toString().trim())){
                    Toast.makeText(this,"输入银行预留号",Toast.LENGTH_SHORT).show();
                    phoneDialog();
                }else{
                    explainDialog();
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
            if(name.getText().toString().trim().length()>0&&idNumber.getText().toString().trim().length()>0){
                submitBt.setBackgroundResource(R.drawable.background_bgall_bule);
            }else{
                submitBt.setBackgroundResource(R.drawable.bacground_bgall_light_gray);
            }
        }
    };
    /**
     * 持卡人说明dialog窗口
     * */
    private void explainDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_name, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        TextView title= (TextView) mDialog.findViewById(R.id.title);
        title.setText("持卡人说明");
        TextView textView= (TextView) mDialog.findViewById(R.id.name_text);
        textView.setText("为保障账户资金安全，只能绑定认证用户本人的银行卡");
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setText("知道了");
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddBankCardActivity.this,WeichatBindingFinishActivity.class);
                intent.putExtra("tag","bankcard");
                startActivity(intent);
                Common.bankcard="1";
                finish();
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        View view=mDialog.findViewById(R.id.view);
        mNo.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
    /**
     * 电话号码不一致dialog窗口
     * */
    private void phoneDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        TextView title= (TextView) mDialog.findViewById(R.id.title);
        title.setText("你填写的手机号码与银行预留的不一致，请前往柜台修改");
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
