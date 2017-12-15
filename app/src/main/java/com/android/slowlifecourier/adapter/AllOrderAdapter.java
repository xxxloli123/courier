package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.TransactionRecordActivity;
import com.android.slowlifecourier.objectmodle.OrdersInfoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class AllOrderAdapter extends BaseAdapter {
    private Context context;
    private List<OrdersInfoEntity> list;
    private LayoutInflater inflater;

    public AllOrderAdapter(Context context, List<OrdersInfoEntity> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_job_summary, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        initData(holder,position);
        return convertView;
    }
    /**
     * 填充相应的值
     * */
    private void initData(ViewHolder holder, final int position){
        if("0".equals(list.get(position).getTag())){
            holder.tag.setText("完成单");
            holder.tag.setTextColor(context.getResources().getColor(R.color.blue));
        }else if("1".equals(list.get(position).getTag())){
            holder.tag.setText("异常单");
            holder.tag.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            holder.tag.setText("取消单");
            holder.tag.setTextColor(context.getResources().getColor(R.color.orange));
        }
        holder.price.setText("￥"+list.get(position).getPrice());
        holder.commodityDetails.setText(list.get(position).getCommodityDetails());
        holder.transactionNumber.setText(list.get(position).getTransactionNumber());
        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TransactionRecordActivity.class);
                intent.putExtra("tag",list.get(position).getTag());
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.payment_method)
        TextView paymentMethod;
        @BindView(R.id.commodity_details)
        TextView commodityDetails;
        @BindView(R.id.transaction_number)
        TextView transactionNumber;
        @BindView(R.id.view_details)
        RelativeLayout viewDetails;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
