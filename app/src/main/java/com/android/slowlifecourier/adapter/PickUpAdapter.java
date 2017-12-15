package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.OrderDetailsActivity;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.objectmodle.PrintOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class PickUpAdapter extends BaseAdapter {
    private List<Double> list;
    private Context context;
    private LayoutInflater inflater;
    int mSelect = -1;   //选中项

    public PickUpAdapter(Context context, List<Double> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pick_up, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传递数据，并跳转
                Toast.makeText(context,"抢单"+"id="+getItemId(position)+"内容="+getItem(position)+"全部="+getCount(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("id",1234);
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.my_image)
        ImageView myImage;
        @BindView(R.id.take_image)
        ImageView takeImage;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.take_address)
        TextView takeAddress;
        @BindView(R.id.give_address)
        TextView giveAddress;
        @BindView(R.id.payment_amount)
        TextView paymentAmount;
        @BindView(R.id.order_time)
        TextView orderTime;
        @BindView(R.id.urgent_order)
        TextView urgentOrder;
        @BindView(R.id.see_details)
        RelativeLayout seeDetails;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
