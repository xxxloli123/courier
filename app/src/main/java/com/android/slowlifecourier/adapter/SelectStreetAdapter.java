package com.android.slowlifecourier.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.SelectDistrictEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 */

public class SelectStreetAdapter extends BaseAdapter {
//    private List<SelectDistrictEntity.CompanylistBean> customers;
    private List<SelectDistrictEntity> district;
    Context context;

    public SelectStreetAdapter(Context context, List<SelectDistrictEntity> district){
        this.district = district;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (district==null)?0:district.size();
    }

    @Override
    public Object getItem(int position) {
        return district.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ViewHolder{
        TextView textViewItem01;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectDistrictEntity district = (SelectDistrictEntity)getItem(position);
        ViewHolder viewHolder = null;
        if(convertView==null){
            Log.d("MyBaseAdapter", "新建convertView,position="+position);
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.select_street_list, null);

            viewHolder = new ViewHolder();

            viewHolder.textViewItem01 = (TextView)convertView.findViewById(
                    R.id.tv_address_street);





            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
            Log.d("MyBaseAdapter", "旧的convertView,position="+position);
        }

        viewHolder.textViewItem01.setText(district.getStreet());





        return convertView;
    }

}