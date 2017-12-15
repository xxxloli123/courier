package com.android.slowlifecourier.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.SelectProvinceAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.dialog.SelectCourier;
import com.android.slowlifecourier.objectmodle.CompanyEntity;
import com.android.slowlifecourier.util.Common;
import com.android.slowlifecourier.util.SimpleCallback;
import com.android.slowlifecourier.view.TimeButton;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;
import com.sgrape.dialog.Dialog;
import com.sgrape.dialog.LoadDialog;
import com.slowlife.lib.MD5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class RegisterActivity extends BaseActivity implements
        LocationSource,
        AMapLocationListener {

    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.district)
    TextView district;
    @BindView(R.id.selectDistrict)
    LinearLayout selectDistrict;
    //    @BindView(R.id.selectDistrict)
//    Spinner selectDistrict;
    private EditText phoneEdit;
    private TimeButton verificationCode;
    private EditText verificationCodeEdit;
    private Button button;
    private CheckBox checkBox;
    @BindView(R.id.selectCour)
    TextView selectCour;
    @BindView(R.id.register_pwd1)
    EditText pwd1;
    @BindView(R.id.register_pwd2)
    EditText pwd2;
    private LoadDialog dialog;
    private List<CompanyEntity> companyEntities;
    private int selectPosition = -1;
    private String smsId;
    /**
     * 定位
     */
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    // /声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private JSONArray areas;
    private int selectedPosition = -1;
    private JSONObject json;


    /**
     * 初始化控件
     */
    protected void init() {
        phoneEdit = (EditText) findViewById(R.id.phone_edit);
        phoneEdit.addTextChangedListener(textWatcher);
        verificationCode = (TimeButton) findViewById(R.id.verification_code);
        verificationCodeEdit = (EditText) findViewById(R.id.verification_code_edit);
        button = (Button) findViewById(R.id.button);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        String hint = checkBox.getText().toString().trim();
        int fstart = hint.indexOf("《掌升活快递员用户协议》");
        int fend = fstart + "《掌升活快递员用户协议》".length();
        CheckBox tvColor = (CheckBox) findViewById(R.id.checkbox);
        tvColor.setText(Common.textColor(hint, fstart, fend));
        //如果电话号码为空设置发送验证码按钮不可点击
        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (v.getId()) {
                    case R.id.phone_edit:
                        if (Common.isNull(phoneEdit.getText().toString().trim())) {
                            verificationCode.setEnabled(false);
                        }
                        break;
                }
            }
        });
        verificationCode.setOnClickListener(this);
        button.setOnClickListener(this);
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
            if (Common.matchePhone(s.toString().trim())) {
                verificationCode.setEnabled(true);
                button.setEnabled(true);
            } else {
                button.setEnabled(false);
                verificationCode.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.verification_code:
                String phone = phoneEdit.getText().toString().trim();
                if (!Common.matchePhone(phone)) {
                    Toast.makeText(this, "手机格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                verificationCode.setTextAfters("").setTextAfter("秒后重发").setTextBefore("获取验证码").setLenght(60 * 1000);
                verificationCode.start();
                newCall(Config.Url.getUrl(Config.SMS_CODE), Common.getCode(phone, "0"));
                break;
            case R.id.button:
                register();
                break;
            case R.id.selectCour:
                if (selectedPosition == -1) {
                    Toast.makeText(this, "请选择配送区域", Toast.LENGTH_SHORT).show();
                    return;
                }
//                selectCourMethod();
                SelectCourier dialog = new SelectCourier(this);
                dialog.setSelectedPosition(selectPosition);
                dialog.setData(companyEntities);
                dialog.setButtonClickListener(new DialogClickListener());
                dialog.show();
                break;
            case R.id.checkbox:
                checkBox.setChecked(true);
                intent = new Intent(this, HelpActivity.class);
                intent.putExtra(HelpActivity.URI, "app/appCourier/serviceContract.html");
                startActivity(intent);
                break;
            case R.id.selectDistrict:
                selectProvinceDialog();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void register() {
        String code = verificationCodeEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String pwd = pwd1.getText().toString().trim();
        if (!Common.matchePhone(phone)) {
            Toast.makeText(this, "手机格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(code)) {
            Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(pwd) || !pwd.matches("^[0-9a-zA-Z_\\*\\.\\?\\-\\+]{6,20}$")) {
            Toast.makeText(this, "密码格式不对,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(pwd, pwd2.getText().toString().trim())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectPosition == -1) {
            Toast.makeText(this, "请选择快递公司", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(smsId)) {
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> params = new HashMap<>();
        JSONObject json = new JSONObject();
        try {
            CompanyEntity ce = companyEntities.get(selectPosition);
            JSONObject json1 = this.json.getJSONArray("companylist").getJSONObject(selectedPosition);
            json.put("postmanStreet", json1.getString("id").toString());
            json.put("commpany_id", ce.getId());
            json.put("commpany_name", ce.getName());
            json.put("phone", phone);
            json.put("password", MD5.md5Pwd(pwd));
            json.put("phoneType", "Android");
            json.put("type", "Courier");
            json.put("token", ((MyApplication) getApplication()).getToken());
            JSONObject sms = new JSONObject();
            sms.put("id", smsId);
            sms.put("code", code);
            sms.put("phone", phone);
            params.put("userStr", json);
            params.put("smsStr", sms);
            newCall(Config.Url.getUrl(Config.REGISTER), params);
        } catch (JSONException e) {
            Toast.makeText(this, "解析数据失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        dialog.dismiss();
        super.fail(tag, code, json);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.COMPANY:
                JSONArray arr = json.getJSONArray("company");
                companyEntities = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < arr.length(); i++) {
                    companyEntities.add(gson.fromJson(arr.getString(i), CompanyEntity.class));
                }
                dialog.dismiss();
                break;
            case Config.SMS_CODE:
                this.smsId = json.getJSONObject("sms").getString("id");
                break;
            case Config.REGISTER:
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,AuthenticationInformationActivity.class));
                break;
//            case Config.POSTSTREET:
//
//                break;
        }
    }

    class DialogClickListener implements Dialog.DialogButtonClickListener<SelectCourier> {
        @Override
        public void done(SelectCourier dialog) {
            if (dialog.getSelectedItemPosition() >= 0) {
                selectPosition = dialog.getSelectedItemPosition();
                selectCour.setText(companyEntities.get(selectPosition).getName());
            }
        }

        @Override
        public void cancel(SelectCourier dialog) {
        }

        @Override
        public void close(SelectCourier dialog) {
        }
    }

    /**
     * 配送区域
     * <p>
     * 定位
     */
    private void getPosition() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2 * 60 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (this.mapLocation == null) this.mapLocation = amapLocation;
            if (mListener != null) mListener.onLocationChanged(amapLocation);
            ((MyApplication) getApplication()).setLocation(amapLocation);
            StringBuilder sb = new StringBuilder();
            sb.append(amapLocation.getProvince())
                    .append(" ")
                    .append(amapLocation.getCity())
                    .append(" ")
                    .append(amapLocation.getDistrict());
            address.setText(sb.toString());
            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("pro", amapLocation.getProvince())
                    .addFormDataPart("city", amapLocation.getCity())
                    .addFormDataPart("district", amapLocation.getDistrict()).build();
            Request request = new Request.Builder().url(Config.Url.getUrl(Config.POSTSTREET)).post(requestBody2).build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
                @Override
                public void onSuccess(String tag, JSONObject jsonObject) throws JSONException {
                    Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    json=jsonObject;
                    areas = jsonObject.getJSONArray("companylist");
                }
            });
            //停止定位
            deactivate();
        } else {
            System.out.println("定位失败");
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用 stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 选择快递公司
     */
    private void selectCourMethod() {
        dialog = new LoadDialog(this);
        dialog.show();
        try {
            JSONObject json = this.json.getJSONArray("companylist").getJSONObject(selectedPosition);
            Map<String, String> params = new HashMap<>();
            params.put("pro", mapLocation.getProvince());
            params.put("city", mapLocation.getCity());
            params.put("district", mapLocation.getDistrict());
            params.put("street",json.getString("street"));
            newCall(Config.Url.getUrl(Config.COMPANY), params);
        }catch (Exception e) {
        }
    }

    /**
     * 选择区域dialog窗口
     */
    private void selectProvinceDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View mDialog = inflater.inflate(R.layout.dialog_select_area, null);
        final android.app.Dialog areaDialog = new AlertDialog.Builder(this, R.style.DialogStyle).create();
        View delete = mDialog.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDialog.dismiss();
            }
        });
        final SelectProvinceAdapter adapter = new SelectProvinceAdapter(this, areas);
        ListView listView = (ListView) mDialog.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        areaDialog.show();
        areaDialog.setContentView(mDialog);
        if (selectedPosition != -1) listView.setItemChecked(selectedPosition, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaDialog.dismiss();
                try {
                    selectedPosition = position;
                    JSONObject addr = areas.getJSONObject(position);
                    district.setText(addr.getString("street").toString());
                } catch (JSONException e) {
                }
                selectCourMethod();
            }
        });
    }
}
