package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;

import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

@SuppressLint("ValidFragment")
public class ServedFragment1 extends FragOrderList implements AdapterView.OnItemClickListener{
    public ServedFragment1() {
        super();
    }

    public ServedFragment1(String rob, String status) {
        super(rob, status);
    }

    private void submit(OrderEntity orderEntity) {
//        orderId:订单id;status=Complete
        ToastUtil.showToast(getActivity(),"点击了送达");
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderEntity.getId());
        map.put("status", "Complete");
        newCall(Config.Url.getUrl(Config.ORDERCOMPLETE), map, orderEntity);
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter();
        super.readInstanceState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        submit(adapter.getItem(position));
    }

    class Adapter extends OrderListAdapter {

        public Adapter() {
            super(getContext());
        }
        @Override
        protected void setData(final View view, final OrderEntity order, final int position) {
            super.setData(view, order, position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.SpareRire.setVisibility(View.GONE);
            holder.grabSingle.setText("确认送达");
            holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(order);
                }
            });
            holder.spareTireBt.setVisibility(View.VISIBLE);
            holder.orderTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.spareTireBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(order);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json1) throws JSONException {
        super.onSuccess(tag, json1);
//        Toast.makeText(getContext(), json1.getString("message"), Toast.LENGTH_SHORT).show();
        if (tag==null)return;
        if (tag instanceof OrderEntity){
            adapter.getData().remove(tag);
            adapter.notifyDataSetChanged();
        }
        if (tag.toString().equals(Config.ORDERCOMPLETE)) {
            JSONObject json = new JSONObject();
            if (aMapLocation != null) try {
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