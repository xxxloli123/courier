package com.android.slowlifecourier.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.MessageCentreAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.MessgeEntity;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class MessageCentreActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private MessageCentreAdapter adapter;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    private int page;
    private final String pageSize = "20";
    private Info info;
    private List<MessgeEntity> msgs;

    @Override
    protected void init() {
        super.init();
        adapter = new MessageCentreAdapter(this, null);
        listView.setAdapter(adapter);
        srl.setOnRefreshListener(this);
        info = ((MyApplication) getApplication()).getInfo();
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        if (info == null) {
            finish();
            return;
        }
        onRefresh();
    }

    @OnClick({R.id.back_rl})
    public void onClick() {
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_centre;
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", info.getId());
        map.put("pageNum", String.valueOf(++page));
        map.put("numPerPage", pageSize);
        newCall(Config.Url.getUrl(Config.MESSAGE), map);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount + 5 > totalItemCount
                && totalItemCount % 20 == 0 && totalItemCount > 0) {
            loadData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("orderId", msgs.get(position).getOrderId());
        startActivity(intent);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        JSONArray arr = json.getJSONArray("listMessage");
        if (msgs == null) msgs = new ArrayList<>();
        if (page == 1) msgs.clear();
        Gson gson = new Gson();
        for (int i = 0; i < arr.length(); i++) {
            msgs.add(gson.fromJson(arr.getString(i), MessgeEntity.class));
        }
        adapter.notifyDataSetChanged(msgs);
        if (srl != null && srl.isRefreshing())
            srl.setRefreshing(false);
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        super.fail(tag, code, json);
        if (srl != null && srl.isRefreshing())
            srl.setRefreshing(false);
    }
}
