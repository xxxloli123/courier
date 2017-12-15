package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adressselectorlib.AddressSelector;
import com.android.slowlifecourier.adressselectorlib.CityInterface;
import com.android.slowlifecourier.adressselectorlib.OnItemClickListener;
import com.android.slowlifecourier.objectmodle.City;
import com.android.slowlifecourier.util.SimpleCallback;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SelectAreaActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.confirm_rl)
    RelativeLayout confirmRl;

    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private AddressSelector addressSelector;
    private JSONArray proArray, cityArray, districtArray;
    private JSONObject proObject, ctiyObject, districtObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        ButterKnife.bind(this);
        addressSelector = (AddressSelector) findViewById(R.id.address);
        addressSelector.setTabAmount(3);
        addressSelector.setTextEmptyColor(Color.parseColor("#333333"));
        addressSelector.setTextSelectedColor(R.color.blue);
        addressSelector.setListTextSelectedColor(R.color.colorPrimary);
        RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("pid", "")
                .addFormDataPart("cid", "").build();
        Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback(SelectAreaActivity.this) {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                proArray = json.getJSONArray("Pro");
                for (int i = 0; i < proArray.length(); i++) {
                    JSONObject jsonObject = proArray.getJSONObject(i);
                    City city = new City();
                    city.setId(jsonObject.getString("id"));
                    city.setName(jsonObject.getString("name"));
                    cities1.add(city);
                    addressSelector.setCities(cities1);
                    Log.d("MyBaseAdapter", "区域=" + cities1);
                }
            }
        });

        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(final AddressSelector addressSelector, CityInterface city, int tabPosition, int position) {
                switch (tabPosition) {
                    case 0:
                        try {
                            proObject = proArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", proObject.getString("id"))
                                    .addFormDataPart("cid", "").build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(SelectAreaActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    cityArray = json.getJSONArray("City");
                                    for (int i = 0; i < cityArray.length(); i++) {
                                        JSONObject jsonObject = cityArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities2.add(city);
                                        addressSelector.setCities(cities2);
                                        Log.d("MyBaseAdapter", "区域=" + cities1);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities2 = new ArrayList<>();
                        break;
                    case 1:
                        try {
                            ctiyObject = cityArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", "")
                                    .addFormDataPart("cid", ctiyObject.getString("id")).build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(SelectAreaActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    districtArray = json.getJSONArray("District");
                                    for (int i = 0; i < districtArray.length(); i++) {
                                        JSONObject jsonObject = districtArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities3.add(city);
                                        addressSelector.setCities(cities3);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities3 = new ArrayList<>();
                        break;
                    case 2:
                        try {
                            districtObject = districtArray.getJSONObject(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(SelectAreaActivity.this,"tabPosition ："+tabPosition+" "+city.getCityName(),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
    }

    private void sendInfo() {
//        //声明Intent对象
//        Intent intent = new Intent();
//        //设置传值跳转的对象
//        intent.setClass(SelectAreaActivity.this, OrderDetailsActivity.class);

//        intent.putExtra("selectedArea", bundle);
//        //启动跳转
//        startActivity(intent);
        Intent intent = new Intent();
        //声明bundle对象
        Bundle bundle = new Bundle();
        try {
            Map<String, Object> area=new HashMap<>();
            area.put("pro", proObject.getString("name"));
            area.put("city", ctiyObject.getString("name"));
            area.put("district", districtObject.getString("name"));
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl://返回
                finish();
                break;
            case R.id.confirm_rl:
                if (districtObject==null){
                    Toast.makeText(SelectAreaActivity.this,"请选择地区",Toast.LENGTH_SHORT).show();
                    return;
                }
                sendInfo();

                break;
        }

    }
}
