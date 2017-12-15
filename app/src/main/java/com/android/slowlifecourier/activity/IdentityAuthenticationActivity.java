package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.objectmodle.AuthenticationInformationEntity;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.util.CompressImg;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
 * Created by Administrator on 2017/2/22 0022.
 */

public class IdentityAuthenticationActivity extends BaseActivity {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.id_number_edit)
    EditText idNumberEdit;
    @BindView(R.id.identity_front)
    ImageView identityFront;
    @BindView(R.id.upload_front)
    TextView uploadFront;
    @BindView(R.id.identity_opposite)
    ImageView identityOpposite;
    @BindView(R.id.upload_opposite)
    TextView uploadOpposite;
    @BindView(R.id.sure_bt)
    TextView sureBt;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.identity)
    TextView identity;
    @BindView(R.id.authentication)
    LinearLayout authentication;
    private File file1, file2;
    private boolean tag;//判断上传的是正面照true，还是反面照false

    /**
     * 初始化页面
     */
    protected void init() {
        file1 = new File(Environment.getExternalStorageDirectory().getPath()+"/2333"
                , UUID.randomUUID().toString() + ".png");

        file2 = new File(Environment.getExternalStorageDirectory().getPath()+"/2333"
                , UUID.randomUUID().toString() + ".png");
//                new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".png");
        AuthenticationInformationEntity entity = new AuthenticationInformationEntity();
        if ("1".equals(entity.getIdentityTag())) {
            scrollView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            authentication.setVisibility(View.VISIBLE);
            name.setText(entity.getIdentityName());
        } else {
            scrollView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            authentication.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back_rl, R.id.upload_front, R.id.upload_opposite, R.id.sure_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.upload_front://上传身份证正面照
                tag = true;
                popwindowsshow();
                break;
            case R.id.upload_opposite://上传身份证反面照
                tag = false;
                popwindowsshow();
                break;
            case R.id.sure_bt://确认提交
                submit();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            tag = savedInstanceState.getBoolean("tag");
    }

    private void submit() {
        if (isEmpty(nameEdit.getText().toString().trim())) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(idNumberEdit.getText().toString().trim())) {
            Toast.makeText(this, "请输入身份证号", Toast.LENGTH_SHORT).show();
        } else {
            Info info = ((MyApplication) getApplication()).getInfo();
            try {
                JSONObject userStr = new JSONObject();
                userStr.put("id", info.getId());
                userStr.put("name", nameEdit.getText().toString().trim());
                userStr.put("idNumber", idNumberEdit.getText().toString().trim());
                Map<String, Object> params = new HashMap<>();
                params.put("userStr", userStr);
                FileInputStream fis = new FileInputStream(file1);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len;
                byte[] buf = new byte[512];
                while ((len = fis.read(buf)) > 0) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                byte[] file1Data = baos.toByteArray();
                fis.close();
                baos.close();
                fis = new FileInputStream(file2);
                baos = new ByteArrayOutputStream();
                while ((len = fis.read(buf)) > 0) {
                    baos.write(buf, 0, len);
                    baos.flush();
                }
                byte[] file2Data = baos.toByteArray();
                fis.close();
                baos.close();
                Request.Builder builder = new Request.Builder().url(Config.Url.getUrl(Config.APPROVE)).tag(Config.UPLOADFILE);
                RequestBody fileRequest1 = RequestBody.create(MediaType.parse("application/octet-stream"), file1Data);
                RequestBody fileRequest2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2Data);
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("userStr", userStr.toString())
                        .addFormDataPart("pictures", file1.getName(), fileRequest1)
                        .addFormDataPart("pictures", file2.getName(), fileRequest2).build();
                builder.method("POST", requestBody);
                new OkHttpClient().newCall(builder.build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IdentityAuthenticationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(IdentityAuthenticationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    JSONObject result = new JSONObject(responseResult);
                                    System.out.println(result.toString());
                                    if (result.getInt("statusCode") != 200) {
                                        Toast.makeText(IdentityAuthenticationActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(IdentityAuthenticationActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(IdentityAuthenticationActivity.this, "解析数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(this, "解析数据失败", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "读取图片出错", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
                it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tag ? file1 : file2));
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
                case 2:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Toast.makeText(this, "图片文件读取出错", Toast.LENGTH_LONG).show();
                        return;
                    }
//                    try {
//                        Bitmap img = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(tag ? file1 : file2));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                case 1:
                    File file = tag ? file1 : file2;
                    if (!file.exists()) {
                        Toast.makeText(this, "图片文件读取出错", Toast.LENGTH_LONG).show();
                        return;
                    }
//                    if (file.length() > 2 * 1024 * 1024) {
//                        Toast.makeText(this, "所选图片文件超过2M", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    ToastUtil.showToast(this,file.length()/1024+"");
                    Log.e("file.length","丢了个雷姆"+file.length()/1024);
                    CompressImg.limit2M(file,file);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (tag) {
                        identityFront.setImageBitmap(bitmap);
                    } else identityOpposite.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("tag", tag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }
}
