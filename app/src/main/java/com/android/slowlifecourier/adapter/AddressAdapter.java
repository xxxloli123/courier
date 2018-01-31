package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.view.MyRadioButton;

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

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select_province, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.radio.setText(list.get(position) + "");
        if (mSelect == position) {
            holder.radio.setChecked(true);
        } else {
            holder.radio.setChecked(false);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.radio)
        MyRadioButton radio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}