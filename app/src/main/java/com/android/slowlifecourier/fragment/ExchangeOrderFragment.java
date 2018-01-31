package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxxloli on 2018/1/11.
 */

@SuppressLint("ValidFragment")
public class ExchangeOrderFragment extends FragOrderList {

    public ExchangeOrderFragment() {
        super();
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter();
        super.readInstanceState();
    }

    public ExchangeOrderFragment(String rob, String status) {
        super(rob, status);
    }

    class Adapter extends OrderListAdapter {
        public Adapter() {
            super(getContext());
        }

        @Override
        protected void setData(View view, final OrderEntity order, int position) {
            super.setData(view, order, position);
            super.setData(view, order, position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.grabSingle.setText("确认");
            holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptOrRefuse(order,true);
                }
            });
            holder.alreadyDelivery.setVisibility(View.VISIBLE);
            holder.alreadyDelivery.setText("取消");
            holder.alreadyDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptOrRefuse(order,false);
                }
            });
        }

        @Override
        protected VH newViewHolder(View v) {
            return new ExchangeOrderFragment.VH(v);
        }
    }

    private void acceptOrRefuse(OrderEntity order, boolean b) {
//        拒绝换单 type[yes 同意/no 不同意]
//        参数：[orderId, userId, type]
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", order.getId());
        params.put("userId", info.getId());
        params.put("type", b?"yes":"no");
        newCall(Config.Url.getUrl(Config.HandleExchangeOrder), params);
        adapter.getData().remove(order);
    }

    class VH extends OrderListAdapter.ViewHolder {
        VH(View view) {
            super(view);
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        super.onSuccess(tag, json);
        if (tag.toString().equals(Config.HandleExchangeOrder)){
            ToastUtil.showToast(getContext(),json.getString("message"));
            adapter.notifyDataSetChanged(adapter.getData());
        }
    }
}
