package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.AuthenticationInformationEntity;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class AuthenticationInformationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.identity_tag)
    TextView identityTag;
    @BindView(R.id.identity_authentication_rl)
    RelativeLayout identityAuthenticationRl;
    @BindView(R.id.health_tag)
    TextView healthTag;
    @BindView(R.id.health_certification_rl)
    RelativeLayout healthCertificationRl;
    @BindView(R.id.driver_tag)
    TextView driverTag;
    @BindView(R.id.driver_license_rl)
    RelativeLayout driverLicenseRl;
    @BindView(R.id.vehicle_tag)
    TextView vehicleTag;
    @BindView(R.id.vehicle_certification_rl)
    RelativeLayout vehicleCertificationRl;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_authentication_information);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.identity_authentication_rl, R.id.health_certification_rl, R.id.driver_license_rl, R.id.vehicle_certification_rl})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.identity_authentication_rl://身份认证
                intent = new Intent(this, IdentityAuthenticationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.health_certification_rl://健康认证
                intent = new Intent(this, HealthCertificationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.driver_license_rl://驾照认证
                intent = new Intent(this, DriverLicenseActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.vehicle_certification_rl://车辆检查安全认证
                intent = new Intent(this, VehicleCertificationActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    /**
     * 回传值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            AuthenticationInformationEntity entity = new AuthenticationInformationEntity();
            Bundle bundle = data.getExtras();
            entity.setIdentityName(bundle.getString("identity"));
            entity.setVehicleName(bundle.getString("vehicle"));
            entity.setHealthName(bundle.getString("health"));
            entity.setDriverName(bundle.getString("driver"));
            if (!Common.isNull(entity.getIdentityName())) {
                identityTag.setText(entity.getIdentityName());
                entity.setIdentityTag("1");
            }
            if (!Common.isNull(entity.getVehicleName())) {
                vehicleTag.setText(entity.getVehicleName());
                entity.setVehicleTag(1);
            }
            if (!Common.isNull(entity.getHealthName())) {
                healthTag.setText(entity.getHealthName());
                entity.setHealthTag(1);
            }
            if (!Common.isNull(entity.getDriverName())) {
                driverTag.setText(entity.getDriverName());
                entity.setDriverTag(1);
            }
        }
    }
}
