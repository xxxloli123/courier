package com.android.slowlifecourier.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.DialogBg;
import com.sgrape.dialog.Dialog;

/**
 * Created by Administrator on 2017/9/1.
 */

public class LoadDialog1  extends Dialog {
    private TextView msg;


    public LoadDialog1(@NonNull Context context) {
        super(context, com.sgrape.util.R.style.DialogStyle);
        ProgressBar prog = new ProgressBar(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(prog);
        msg = new TextView(context);

        linearLayout.addView(msg);
        linearLayout.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearLayout.setBackground(new DialogBg(20, Color.BLACK));
        } else linearLayout.setBackgroundDrawable(new DialogBg(20, Color.BLACK));
        int size=context.getResources().getDimensionPixelOffset(com.sgrape.util.R.dimen.loadDialogSize);
        setLayout(size,size);
        setContentView(linearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setMessage(CharSequence txt) {
        this.msg.setText(txt);
    }

}
