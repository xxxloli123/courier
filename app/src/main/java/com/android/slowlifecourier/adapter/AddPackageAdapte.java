package com.android.slowlifecourier.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.sgrape.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/4/20.
 */

public class AddPackageAdapte extends BaseAdapter<OrderEntity> {

    public AddPackageAdapte(Context context) {
        super(context);
    }

    @Override
    protected void setData(View view, OrderEntity obj, int position) {

    }

    @Override
    protected VH newViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_add_package;
    }


    public static void timeAppointmentDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_address, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(mDialog);// 设置对话框的布局
        ListView listview = (ListView) window.findViewById(R.id.list_dz);
        LinearLayout linear_qd = (LinearLayout) window.findViewById(R.id.linear_qd);

        //listview测试数据
        AddressAdapter adapte;
        String name[] = new String[]{"渝北区", "渝中区", "江北区", "合川区", "永川区", "沙坪坝区", "南岸区", "九龙坡区", "大渡口区", "北碚区"};
        List<String> list = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            list.add(name[i]);
        }
        adapte = new AddressAdapter(context, list);
        listview.setAdapter(adapte);

        linear_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    static class ViewHolder extends VH {
        @BindView(R.id.linear_dizi)
        LinearLayout linear_dizi;
        @BindView(R.id.text_dz)
        TextView text_dz;
        @BindView(R.id.text_zl)
        TextView text_zl;
        @BindView(R.id.image_jian)
        ImageView image_jian;
        @BindView(R.id.image_jia)
        ImageView image_jia;


        ViewHolder(View view) {
            super(view);
        }
    }
}
