package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/27 0027.
 * TODO  抢单
 */

@SuppressLint("ValidFragment")
public class WaitingListFragment extends FragOrderList implements SwipeRefreshLayout.OnRefreshListener {

    private List<Integer> positions = null;

    public WaitingListFragment() {
        super();
    }

    public WaitingListFragment(String rob, String status) {
        super(rob, status);
    }


    private void submit(OrderEntity orderEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderEntity.getId());
        map.put("userId", info.getId());
        map.put("address", aMapLocation.getAddress());
        newCall(Config.Url.getUrl(Config.BATTLE), map, orderEntity);

    }

    class Adapter extends OrderListAdapter {

        public Adapter() {
            super(getContext());
        }

        @Override
        protected void setData(View view, final OrderEntity order, final int position) {
            super.setData(view, order, position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(order);
                    if (!positions.contains(position))
                        positions.add(position);
                }
            });
        }

        @Override
        protected VH newViewHolder(View v) {
            return new WaitingListFragment.VH(v);
        }
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter();
        positions = new ArrayList<>();
        super.readInstanceState();
    }

    class VH extends OrderListAdapter.ViewHolder {

        VH(View view) {
            super(view);
            if (grabSingle.getVisibility() != View.VISIBLE) grabSingle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json1) throws JSONException {
        super.onSuccess(tag, json1);
        if (tag instanceof OrderEntity) {
            // 抢单
            Toast.makeText(getContext(), json1.getString("message"), Toast.LENGTH_SHORT).show();
            adapter.getData().remove(tag);
            adapter.notifyDataSetChanged();
            if (adapter.getCount() == 0) {
                noOrder.setVisibility(View.VISIBLE);
            }
            JSONObject json = new JSONObject();
            try {
                json.put("id", info.getId());
                json.put("lat", aMapLocation.getLatitude());
                json.put("lng", aMapLocation.getLongitude());
                Map<String, Object> params = new HashMap<>();
                params.put("userStr", json);
                newCall(Config.Url.getUrl("slowlife/appuser/userupdatelatlng"), params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
