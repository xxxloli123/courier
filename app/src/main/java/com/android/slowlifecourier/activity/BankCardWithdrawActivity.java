package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class BankCardWithdrawActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_bank_card_withdraw);
        unbinder = ButterKnife.bind(this);
        addBankCardDialog(BankCardWithdrawActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.back_rl)
    public void onClick() {
        finish();
    }
    /**
     * 弹出添加银行卡窗口
     */
    public static void addBankCardDialog(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(activity).create();
        final TextView text = (TextView) mDialog.findViewById(R.id.title);
        text.setText("你还没有用于提现的银行卡，请先加一张储蓄卡");
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setText("添加银行卡");
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,AddBankCardActivity.class);
                activity.startActivity(intent);
                dialog.dismiss();
                activity.finish();
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
