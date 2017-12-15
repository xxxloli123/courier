package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class PersonalInformationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.essential_information)
    RelativeLayout essentialInformation;
    @BindView(R.id.authentication_information)
    RelativeLayout authenticationInformation;
    @BindView(R.id.my_equipment)
    RelativeLayout myEquipment;
    @BindView(R.id.insurance_service)
    RelativeLayout insuranceService;
    @BindView(R.id.received_evaluation)
    RelativeLayout receivedEvaluation;


    @OnClick({R.id.back_rl, R.id.essential_information, R.id.authentication_information, R.id.my_equipment,
            R.id.insurance_service, R.id.received_evaluation})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.essential_information://基本信息
                intent = new Intent(this, EssentialInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.authentication_information://认证信息
                intent = new Intent(this, AuthenticationInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.my_equipment://我的装备
                intent = new Intent(this, MyEquipmentActivity.class);
                startActivity(intent);
                break;
            case R.id.insurance_service://保险服务
                Toast.makeText(this, "保险服务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.received_evaluation://收到的评价
                intent = new Intent(this, ReceivedEvaluationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_information;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }

}
