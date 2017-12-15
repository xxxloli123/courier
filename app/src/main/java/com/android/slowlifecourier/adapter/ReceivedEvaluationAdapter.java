package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.CourierEvaluateEntity;
import com.android.slowlifecourier.view.RatingBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class ReceivedEvaluationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<CourierEvaluateEntity.EvaluateInfoBean.AaDataBean> list;

    public ReceivedEvaluationAdapter(Context mContext, List<CourierEvaluateEntity.EvaluateInfoBean.AaDataBean> list) {
        this.mContext = mContext;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }
//    list == null || list.isEmpty() ? 0 : list.size()
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
            convertView = inflater.inflate(R.layout.item_received_evaluation, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.comment.setText(list.get(position).getComment());
        holder.time.setText(list.get(position).getCreateDate());
        holder.ratingBar.setCountSelected(list.get(position).getGrade());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.comment)
        TextView comment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
