package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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


/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class AccountDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        AbsListView.OnScrollListener{
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.listview)
    ListView listview;
    Info info;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private int page = 0;
    private int pageSize = 20;
    private Adapter adapter;
    private boolean loadding = false;


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    @Override
    protected void init() {
        super.init();
        info = ((MyApplication) getApplication()).getInfo();
        adapter = new Adapter();
        listview.setAdapter(adapter);
        listview.setOnScrollListener(this);
        srl.setOnRefreshListener(this);
        if (info == null) startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (info == null) info = ((MyApplication) getApplication()).getInfo();
        loadData();
    }

    private void loadData() {
        if (!loadding) {
            loadding = true;
            Info info = ((MyApplication) getApplication()).getInfo();
            if (info == null) return;
            Map<String, Object> map = new HashMap<>();
            //[userId, startPage, pageSize, today]
            map.put("userId", info.getId());
            map.put("startPage", ++page);
            map.put("pageSize", pageSize);
            newCall(Config.Url.getUrl("slowlife/appuser/getuseraccount"), map);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_details;
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        loadding = false;
        if (srl != null && srl.isRefreshing())
            srl.setRefreshing(false);
        JSONArray arr = json.getJSONObject("accountInfo").getJSONArray("aaData");
        if (page == 1) adapter.notifyDataSetChanged(arr);
        else adapter.addData(arr);
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        super.fail(tag, code, json);
        if (srl != null && srl.isRefreshing())
            srl.setRefreshing(false);
        loadding = false;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount % pageSize == 0
                && totalItemCount - visibleItemCount + firstVisibleItem <= 5) {
            loadData();
        }
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
                view = LayoutInflater.from(AccountDetailsActivity.this).inflate(R.layout.item_income, null, false);
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
