package com.android.slowlifecourier.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.android.slowlifecourier.R;

/**
 * Created by sgrape on 2017/7/4.
 * e-mail: sgrape1153@gmail.com
 */

public class MessageDialog extends Dialog {
    private DialogButtonClickListener listener;
    private TextView title;
    private TextView message;

    public MessageDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        title = findView(R.id.dialog_msg_title);
        message = findView(R.id.dialog_msg_message);
        findView(R.id.dialog_msg_btn_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) listener.done(MessageDialog.this);
                    }
                });
        findView(R.id.dialog_msg_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.cancel(MessageDialog.this);
            }
        });


        int widght = getContext().getResources().getDisplayMetrics().widthPixels;
        getWindow().setLayout(widght / 3 * 2, -2);
    }

    @Override
    public void setTitle(CharSequence t) {
        title.setText(t);
    }

    public void setMessage(CharSequence msg) {
        message.setText(msg);
    }


    public void setListener(DialogButtonClickListener l) {
        this.listener = l;
    }


    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    public static interface DialogButtonClickListener {
        void done(Dialog dialog);

        void cancel(Dialog dialog);
    }
}
