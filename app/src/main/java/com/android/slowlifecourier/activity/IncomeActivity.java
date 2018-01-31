package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class IncomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        AbsListView.OnScrollListener {
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.total_assets)
    TextView totalAssets;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.today_area_tv)
    TextView todayAreaTv;
    @BindView(R.id.today_out_area_tv)
    TextView todayOutAreaTv;
    @BindView(R.id.this_month_area_tv)
    TextView thisMonthAreaTv;
    @BindView(R.id.this_month_out_area_tv)
    TextView thisMonthOutAreaTv;

    private int page = 0;
    private int pageSize = 20;
    private Adapter adapter;
    private boolean loadding = false;
    private double balance;


    protected void init() {
        Info info = ((MyApplication) getApplication()).getInfo();
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        srl.setOnRefreshListener(this);
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
    }

    private void loadData() {
        if (!loadding) {
            loadding = true;
            Info info = ((MyApplication) getApplication()).getInfo();
            if (info == null) return;
            name.setText(info.getName());
            phone.setText(info.getPhone());
            Map<String, Object> map = new HashMap<>();
            //[userId, startPage, pageSize, today]
            map.put("userId", info.getId());
            map.put("startPage", ++page);
            map.put("pageSize", pageSize);
            map.put("today", "today");
            newCall(Config.Url.getUrl("slowlife/appuser/getuseraccount"), map);

            Map<String, Object> map1 = new HashMap<>();
            map1.put("userId", info.getId());
            newCall(Config.Url.getUrl(Config.Get_Statistics), map1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override

    @OnClick({R.id.back_rl, R.id.withdraw})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.footer_btn1://提现
                intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra(WithdrawActivity.BALANCE, Double.valueOf(balance));
                startActivity(intent);
                break;
            case R.id.footer_btn2://充值
                intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.footer_btn3://明细
                intent = new Intent(this, AccountDetailsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_income;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        if (tag.equals(Config.Get_Statistics)){
            if (todayAreaTv!=null)todayAreaTv.setText("今日同城："+json.getString("todayCityWide"));
            if (todayOutAreaTv!=null)todayOutAreaTv.setText("今日区外："+json.getString("todayIntercity"));
            if (thisMonthAreaTv!=null)thisMonthAreaTv.setText("本月同城:"+json.getString("monthCityWide"));
            if (thisMonthOutAreaTv!=null)thisMonthOutAreaTv.setText("本月区外:"+json.getString("monthIntercity"));
        }else {
            loadding = false;
            if (srl != null && srl.isRefreshing()) srl.setRefreshing(false);
            totalAssets.setText(json.getString("balance"));
            balance = json.getDouble("balance");
            JSONArray arr = json.getJSONObject("accountInfo").getJSONArray("aaData");
            if (page == 1) adapter.notifyDataSetChanged(arr);
            else adapter.addData(arr);
        }
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        super.fail(tag, code, json);
        if (srl != null && srl.isRefreshing()) srl.setRefreshing(false);
        loadding = false;
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount % pageSize == 0
                && totalItemCount >= pageSize
                && totalItemCount - visibleItemCount + firstVisibleItem <= 5) {
            loadData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    class Adapter extends BaseAdapter {
        JSONArray arr;

        public Adapter() {
        }

        public Adapter(JSONArray arr) {
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr == null ? 0 : arr.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return arr.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder vh;
            if (view == null) {
                view = LayoutInflater.from(IncomeActivity.this).inflate(R.layout.item_income, null, false);
                vh = new ViewHolder(view);
                view.setTag(vh);
            } else vh = (ViewHolder) view.getTag();
            try {
                JSONObject json = arr.getJSONObject(position);
                vh.explan.setText(json.getString("type_value"));
                vh.time.setText(json.getString("createDate"));
                vh.num.setText(json.getString("cost"));
                vh.state.setText(json.getString("status_value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }

        public void notifyDataSetChanged(JSONArray arr) {
            this.arr = arr;
            super.notifyDataSetChanged();
        }

        public void addData(JSONArray arr) {
            if (this.arr == null) {
                notifyDataSetChanged(arr);
                return;
            }
            for (int i = 0; i < arr.length(); i++)
                try {
                    this.arr.put(arr.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        @BindView(R.id.explan)
        TextView explan;
        @BindView(R.id.datetime)
        TextView time;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.state)
        TextView state;

        public ViewHolder(View v) {
            ButterKnife.bind(v);
            explan = (TextView) v.findViewById(R.id.explan);
            time = (TextView) v.findViewById(R.id.datetime);
            num = (TextView) v.findViewById(R.id.num);
            state = (TextView) v.findViewById(R.id.state);
        }
    }
}
