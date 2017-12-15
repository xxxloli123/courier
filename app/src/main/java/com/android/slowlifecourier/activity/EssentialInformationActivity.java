package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

public class EssentialInformationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.head_rl)
    RelativeLayout headRl;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_rl)
    RelativeLayout nameRl;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.phone_rl)
    RelativeLayout phoneRl;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address_rl)
    RelativeLayout addressRl;
    private Bitmap bitmap;
    private File imgPath;

    @Override
    protected void init() {
        super.init();
    }

    @OnClick({R.id.back_rl, R.id.head_rl, R.id.name_rl, R.id.phone_rl, R.id.address_rl})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.head_rl://头像
                popwindowsshow();
                break;
            case R.id.name_rl://昵称
                intent = new Intent(this, NameActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_rl://电话
                intent = new Intent(this, RevisePhoneNumberActivity.class);
                startActivity(intent);
                break;
            case R.id.address_rl://地址
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Info info = ((MyApplication) getApplication()).getInfo();
        name.setText(info.getNickName());
        phone.setText(info.getPhone());
        if (info.getHeadimg() != null) {
            bitmap = info.getHeadimg();
            head.setImageBitmap(toRoundBitmap());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_essential_information;
    }

    /**
     * 弹出窗口
     */
    private void popwindowsshow() {
        final PopupWindow pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.popwindows_head, null);
        final LinearLayout popwindows = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//拍照
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imgPath = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".png");
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgPath)); //指定图片输出地址
                startActivityForResult(it, 1);
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//从相册中选择
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 2);
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                popwindows.clearAnimation();
            }
        });
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 拍照上传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    System.out.println(imgPath.getAbsolutePath());
                    if (!imgPath.exists()) {
                        Toast.makeText(this, "读取文件出错", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent i = new Intent(this, ImageDisposeActivity.class);
                    i.putExtra(ImageDisposeActivity.BITMAPPATH, imgPath.getAbsolutePath());
                    startActivityForResult(i, 99);
//                    bitmap = Bitmap.createScaledBitmap(b, 180, 180, true);
//                    bitmap1 = toRoundBitmap(bitmap);
//                    head.setImageBitmap(bitmap1);
                    break;
                case 2:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    String path = null;
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        if (cursor.moveToFirst() && columnIndex >= 0)
                            path = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    if (path == null) {
                        System.out.println(uri.getPath());
                        try {
                            File dir = new File(getCacheDir(), "tem.png");
                            Bitmap img = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dir));
                            path = dir.getAbsolutePath();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent(this, ImageDisposeActivity.class);
                    intent.putExtra(ImageDisposeActivity.BITMAPPATH, path);
                    startActivityForResult(intent, 99);
                    break;
                case 99:
                    uploadImg(data);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 把bitmap转成圆形
     */
    public Bitmap toRoundBitmap() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        //取最短边做边长
        if (width < height) {
            r = width;
        } else {
            r = height;
        }
        //构建一个bitmap
        Bitmap backgroundBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBm);
        Paint p = new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

    // 上传头像
    private void uploadImg(Intent data) {
        final Info info = ((MyApplication) getApplication()).getInfo();
        bitmap = data.getParcelableExtra(ImageDisposeActivity.RESULT_IMG);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        Request.Builder builder = new Request.Builder().url(Config.Url.getUrl(Config.UPLOADFILE)).tag(Config.UPLOADFILE);
        RequestBody fileRequest = RequestBody.create(MediaType.parse("application/octet-stream"), baos.toByteArray());
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", info.getId())
                .addFormDataPart("type", "0")
                .addFormDataPart("pictures", info.getId() + ".png", fileRequest).build();

        builder.method("POST", requestBody);
        new OkHttpClient().newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EssentialInformationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int responseCode = response.code();
                final String responseResult = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode != 200) {
                            Toast.makeText(EssentialInformationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONObject result = new JSONObject(responseResult);
                            if (result.getInt("statusCode") != 200) {
                                Toast.makeText(EssentialInformationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            MyApplication app = (MyApplication) getApplication();
                            info.setHeadimg(bitmap);
                            info.setHeaderImg(result.getJSONObject("user").getString("headerImg"));
                            head.setImageBitmap(toRoundBitmap());
                        } catch (JSONException e) {
                            Toast.makeText(EssentialInformationActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }
}
