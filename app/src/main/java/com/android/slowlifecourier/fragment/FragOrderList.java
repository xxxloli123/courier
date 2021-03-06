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
import java.util.Calendar;
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
    private boolean isExchange=false;

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
        aMapLocation=((MyApplication) getContext().getApplicationContext()).getLocation();
        if (listview.getAdapter() == null)
            listview.setAdapter(adapter);
        if (listview.getCount() > 0) {
            noOrder.setVisibility(View.GONE);
        } else {
            noOrder.setVisibility(View.VISIBLE);
        }
        info = ((MyApplication) getContext().getApplicationContext()).getInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView != null && isVisibleToUser) onRefresh();
    }

    @Override
    protected void initListener() {
        if (status==null||!status.equals("ExchangeOrderFragment"))
            listview.setOnScrollListener(new OnScrollListener());
        srl.setOnRefreshListener(this);
    }

    @Override
    protected void loadData() {
//        if (status!=null&&status.equals("ExchangeOrderFragment")){
//            dbManagerOrder=new DBManagerOrder(getActivity());
//            List<OrderEntity> orders=new ArrayList<>();
//            orders.addAll(dbManagerOrder.queryList());
//            firstLoad = false;
//            if (!orders.isEmpty()){
//                adapter.notifyDataSetChanged(orders);
//                if (noOrder!=null)noOrder.setVisibility(View.GONE);
//            }else if (noOrder!=null)noOrder.setVisibility(View.VISIBLE);
//            if (srl!=null)srl.setRefreshing(false);
//        }else
            synchronized (info) {
            if (!firstLoad && srl != null && !srl.isRefreshing()) return;
            firstLoad = false;
            Map<String, Object> map = new HashMap<>();
            map.put("userId", info.getId());
            map.put("pageNum", ++page);
            map.put("numPerPage", 100);
            if (rob != null) map.put("rob", rob);
            else{
                switch (status) {
                    case "UnPayed1":
                        map.put("startDate", getTime());
                        map.put("status", "UnPayed");
                        break;
                    case "ExchangeOrderFragment":
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", info.getId());
                        newCall(Config.Url.getUrl(Config.GetExchangeOrder), params);
                        isExchange=true;
                        break;
                    default:
                        map.put("status", status);
                        break;
                }
            }
            if (isExchange){
                isExchange=false;
                return;
            }
            if (!isEmpty(type)) map.put("type", type);
            newCall(Config.Url.getUrl(Config.ROBORDER), map);
        }
    }

    private String getTime() {
        Calendar cal=Calendar.getInstance();
        return (cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH) + 1)+"-"+cal.get(Calendar.DAY_OF_MONTH));
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
            case Config.GetExchangeOrder:
                List<OrderEntity> orders = adapter.getData();
                if (orders == null) orders = new ArrayList<>();
                if (page == 1 && !orders.isEmpty()) orders.clear();
                JSONArray arr=new JSONArray();
                if (tag.toString().equals(Config.GetExchangeOrder)){
//                    Log.e("GetExchangeOrder",""+json);
                    //数据结构 json.getJSONObject("ordersInfo").getJSONArray("aaData") 不正确  不执行下面代码
                    arr = json.getJSONArray("listorder");
                }else {
                    arr = json.getJSONObject("ordersInfo").getJSONArray("aaData");
                }
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
                    //                          Count 计数 % 除法中的余
                    && listview.getAdapter().getCount() % 20 == 0
                    && listview.getAdapter().getCount() != 0) {
                loadData();
            }
        }
    }
}
