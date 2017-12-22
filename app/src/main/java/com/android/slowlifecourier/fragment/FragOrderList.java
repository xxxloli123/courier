package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.greendao.DBManagerOrder;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by sgrape on 2017/5/25.
 * e-mail: sgrape1153@gmail.com
 */
@SuppressLint("ValidFragment")
public class FragOrderList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private String type;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.no_order)
    LinearLayout noOrder;
    @BindView(com.sgrape.library.R.id.refresh)
    SwipeRefreshLayout srl;

    protected Info info;
    private int page = 0;
    protected String rob;
    protected OrderListAdapter adapter;
    protected String status;
    protected boolean enable;
    private DBManagerOrder dbManagerOrder;
    protected AMapLocation aMapLocation;

    public FragOrderList() {
    }

    public FragOrderList(String rob, String status) {
        this(rob, status, null);
    }

    public FragOrderList(String rob, String status, String type) {
        this(rob, status, type, true);
    }

    public FragOrderList(String rob, String status, String type, boolean enable) {
        super();
        this.rob = rob;
        this.status = status;
        this.type = type;
        this.enable = enable;
        getArguments().putString("rob", this.rob);
        getArguments().putString("status", this.status);
        getArguments().putString("type", type);
        getArguments().putBoolean("enable", enable);
    }

    @Override
    protected void init() {
        if (listview.getAdapter() == null)
            listview.setAdapter(adapter);
        if (listview.getCount() > 0) {
            noOrder.setVisibility(View.GONE);
        } else {
            noOrder.setVisibility(View.VISIBLE);
        }
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
        aMapLocation=((MyApplication) getContext().getApplicationContext()).getLocation();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView != null && isVisibleToUser) onRefresh();
    }

    @Override
    protected void initListener() {
        if (status==null||!status.equals("UnPayed1"))listview.setOnScrollListener(new OnScrollListener());
        srl.setOnRefreshListener(this);
    }

    @Override
    protected void loadData() {
        if (status!=null&&status.equals("UnPayed1")){
            dbManagerOrder=new DBManagerOrder(getActivity());
            List<OrderEntity> orders = adapter.getData();
            if (orders == null) {
                orders=new ArrayList<>();
                orders.addAll(dbManagerOrder.queryList());
            }
            firstLoad = false;
            if (!orders.isEmpty()){
                adapter.notifyDataSetChanged(orders);
                noOrder.setVisibility(View.GONE);
            }else noOrder.setVisibility(View.VISIBLE);
            if (srl!=null)srl.setRefreshing(false);
        }else
            synchronized (info) {
            if (!firstLoad && srl != null && !srl.isRefreshing()) return;
            firstLoad = false;
            Map<String, Object> map = new HashMap<>();
            map.put("userId", info.getId());
            map.put("pageNum", ++page);
            map.put("numPerPage", 20);
            if (rob != null) map.put("rob", rob);
            else map.put("status", status);
            if (!isEmpty(type)) map.put("type", type);
            newCall(Config.Url.getUrl(Config.ROBORDER), map);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_waiting_list;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        super.fail(tag, code, json);
        if (srl != null) srl.setRefreshing(false);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.ROBORDER:
                List<OrderEntity> orders = adapter.getData();
                if (orders == null) orders = new ArrayList<>();
                if (page == 1 && !orders.isEmpty()) orders.clear();
                JSONArray arr = json.getJSONObject("ordersInfo").getJSONArray("aaData");
                Log.e("order","丢了个雷姆"+arr);
                Gson gson = new Gson();
                for (int i = 0; i < arr.length(); i++) {
                    final JSONObject cache = arr.getJSONObject(i);
                    orders.add(orders.size(), gson.fromJson(cache.toString(), OrderEntity.class));
                }
                adapter.notifyDataSetChanged(orders);
                if (noOrder!=null) {
                    if (orders.isEmpty()) noOrder.setVisibility(View.VISIBLE);
                    else noOrder.setVisibility(View.GONE);
                }
                if (srl!=null)srl.setRefreshing(false);
                firstLoad = false;
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        if (srl != null && !srl.isRefreshing())
            srl.setRefreshing(true);
        loadData();
    }

    @Override
    protected void readInstanceState() {
        super.readInstanceState();
        rob = getArguments().getString("rob");
        status = getArguments().getString("status");
        ArrayList<OrderEntity> orders = getArguments().getParcelableArrayList("orderlist");
        if (adapter == null) {
            adapter = new OrderListAdapter(getContext());
            adapter.setEnable(enable);
        }
        if (orders != null || adapter.getCount() == 0) adapter.notifyDataSetChanged(orders);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        getArguments().putString("rob", this.rob);
        getArguments().putString("status", this.status);
        bundle.putParcelableArrayList("orderlist", (ArrayList<OrderEntity>) adapter.getData());
    }

    class OnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount > listview.getAdapter().getCount() - 5
                    && listview.getAdapter().getCount() % 20 == 0
                    && listview.getAdapter().getCount() != 0) {
                loadData();
            }
        }
    }
}
