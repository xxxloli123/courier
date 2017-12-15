package com.android.slowlifecourier.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.DisplayUtil;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.objectmodle.CompanyEntity;
import com.sgrape.adapter.BaseAdapter;
import com.sgrape.dialog.Dialog;

import java.util.List;

/**
 * Created by sgrape on 2017/5/23.
 * e-mail: sgrape1153@gmail.com
 */

public class SelectCourier extends Dialog {
    private ListView listView;
    private Adapter adapter;
    private List<CompanyEntity> list;
    private int selectedPosition=-1;

    public SelectCourier(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_courier);
        listView = searchView(R.id.dia_courier_listview);
        adapter = new Adapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setBackgroundColor(Color.WHITE);
        listView.setLayoutParams(new LinearLayout.LayoutParams(-1, DisplayUtil.dip2px(getContext(), 200)));
        listView.setItemChecked(selectedPosition,true);
    }


    public void setData(List<CompanyEntity> data) {
        this.list = data;
        if (adapter != null) adapter.notifyDataSetChanged(data);
    }
    public List<CompanyEntity> getData(){
        return list;
    }

    public int getSelectedItemPosition() {
        return listView.getCheckedItemPosition();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    class Adapter extends BaseAdapter<CompanyEntity> {
        private int padding;

        public Adapter(Context context, List<CompanyEntity> list) {
            super(context, list);
            padding = DisplayUtil.dip2px(context, 8);
        }

        @Override
        protected void setData(View view, CompanyEntity entity,final int poisition) {
            TextView textView = (TextView) view;
            textView.setText(entity.getName());
        }

        @Override
        protected View getView() {
            RadioButton view = new RadioButton(inflater.getContext());
            view.setPadding(padding, padding, padding, padding);
            view.setTextSize(14);
            view.setButtonDrawable(null);
            view.setFocusable(false);
            view.setClickable(false);
            view.setFocusableInTouchMode(false);
            view.setBackgroundResource(R.drawable.list_dialog_item_selector);
            return view;
        }

        @Override
        protected VH newViewHolder(View v) {
            return null;
        }

        @Override
        protected int getLayoutId() {
            return 0;
        }
    }


}
