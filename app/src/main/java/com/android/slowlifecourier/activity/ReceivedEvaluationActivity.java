package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.ReceivedEvaluationAdapter;
import com.android.slowlifecourier.adapter.ReceivedEvaluationAdapter1;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.CourierEvaluateEntity;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.util.SimpleCallback;
import com.android.slowlifecourier.view.CustomRadioGroup;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class ReceivedEvaluationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
//    @BindView(R.id.customRadioGroup)
//    CustomRadioGroup customRadioGroup;
    @BindView(R.id.listview)
    ListView listview;
//    @BindView(R.id.no_evaluation)
//    LinearLayout noEvaluation;
    private Unbinder unbinder;
//    private String[] labStr = {"全部", "包装精美", "味道好", "干净卫生", "份量足", "服务不错", "物美价廉", "送货快", "食材新鲜", "不好吃"};
    private ReceivedEvaluationAdapter adapter;
    private List<CourierEvaluateEntity.EvaluateInfoBean.AaDataBean> evaluateData=new ArrayList<>();
    protected Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_received_evaluation);
        unbinder = ButterKnife.bind(this);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        info = ((MyApplication) ReceivedEvaluationActivity.this.getApplicationContext()).getInfo();
        RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type","express")
                    .addFormDataPart("typeId",info.getId())
                    .addFormDataPart("startPage","1")
                    .addFormDataPart("pageSize","50")
                    .build();
            Request request = new Request
                    .Builder().post(requestBody)
                    .url(Config.Url.getUrl(Config.EVALUATE))
                    .build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(this) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    Toast.makeText(ReceivedEvaluationActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    //得到实体类对象
                    CourierEvaluateEntity dto=gson.fromJson(json.toString(),CourierEvaluateEntity.class);
                    CourierEvaluateEntity.EvaluateInfoBean evaluateInfoBean=dto.getEvaluateInfo();
                    for (CourierEvaluateEntity.EvaluateInfoBean.AaDataBean aaDataBean:evaluateInfoBean.getAaData() ){
                        evaluateData.add(aaDataBean);
                        System.out.println("评价"+evaluateData);
                    }
                }
            });
//        List<String> list=new ArrayList<String>();
//        for (int i=0;i<20;i++){
//            list.add("商品还是不错的我很满意向往下次海恩那个和错,商品还是不错的我很满意向往下次海恩那个和错");
//        }
        adapter=new ReceivedEvaluationAdapter(this,evaluateData);
//        ReceivedEvaluationAdapter1 adapter1=new ReceivedEvaluationAdapter1(this,list);
        listview.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @OnClick({R.id.back_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }
}
