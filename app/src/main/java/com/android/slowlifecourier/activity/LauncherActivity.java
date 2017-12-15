package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlifecourier.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class LauncherActivity extends Activity implements View.OnClickListener {
    private TextView countDown;
    private LinearLayout run;
    private int recLen = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 注意：添加3秒睡眠，以确保黑屏一会儿的效果明显，在项目应用要去掉这3秒睡眠
        setContentView(R.layout.activity_launcher);
        countDown = (TextView) findViewById(R.id.count_down);
        run = (LinearLayout) findViewById(R.id.run);
        run.setOnClickListener(this);
        //倒计时开始
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // TODO 处理推送
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(--recLen);
            }
        }, 1000, 1000);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (recLen < 0) {
                timer.cancel();
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            } else {
                countDown.setText(String.valueOf(msg.what));
            }
        }
    };
    Timer timer = new Timer();

    @Override
    public void onClick(View v) {
        timer.cancel();
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();
    }
}
