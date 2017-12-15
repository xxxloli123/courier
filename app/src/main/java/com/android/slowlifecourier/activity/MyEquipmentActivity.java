package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.MyEquipmentDromeanuAdapter;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.view.CustomRadioGroup;
import com.android.slowlifecourier.view.DropDownMenu;
import com.android.slowlifecourier.view.MyProgressDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class MyEquipmentActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.existing_equipment)
    TextView existingEquipment;
    @BindView(R.id.customRadioGroup)
    CustomRadioGroup customRadioGroup;
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.reason_application)
    EditText reasonApplication;
    @BindView(R.id.submit_bt)
    Button submitBt;
    private Unbinder unbinder;
    private String[] labStr = {"保温箱", "电子秤"};
    private String equipment[] = {"雨衣", "电话", "衣服", "保温箱", "电子秤", "快递单"};
    private String headers[] = {"请选择"};
    private MyEquipmentDromeanuAdapter adapter;
    private List<View> popupViews = new ArrayList<>();
    private MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_my_equipment);
        unbinder = ButterKnife.bind(this);
        initView();
    }
    /**
     * 初始化控件
     * */
    private void initView(){
        //已有装备
        for (int i = 0; i < labStr.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.item_my_equipment_rb, null);
            radioButton.setText(labStr[i] + "(1)");
            customRadioGroup.addView(radioButton);
        }
        //显示下拉菜单
        displayDropDownMenu();
    }
    /**
     * 显示下拉菜单
     * */
    private void displayDropDownMenu(){
        final ListView evaluationView = new ListView(this);
        evaluationView.setDividerHeight(0);
        adapter=new MyEquipmentDromeanuAdapter(this, Arrays.asList(equipment));
        evaluationView.setAdapter(adapter);
        popupViews.add(evaluationView);
        evaluationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckItem(position);
                dropDownMenu.setTabText(equipment[position]);
                dropDownMenu.closeMenu();
            }
        });
        TextView contentView = new TextView(this);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.back_rl, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.submit_bt:
                progressDialog = new MyProgressDialog(this, "提交申请");
                progressDialog.show();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
