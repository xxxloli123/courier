package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.util.LogoConfig;
import com.sgrape.adapter.BaseAdapter;
import com.slowlife.map.util.AMapUtil;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class OrderListAdapter extends BaseAdapter<OrderEntity> {

    public OrderListAdapter(Context context) {
        super(context, new ArrayList<OrderEntity>());
    }

    private boolean enable = true;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    protected void setData(final View view, final OrderEntity order, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.takeAddress.setText(String.format("%s%s%s%s%s",
                order.getStartPro(), order.getStartCity(), order.getStartDistrict(),
                order.getStartStreet(), order.getStartHouseNumber()));

        if ((order.getStatus().equals("UnReceivedOrder")
                || order.getStatus().equals("ReceivedOrder"))
                && order.getType().equals("Intercity")) {
            if (order.getEndPro() == null || order.getEndCity() == null) {
                holder.giveAddress.setText("");
            } else {
                holder.giveAddress.setText(order.getEndPro() + "  " + order.getEndCity() + "  " + order.getEndDistrict());
            }
        } else {
            holder.giveAddress.setText(String.format("%s%s%s%s%s",
                    order.getEndPro(), order.getEndCity(), order.getEndDistrict(),
                    order.getEndStreet(), order.getEndHouseNumber()).replace("null", ""));
        }
        if (order.getType().equals("Intercity")) {               // 区外
            if (holder.localtion.getVisibility() == View.VISIBLE)
                holder.localtion.setVisibility(View.INVISIBLE);
            holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
            if (holder.ordernumlayout.getVisibility() != View.VISIBLE)
                holder.ordernumlayout.setVisibility(View.VISIBLE);
            holder.orderNum.setText(order.getOrderNumber());
            if (holder.phoneLayout1.getVisibility() == View.VISIBLE)
                holder.phoneLayout1.setVisibility(View.VISIBLE);
            if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                holder.phoneLayout.setVisibility(View.VISIBLE);
            holder.phone.setText(order.getReceiverPhone());
            switch (order.getStatus()) {
                case "UnReceivedOrder": {                       // 待抢单
                    if (holder.layout.getVisibility() == View.VISIBLE)
                        holder.layout.setVisibility(View.GONE);
                    holder.phoneLayout1.setVisibility(View.GONE);
                    break;
                }
                case "ReceivedOrder": {                         // 待取货
                    if (holder.layout.getVisibility() == View.VISIBLE)
                        holder.layout.setVisibility(View.GONE);
                    if (holder.grabSingle.getVisibility() != View.VISIBLE)
                        holder.grabSingle.setVisibility(View.VISIBLE);
                    holder.grabSingle.setText("取货");
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    holder.phoneLayout1.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(order.getStartLat() + "")
                            || TextUtils.isEmpty(order.getStartLng() + "")
                            || TextUtils.equals("0", order.getStartLat() + "")
                            || TextUtils.equals("0.0", order.getStartLat() + "")
                            || TextUtils.equals("0", order.getStartLng() + "")
                            || TextUtils.equals("0.0", order.getStartLng() + "")) {
                        if (holder.localtion.getVisibility() == View.VISIBLE)
                            holder.localtion.setVisibility(View.INVISIBLE);
                    } else {
                        if (holder.localtion.getVisibility() != View.VISIBLE)
                            holder.localtion.setVisibility(View.VISIBLE);
                        System.out.println(order.getStartLat() + "定位" + order.getStartLng());
                        holder.localtion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                                    AMapUtil.goToNaviActivity(v.getContext(), v.getContext().getApplicationInfo().name,
                                            null, String.valueOf(order.getStartLat()), String.valueOf(order.getStartLng()), "0", "2");
                                } else {
                                    Toast.makeText(v.getContext(), "未安装高德地图无法为您导航", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    break;
                }
                case "UnPayed": {                               // 待付款
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    if (holder.layout.getVisibility() != View.VISIBLE)
                        holder.layout.setVisibility(View.VISIBLE);
                    if (holder.alreadyDelivery.getVisibility() != View.VISIBLE)
                        holder.alreadyDelivery.setVisibility(View.VISIBLE);
                    holder.price.setText(order.getUserActualFee() + "");
                    holder.phoneLayout.setVisibility(View.VISIBLE);
                    holder.phoneLayout1.setVisibility(View.VISIBLE);
                    holder.phone.setText(order.getCreateUserPhone());
                    holder.phone1.setText(order.getReceiverPhone());
                    break;
                }
                case "GoodsDelivery": {                         // 待送达
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    if (holder.layout.getVisibility() != View.VISIBLE)
                        holder.layout.setVisibility(View.VISIBLE);
                    holder.phoneLayout.setVisibility(View.VISIBLE);
                    holder.phoneLayout1.setVisibility(View.VISIBLE);
                    holder.price.setText(order.getUserActualFee() + "");
                    holder.phone.setText(order.getCreateUserPhone());
                    holder.phone1.setText(order.getReceiverPhone());
                    break;
                }
            }

        } else {                                                // 同城
            if (holder.ordernumlayout.getVisibility() == View.VISIBLE)
                holder.ordernumlayout.setVisibility(View.GONE);
            switch (order.getStatus()) {
                case "UnReceivedOrder": {                       // 待抢单
                    if (holder.layout.getVisibility() == View.VISIBLE)
                        holder.layout.setVisibility(View.GONE);
                    if (holder.localtion.getVisibility() == View.VISIBLE)
                        holder.localtion.setVisibility(View.INVISIBLE);
                    if (holder.phoneLayout1.getVisibility() == View.VISIBLE)
                        holder.phoneLayout1.setVisibility(View.GONE);
                    if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                        holder.phoneLayout.setVisibility(View.VISIBLE);
                    holder.phone.setText(order.getCreateUserPhone());
                    break;
                }
                case "ReceivedOrder": {                         // 待取货
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    if (holder.layout.getVisibility() == View.VISIBLE)
                        holder.layout.setVisibility(View.GONE);
                    if (holder.phoneLayout1.getVisibility() == View.VISIBLE)
                        holder.phoneLayout1.setVisibility(View.GONE);
                    if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                        holder.phoneLayout.setVisibility(View.VISIBLE);
                    if (holder.grabSingle.getVisibility() != View.VISIBLE)
                        holder.grabSingle.setVisibility(View.VISIBLE);
                    if (holder.localtion.getVisibility() != View.VISIBLE)
                        holder.localtion.setVisibility(View.VISIBLE);
                    holder.grabSingle.setText("取货");
                    holder.phone.setText(order.getCreateUserPhone());
                    holder.localtion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                                AMapUtil.goToNaviActivity(v.getContext(), v.getContext().getApplicationInfo().name,
                                        null, order.getStartLat() + "", order.getStartLng() + "", "0", "2");
                            } else {
                                Toast.makeText(v.getContext(), "未安装高德地图无法为您导航", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                }
                case "UnPayed": {                               // 待付款
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    if (holder.layout.getVisibility() != View.VISIBLE)
                        holder.layout.setVisibility(View.VISIBLE);
                    if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                        holder.phoneLayout.setVisibility(View.VISIBLE);
                    if (holder.phoneLayout1.getVisibility() != View.VISIBLE)
                        holder.phoneLayout1.setVisibility(View.VISIBLE);
                    if (holder.alreadyDelivery.getVisibility() != View.VISIBLE)
                        holder.alreadyDelivery.setVisibility(View.VISIBLE);
                    holder.phone1.setText(order.getReceiverPhone());
                    holder.phone.setText(order.getCreateUserPhone());
                    holder.price.setText(order.getUserActualFee() + "");
                    if (holder.localtion.getVisibility() != View.VISIBLE)
                        holder.localtion.setVisibility(View.VISIBLE);
                    holder.localtion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                                AMapUtil.goToNaviActivity(v.getContext(), v.getContext().getApplicationInfo().name,
                                        null, order.getEndLat() + "", order.getEndLng() + "", "0", "2");
                            } else {
                                Toast.makeText(v.getContext(), "未安装高德地图无法为您导航", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                }
                case "GoodsDelivery": {                         // 待送达
                    holder.lan.setText(order.getStartLng() + "," + order.getStartLat());
                    if (holder.layout.getVisibility() != View.VISIBLE)
                        holder.layout.setVisibility(View.VISIBLE);
                    if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                        holder.phoneLayout.setVisibility(View.VISIBLE);
                    if (holder.phoneLayout1.getVisibility() != View.VISIBLE)
                        holder.phoneLayout1.setVisibility(View.VISIBLE);
                    holder.phone1.setText(order.getReceiverPhone());
                    holder.price.setText(order.getUserActualFee() + "");
                    if (holder.localtion.getVisibility() != View.VISIBLE)
                        holder.localtion.setVisibility(View.VISIBLE);
                    holder.localtion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                                AMapUtil.goToNaviActivity(v.getContext(), v.getContext().getApplicationInfo().name,
                                        null, order.getEndLat() + "", order.getEndLng() + "", "0", "2");
                            } else {
                                Toast.makeText(v.getContext(), "未安装高德地图无法为您导航", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                }
            }
        }
        if (TextUtils.equals("Complete", order.getStatus())) {
            if (holder.localtion.getVisibility() == View.VISIBLE)
                holder.localtion.setVisibility(View.INVISIBLE);
            if (holder.layout.getVisibility() != View.VISIBLE)
                holder.layout.setVisibility(View.VISIBLE);
            if (holder.phoneLayout.getVisibility() != View.VISIBLE)
                holder.phoneLayout.setVisibility(View.VISIBLE);
            if (holder.phoneLayout1.getVisibility() != View.VISIBLE)
                holder.phoneLayout1.setVisibility(View.VISIBLE);
            holder.phone1.setText(order.getReceiverPhone());
            holder.price.setText(order.getUserActualFee() + "");
        }
        holder.phone.setText(order.getCreateUserPhone());
        holder.paymentAmount.setText(order.getPayStatus_value());
        holder.orderTime.setText("客户下单时间:"
                + order.getCreateDate().substring(order.getCreateDate().lastIndexOf(" ")));
        holder.urgentOrder.setVisibility(TextUtils.equals(order.getUrgent(), "no") ? View.GONE : View.VISIBLE);
        holder.logo.setImageResource(LogoConfig.getLogoRes(order.getUserChoiceCommpanyCode()));
        holder.commpanyName.setText(order.getUserChoiceCommpanyName());
        if (!enable) view.findViewById(R.id.fjaoeijfaoiejf).setVisibility(View.GONE);
    }

    @Override
    protected VH newViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_waiting_list;
    }

    public static class ViewHolder extends VH {
        @BindView(R.id.price)
        public TextView price;
        @BindView(R.id.layout)
        public LinearLayout layout;
        @BindView(R.id.take_address)
        public TextView takeAddress;
        @BindView(R.id.give_address)
        public TextView giveAddress;
        @BindView(R.id.payment_amount)
        public TextView paymentAmount;
        @BindView(R.id.order_time)
        public TextView orderTime;
        @BindView(R.id.urgent_order)
        public TextView urgentOrder;
        @BindView(R.id.grab_single)
        public TextView grabSingle;
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.localtion)
        View localtion;
        @BindView(R.id.phone_info)
        View phoneLayout;
        @BindView(R.id.phone_title)
        TextView phonetitle;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.phone_info1)
        View phoneLayout1;
        @BindView(R.id.phone_title1)
        TextView phonetitle1;
        @BindView(R.id.phone1)
        TextView phone1;
        @BindView(R.id.order_num_layout)
        View ordernumlayout;
        @BindView(R.id.order_num)
        TextView orderNum;
        @BindView(R.id.afjoaiej)
        public View afjoaiej;
        @BindView(R.id.commpanyName)
        TextView commpanyName;
        @BindView(R.id.already_delivery)
        public TextView alreadyDelivery;
        @BindView(R.id.lan)
        public TextView lan;


        public ViewHolder(View view) {
            super(view);
        }
    }


}
