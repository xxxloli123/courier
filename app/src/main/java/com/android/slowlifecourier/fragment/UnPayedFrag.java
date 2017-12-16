package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.OrderDetailsActivity;
import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.dialog.MessageDialog;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.view.TimeButton;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sgrape on 2017/5/29.
 * e-mail: sgrape1153@gmail.com
 */

@SuppressLint("ValidFragment")
public class UnPayedFrag extends FragOrderList {
    public UnPayedFrag() {
        super();
    }

    private long time = -1;
    private int deliveryCountdown = 60;


    public UnPayedFrag(String rob, String status) {
        super(rob, status);
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter();
        super.readInstanceState();
    }
    
    class Adapter extends OrderListAdapter {
        public Adapter() {
            super(getContext());
        }

        @Override
        protected void setData(View view, final OrderEntity order, int position) {
            super.setData(view, order, position);
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.grabSingle.setVisibility(View.GONE);
            if (TextUtils.equals(order.getPayType(), "Cash")) {
                holder.grabSingle.setText("确认收款");
                if (holder.afjoaiej.getVisibility() == View.VISIBLE)
                    holder.afjoaiej.setVisibility(View.GONE);
                holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog dialog = new MessageDialog(getContext());
                        dialog.show();
                        dialog.setListener(new MessageDialog.DialogButtonClickListener() {

                            @Override
                            public void done(Dialog dialog) {
                                dialog.dismiss();
                                Map<String, String> params = new HashMap<>();
                                params.put("orderId", order.getId());
                                params.put("cash", "cash");
                                newCall(Config.Url.getUrl(Config.CONFIRMRECEIVABLES), params, order);
                            }

                            @Override
                            public void cancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                holder.alreadyDelivery.setVisibility(View.VISIBLE);
                holder.alreadyDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> params = new HashMap<>();
                        params.put("orderId", order.getId());
                        newCall(Config.Url.getUrl(Config.ALREADYDELIVERY), params);
                        Toast.makeText(getActivity(), "已提醒用户物件已送达\n如需再次提醒\n请一分钟后再操作", Toast.LENGTH_LONG).show();
                        holder.alreadyDelivery.setClickable(false);
                    }
                });
            }

            /**
             * 未支付状态（现金都没有）
             * */
            else {
                if (TextUtils.equals(order.getStatus(), "UnPayed")) {
                    holder.afjoaiej.setVisibility(View.VISIBLE);
                    holder.afjoaiej.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MessageDialog dialog = new MessageDialog(getContext());
                            dialog.show();
                            dialog.setListener(new MessageDialog.DialogButtonClickListener() {

                                @Override
                                public void done(Dialog dialog) {
                                    dialog.dismiss();
                                    Map<String, String> params = new HashMap<>();
                                    params.put("orderId", order.getId());
                                    params.put("cash", "cash");
                                    newCall(Config.Url.getUrl(Config.CONFIRMRECEIVABLES), params, order);
                                }

                                @Override
                                public void cancel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
                holder.grabSingle.setText("提醒付款");
                holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("orderId", order.getId());
                        newCall(Config.Url.getUrl(Config.REMINDUSERPAYMENT), params);
                    }
                });
                holder.alreadyDelivery.setVisibility(View.VISIBLE);
                holder.alreadyDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> params = new HashMap<>();
                        params.put("orderId", order.getId());
                        newCall(Config.Url.getUrl(Config.ALREADYDELIVERY), params);
                        Toast.makeText(getActivity(), "已提醒用户物件已送达\n如需再次提醒\n请一分钟后再操作", Toast.LENGTH_LONG).show();
                        holder.alreadyDelivery.setClickable(false);
                    }
                });
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
//        Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
        switch (tag.toString()){
            case Config.ALREADYDELIVERY:
        }
        super.onSuccess(tag, json);
        if (tag != null && tag instanceof OrderEntity) {
            adapter.getData().remove(tag);
            adapter.notifyDataSetChanged();
        }
    }


}