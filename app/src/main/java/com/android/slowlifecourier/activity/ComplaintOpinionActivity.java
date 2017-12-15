package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class ComplaintOpinionActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.customer_service)
    TextView customerService;
    @BindView(R.id.slow_food)
    RelativeLayout slowFood;
    @BindView(R.id.bad_attitude)
    RelativeLayout badAttitude;
    @BindView(R.id.no_address)
    RelativeLayout noAddress;
    @BindView(R.id.other)
    RelativeLayout other;
    @BindView(R.id.suggest)
    RelativeLayout suggest;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_complaint_opinion);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.customer_service, R.id.slow_food, R.id.bad_attitude, R.id.no_address, R.id.other, R.id.suggest})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.customer_service:
                Common.phoneDialog(this,"023-1234-5678");
                break;
            case R.id.slow_food:
                intent=new Intent(this,ComplainOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.bad_attitude:
                intent=new Intent(this,ComplainOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.no_address:
                intent=new Intent(this,ComplainOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.other:
                intent=new Intent(this,ComplainOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.suggest:
                intent=new Intent(this,SuggestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
