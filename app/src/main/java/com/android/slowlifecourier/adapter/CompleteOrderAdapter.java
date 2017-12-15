package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.view.RatingBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class CompleteOrderAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;

    public CompleteOrderAdapter(Context context, List<String> list) {
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
            convertView = inflater.inflate(R.layout.item_complete_order, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.comment.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.comment)
        TextView comment;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.remarks)
        TextView remarks;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
