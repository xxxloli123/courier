package com.android.slowlifecourier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sgrape on 2017/7/29.
 * e-mail: sgrape1153@gmail.com
 */

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (LbsService.isServiceRunning(context)) {
            context.startService(new Intent(context, LbsService.class));
        }
    }
}
