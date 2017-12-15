package com.android.slowlifecourier.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.util.CacheActivity;
import com.android.slowlifecourier.view.PixelFormat;
import com.android.slowlifecourier.view.ShSwitchView;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class SetActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.my_position)
    TextView myPosition;
    @BindView(R.id.switch_view)
    ShSwitchView switchView;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.quit)
    Button quit;
    private Unbinder unbinder;

    @OnClick({R.id.back_rl, R.id.my_position, R.id.quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.my_position:
                showToast();
                break;
            case R.id.quit:
                quitDialog();
                break;
            case R.id.update:
                update(this, getVersionCode(this));
                break;
        }
    }

    @Override
    protected void init() {
        super.init();

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String verName = packageInfo.versionName;
            version.setText("V" + verName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set;
    }

    /**
     * 自定义toast
     */
    private void showToast() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.toast_dialog, null);
        TextView position = (TextView) view.findViewById(R.id.position);
        TextView longitudeLatitude = (TextView) view.findViewById(R.id.longitude_latitude);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, PixelFormat.formatDipToPx(this, 70));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    /**
     * 弹出退出窗口
     */
    private void quitDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_quit, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((MyApplication) getApplication()).setInfo(null);
                CacheActivity.finishActivity();
                Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void update(final Context context, final int versionCode) {
        Request req = new Request.Builder()
                .tag("")
                .url(Config.Url.getUrl(Config.UPDATE)).build();
        new OkHttpClient().newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    int ver = json.getJSONObject("appVersion").getInt("version");
                    if (ver > versionCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //创建下载任务,downloadUrl就是下载链接
                                DownloadManager.Request request = new DownloadManager.Request(
                                        Uri.parse(Config.Url.getUrl("/slowlife/share/appdownload?type=androidCourier")));
                                //指定下载路径和下载文件名
                                request.setDestinationInExternalPublicDir("/sdcard/Android/" + context.getPackageName(), "惠递到家.apk");
                                request.setDescription("惠递到家新版本下载");
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setMimeType("application/vnd.android.package-archive");
                                // 设置为可被媒体扫描器找到
                                request.allowScanningByMediaScanner();
                                // 设置为可见和可管理
                                request.setVisibleInDownloadsUi(true);
                                //获取下载管理器
                                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                //将下载任务加入下载队列，否则不会进行下载
                                downloadManager.enqueue(request);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "已安装最新版", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private static Handler handler = new Handler();
}
