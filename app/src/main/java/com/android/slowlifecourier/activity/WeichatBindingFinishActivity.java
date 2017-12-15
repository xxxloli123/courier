package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
 * Created by Administrator on 2017/2/24 0024.
 */

public class WeichatBindingFinishActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.finishs)
    TextView finishs;
    @BindView(R.id.title)
    TextView title;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_weichat_binding_finish);
        unbinder = ButterKnife.bind(this);
        String tag=getIntent().getStringExtra("tag");
        if(tag.equals("bankcard")){
            title.setText("添加银行卡");
        }else if(tag.equals("weichat")){
            title.setText("微信绑定");
        }else if(tag.equals("alipay")){
            title.setText("支付宝绑定绑定");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.finishs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.finishs:
                Intent intent = new Intent(this, WithdrawActivity.class);
                startActivity(intent);
                CacheActivity.finishSingleActivity(new WithdrawActivity());
                finish();
                break;
        }
    }
}
