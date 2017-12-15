package com.android.slowlifecourier.fragment;

import android.content.Context;
import android.view.View;

import com.android.slowlifecourier.adapter.OrderListAdapter;
import com.android.slowlifecourier.objectmodle.OrderEntity;

/**
 * Created by sgrape on 2017/7/10.
 * e-mail: sgrape1153@gmail.com
 */

public class CompleteFrag extends FragOrderList {

    public CompleteFrag() {
        super(null, "Complete", "CityWide,Intercity");
    }

    @Override
    protected void readInstanceState() {
        if (adapter == null) adapter = new Adapter(getContext());
        super.readInstanceState();
    }

    class Adapter extends OrderListAdapter {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected void setData(View view, OrderEntity order, int position) {
            super.setData(view, order, position);
            ViewHolder vh = (ViewHolder) view.getTag();
            if (vh.price.getVisibility() != View.VISIBLE)
                vh.price.setVisibility(View.VISIBLE);
            if (vh.grabSingle.getVisibility()==View.VISIBLE)
            vh.grabSingle.setVisibility(View.GONE);
        }
    }
}
