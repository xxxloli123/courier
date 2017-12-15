package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.PayUtil;
import com.alipay.sdk.pay.demo.AuthResult;
import com.alipay.sdk.pay.demo.PayResult;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;
import com.slowlife.lib.MD5;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.check_ali)
    CheckBox ali;
    @BindView(R.id.check_wx)
    CheckBox wx;
    @BindView(R.id.cost)
    TextView cost;
    boolean isali = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }


    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case "slowlife/apppay/createrechargeorder":
                Map<String, Object> map = new HashMap<>();
                if (isali) {
                    map.put("way", "alipay");
                } else map.put("way", "wx");
                map.put("orderIds", json.getString("orderId"));
                if (isali)
                    newCall(Config.Url.getUrl("slowlife/apppay/createtrade"), map);
                else wxPay(json.getString("orderId"));
                break;
            case "slowlife/apppay/createtrade":
                String orderinfo = json.getString("alipayBody");
                PayUtil.aliPay(RechargeActivity.this, orderinfo);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.ali:
                if (wx.isChecked()) wx.setChecked(false);
                ali.setChecked(true);
                isali = true;
                break;
            case R.id.wx:
                if (ali.isChecked()) ali.setChecked(false);
                wx.setChecked(true);
                isali = false;
                break;
            case R.id.submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (!ali.isChecked() && !wx.isChecked()) {
            Toast.makeText(this, "请选择充值方式", Toast.LENGTH_SHORT).show();
            return;
        }
        String cose = this.cost.getText().toString().trim();
        if (isEmpty(cose) || cose == "0" || "0".equals(cose)) {
            Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
            return;
        }
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", info.getId());
        map.put("cost", cose);
        newCall(Config.Url.getUrl("slowlife/apppay/createrechargeorder"), map);
    }


    private void wxPay(String orderId) {
        final PayReq req = new PayReq();
        final String appId = "wx86a1cec05b33ad1f";
        final String appSecret = "a1ec1e873ca8045afa898e060947128b";
        req.appId = appId;
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        req.packageValue = "Sign=WXPay";
        final IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
        //  是否支持支付createtrade
        final String url = Config.Url.getUrl("slowlife/wxpay/createwxtrade");
        try {
            boolean reg = api.registerApp(appId);
            if (!reg) {
                Toast.makeText(this, "注册到微信失败", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
        }
        Info info = ((MyApplication) getApplication()).getInfo();
        if (info == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("orderIds", orderId);

        Request request = new Request.Builder()
                .post(requestBody.build())
                .url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(0, "网络错误").sendToTarget();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code() != 200) {
                    mHandler.obtainMessage(0, "服务器错误").sendToTarget();
                    return;
                }
                final String buf = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!TextUtils.isEmpty(buf)) {
                                JSONObject res = new JSONObject(buf);
                                if (res.getInt("statusCode") != 200) {
                                    Toast.makeText(RechargeActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (null != res && res.has("wxresult")) {
                                    JSONObject json = res.getJSONObject("wxresult");
                                    final PayReq req = new PayReq();
                                    req.appId = appId;
                                    req.partnerId = json.getJSONArray("mch_id").getString(0);
                                    req.prepayId = json.getJSONArray("prepay_id").getString(0);
                                    req.nonceStr = json.getJSONArray("nonce_str").getString(0);
                                    req.timeStamp = String.valueOf(System.currentTimeMillis()/1000);
                                    req.packageValue = "Sign=WXPay";


                                    StringBuffer signsb = new StringBuffer();
                                    signsb.append("appid=")
                                            .append(appId)
                                            .append("&noncestr=")
                                            .append(req.nonceStr)
                                            .append("&package=")
                                            .append(req.packageValue)
                                            .append("&partnerid=")
                                            .append(req.partnerId)
                                            .append("&prepayid=")
                                            .append(req.prepayId)
                                            .append("&timestamp=")
                                            .append(req.timeStamp)
                                            .append("&key=")
                                            .append("e866f0d58b12ae41dd677d2c7f313d5b");
                                    req.sign = MD5.md5(signsb.toString()).toUpperCase();

                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    boolean result = api.sendReq(req);
                                    if (result)
                                        Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(RechargeActivity.this, "调用支付失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RechargeActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            mHandler.obtainMessage(0, "异常：" + e.getMessage()).sendToTarget();
                        }
                    }
                });
            }
        });
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(RechargeActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(RechargeActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case 0: {
                    Toast.makeText(RechargeActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
                default:
                    break;
            }
        }
    };
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
}
