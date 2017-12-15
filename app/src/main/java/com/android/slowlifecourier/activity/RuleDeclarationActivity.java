package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.util.CacheActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class RuleDeclarationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_rule_declaration);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.back_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
