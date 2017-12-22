package com.android.slowlifecourier;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.http.OkHttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LbsService extends Service implements
        LocationSource,
        AMapLocationListener,
        OkHttpCallback.Impl {

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    // /声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private RegeocodeAddress result;
    private OkHttpClient httpClient;
    private OkHttpCallback callback;

    @Override
    public void onCreate() {
        super.onCreate();
        httpClient = new OkHttpClient();
        callback = new OkHttpCallback(this);
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2 * 60 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        Notification notification = new Notification(R.mipmap.logo,
                "wf update service is running",
                System.currentTimeMillis());
        Intent intent = new Intent();
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
//        notification.setLatestEventInfo(this, "WF Update Service",
//                "wf update service is running！", pintent);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.mContentTitle = "定位服务";
        builder.mContentInfo = "正在定位";
        builder.mLargeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        builder.setContentIntent(pintent);
        //让该service前台运行，避免手机休眠时系统自动杀掉该服务
        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
        startForeground(0, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (this.mapLocation == null) this.mapLocation = amapLocation;
            if (mListener != null) mListener.onLocationChanged(amapLocation);
            ((MyApplication) getApplication()).setLocation(amapLocation);

            //userStr :id, lat 纬度 , lng 经度,
            try {
                Info info = ((MyApplication) getApplication()).getInfo();
                if (info == null) {
                    deactivate();
                    stopSelf();
                    return;
                }
                JSONObject json = new JSONObject();
                json.put("id", info.getId());
                json.put("lat", amapLocation.getLatitude());
                json.put("lng", amapLocation.getLongitude());
                RequestBody requestBody = new MultipartBody.Builder()
                        .addFormDataPart("userStr", json.toString())
                        .build();
                Request request = new Request.Builder()
                        .url(Config.Url.getUrl("slowlife/appuser/userupdatelatlng"))
                        .post(requestBody)
                        .build();
                httpClient.newCall(request).enqueue(callback);
            } catch (JSONException e) {
            }
        } else {
            System.out.println("定位失败");
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deactivate();
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {

    }

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    public static boolean isServiceRunning(Context mContext) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClass().equals(LbsService.class) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
