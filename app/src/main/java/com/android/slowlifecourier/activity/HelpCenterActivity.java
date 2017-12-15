package com.android.slowlifecourier.activity;

import android.content.Intent;
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

public class HelpCenterActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.common_problem)
    RelativeLayout commonProblem;
    @BindView(R.id.rule_declaration)
    RelativeLayout ruleDeclaration;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_help_center);
        unbinder=ButterKnife.bind(this);
    }

    @OnClick({R.id.back_rl, R.id.common_problem, R.id.rule_declaration})
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.common_problem://常见问题
                intent=new Intent(this,CommonProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.rule_declaration://平台规则
                intent=new Intent(this,RuleDeclarationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
