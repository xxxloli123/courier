package com.android.slowlifecourier.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.slowlifecourier.BaseFragment;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.TodayOrderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class YesterdayOrderFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.no_order)
    LinearLayout noOrder;
    private Unbinder unbinder;
    private TodayOrderAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_order, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Double> list=new ArrayList<>();
        for(int i=0;i<8;i++){
            list.add(23.1+i);
        }
        adapter=new TodayOrderAdapter(getActivity(),list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.changeSelected(position);
        complaintOrderDialog(getActivity());
    }
    /**
     * 弹出投诉订单窗口窗口
     */
    public static void complaintOrderDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_complaint_order, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
}
