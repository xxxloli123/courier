package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.slowlifecourier.activity.OrderDetailsActivity;
import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.bluetoothprint.print.PrintQueue;
import com.android.slowlifecourier.bluetoothprint.printutil.PrintOrderDataMaker;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter58mm;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.dialog.MessageDialog;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.objectmodle.PrintOrder;
import com.android.slowlifecourier.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/27 0027.
 * <p>
 * TODO   待送货Frag
 */

@SuppressLint("ValidFragment")
public class PickUpFragment extends FragOrderList implements AdapterView.OnItemClickListener {
    public PickUpFragment() {
        super();
    }

    public PickUpFragment(String rob, String status) {
        super(rob, status);
    }

    private void submit(int position) {
        Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("orderId", adapter.getItem(position).getId());
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter();
        super.readInstanceState();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        int position = data.getIntExtra("position", -1);
        if (position == -1) return;
        adapter.getData().remove(position);
        adapter.notifyDataSetChanged();
        if (adapter.getCount() == 0) noOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        submit(position);

    }

    class Adapter extends OrderListAdapter {

        public Adapter() {
            super(getContext());
        }

        @Override
        protected void setData(final View view, final OrderEntity order, final int position) {
            super.setData(view, order, position);
            final Info info = ((MyApplication) getContext().getApplicationContext()).getInfo();
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(listview, view, position, position);
                }
            });
            holder.alreadyDelivery.setVisibility(View.VISIBLE);
            holder.alreadyDelivery.setText("取消");
            holder.alreadyDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageDialog dialog = new MessageDialog(getActivity());
                    dialog.show();
                    dialog.setMessage("确定取消订单吗?");
                    dialog.setListener(new MessageDialog.DialogButtonClickListener() {
                        @Override
                        public void done(Dialog dialog) {
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("orderId", order.getId())
                                    .addFormDataPart("userId", info.getId())
                                    .build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.CANCELORDER)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(getActivity()) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                        @Override
                        public void cancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(listview, view, position, position);
                }
            });
        }

        @Override
        protected VH newViewHolder(View v) {
            return new PickUpFragment.VH(v);
        }
    }

    class VH extends OrderListAdapter.ViewHolder {
        VH(View view) {
            super(view);
        }
    }
}
