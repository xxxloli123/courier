package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.view.MyRadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/31.
 */

public class SelectCityAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private JSONArray areas;

    public SelectCityAdapter(Context mContext, JSONArray areas) {
        this.areas = areas;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return areas == null ? 0 : areas.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return areas.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SelectProvinceAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select_area, null);
            holder = new SelectProvinceAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SelectProvinceAdapter.ViewHolder) convertView.getTag();
        }
        try {
            final JSONObject addr = areas.getJSONObject(position);
            holder.radio.setText(addr.getString("name").toString());
        } catch (JSONException e) {
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