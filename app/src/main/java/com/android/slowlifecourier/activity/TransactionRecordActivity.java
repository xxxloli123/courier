package com.android.slowlifecourier.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.BaseActivity;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.CompleteOrderAdapter;
import com.android.slowlifecourier.util.CacheActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.android.slowlifecourier.R.id.complete_order;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class TransactionRecordActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.order_tag)
    TextView orderTag;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.order_name)
    TextView orderName;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.transaction_number)
    TextView transactionNumber;
    @BindView(R.id.cancel_time)
    TextView cancelTime;
    @BindView(R.id.cancel_reason)
    TextView cancelReason;
    @BindView(R.id.cancel_rl)
    LinearLayout cancelRl;
    @BindView(complete_order)
    LinearLayout completeOrder;
    @BindView(R.id.chargeback_time)
    TextView chargebackTime;
    @BindView(R.id.chargeback_reason)
    TextView chargebackReason;
    @BindView(R.id.chargeback_rl)
    LinearLayout chargebackRl;
    private Unbinder unbinder;
    private CompleteOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_transaction_record);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        String tag=getIntent().getStringExtra("tag");//接收传递过来的值，确定是什么单子
        List<String> list = new ArrayList<String>();
        list.add("用户评论");
        list.add("商家评论");
        adapter = new CompleteOrderAdapter(this, list);
        listview.setAdapter(adapter);
        getOrderTag(tag);
    }
    /**
     * 根据不同的订单确定不同的值
     * */
    private void getOrderTag(String tag){
        if("0".equals(tag)){//完成单
            completeOrder.setVisibility(View.VISIBLE);
            cancelRl.setVisibility(View.GONE);
            chargebackRl.setVisibility(View.GONE);
        }else if("1".equals(tag)){//异常单
            completeOrder.setVisibility(View.GONE);
            cancelRl.setVisibility(View.GONE);
            chargebackRl.setVisibility(View.VISIBLE);
        }else{//取消单
            completeOrder.setVisibility(View.GONE);
            cancelRl.setVisibility(View.VISIBLE);
            chargebackRl.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.back_rl)
    public void onClick() {
        finish();
    }
}
