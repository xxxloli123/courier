package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.OrderDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class TodayOrderAdapter extends BaseAdapter {
    private List<Double> list;
    private Context context;
    private LayoutInflater inflater;
    int mSelect = -1;   //选中项

    /**
     * 刷新方法
     **/
    public void changeSelected(int positon){
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    public TodayOrderAdapter(Context context, List<Double> list) {
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
            convertView = inflater.inflate(R.layout.item_today_order, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.price.setText(list.get(position)+"");

//        if(mSelect==position){
//            convertView.setBackgroundResource(R.color.mask_color);  //选中项背景
//        }else{
//            convertView.setBackgroundResource(R.color.white);  //其他项背景
//        }
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"待送达详情内容"+"id="+getItemId(position)+"内容="+getItem(position)+"全部="+getCount(),Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.linear)
        LinearLayout linear;
        @BindView(R.id.my_image)
        ImageView myImage;
        @BindView(R.id.take_image)
        ImageView takeImage;
        @BindView(R.id.price)
        TextView price;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
