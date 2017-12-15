package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlifecourier.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/21.
 */

public class AddressAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    int mSelect = -1;   //选中项

    public AddressAdapter(Context context, List<String> list) {
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
        AddressAdapter.ViewHolder holder=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dizhi, null);
            holder=new AddressAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (AddressAdapter.ViewHolder) convertView.getTag();
        }
        holder.text_cq.setText(list.get(position)+"");
        holder.linear_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.text_cq)
        TextView text_cq;
        @BindView(R.id.linear_dz)
        LinearLayout linear_dz;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}