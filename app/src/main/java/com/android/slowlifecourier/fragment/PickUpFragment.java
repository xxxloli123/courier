package com.android.slowlifecourier.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.OrderDetailsActivity;
import com.android.slowlifecourier.adapter.AddressAdapter;
import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.bluetoothprint.print.PrintQueue;
import com.android.slowlifecourier.bluetoothprint.printutil.PrintOrderDataMaker;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter58mm;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.dialog.MessageDialog;
import com.android.slowlifecourier.http.OkHttp;
import com.android.slowlifecourier.http.OkHttpCallback;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.objectmodle.PrintOrder;
import com.android.slowlifecourier.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private String exchangePostmanId;

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
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.grabSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(listview, view, position, position);
                }
            });
            holder.alreadyDelivery.setVisibility(View.VISIBLE);
            holder.alreadyDelivery.setText("换单");
            holder.alreadyDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MessageDialog dialog = new MessageDialog(getActivity());
//                    dialog.show();
//                    dialog.setMessage("确定取消订单吗?");
//                    dialog.setListener(new MessageDialog.DialogButtonClickListener() {
//                        @Override
//                        public void done(Dialog dialog) {
//                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                                    .addFormDataPart("orderId", order.getId())
//                                    .addFormDataPart("userId", info.getId())
//                                    .build();
//                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.CANCELORDER)).post(requestBody2).build();
//                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(getActivity()) {
//                                @Override
//                                public void onSuccess(String tag, JSONObject json) throws JSONException {
//                                    Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
//                                    adapter.getData().remove(order);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                            dialog.dismiss();
//                        }
//                        @Override
//                        public void cancel(Dialog dialog) {
//                            dialog.dismiss();
//                        }
//                    });
//                    adapter.notifyDataSetChanged();
                    exchangeOrder(order);
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

    private void exchangeOrder(final OrderEntity order) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_address, null);
        final Dialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(mDialog);// 设置对话框的布局
        final ListView listview = (ListView) window.findViewById(R.id.list_dz);
        final LinearLayout linear_qd = (LinearLayout) window.findViewById(R.id.linear_qd);

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", order.getId());
        OkHttp.Call(Config.Url.getUrl(Config.GET_Courier), params, new OkHttpCallback.Impl() {
            @Override
            public void onSuccess(Object tag, JSONObject json) throws JSONException {
                final JSONArray arr = json.getJSONArray("list");
                List<String> list = new ArrayList<>();
                if (arr.length()==0){
                    return;
                }
                for (int i = 0; i < arr.length(); i++) {
                    //[
//                        "2c9ad8435d40a6e5015d43d951d200b7",
//                                "黄杰"
//                        ]
                    list.add(arr.getJSONArray(i).get(1).toString());
                }
                final AddressAdapter adapte = new AddressAdapter(getContext(), list);
                listview.setAdapter(adapte);
                final Info info = ((MyApplication) getContext().getApplicationContext()).getInfo();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            adapte.changeSelected(position);
                            exchangePostmanId=arr.getJSONArray(position).get(0).toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                linear_qd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exchangePostmanId==null)return;
//                        接单快递员提交换单请求;exchangePostmanId,待换单快递员id
//                        参数：[orderId, userId, exchangePostmanId]
                        Map<String, Object> params = new HashMap<>();
                        params.put("orderId", order.getId());
                        params.put("userId", info.getId());
                        params.put("exchangePostmanId", exchangePostmanId);
                        newCall(Config.Url.getUrl(Config.RequestExchangeOrder), params);
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void fail(Object tag, int code, JSONObject json) throws JSONException {

            }

            @NonNull
            @Override
            public Context getContext() {
                return getActivity();
            }
        });
    }

    class VH extends OrderListAdapter.ViewHolder {
        VH(View view) {
            super(view);
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        super.onSuccess(tag, json);
        if (tag.toString().equals(Config.RequestExchangeOrder))
            ToastUtil.showToast(getContext(),json.getString("message"));
    }
}
