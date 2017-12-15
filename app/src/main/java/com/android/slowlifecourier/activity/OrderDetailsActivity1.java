package com.android.slowlifecourier.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.objectmodle.OrdersEntity;
import com.android.slowlifecourier.objectmodle.PayStatus;
import com.android.slowlifecourier.util.LogoConfig;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.android.slowlifecourier.objectmodle.OrderStatus.Complete;
import static com.android.slowlifecourier.objectmodle.OrderStatus.GoodsDelivery;
import static com.android.slowlifecourier.objectmodle.OrderStatus.ReceivedOrder;
import static com.android.slowlifecourier.objectmodle.OrderStatus.UnReceivedOrder;


public class OrderDetailsActivity1 extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.user_head)
    ImageView userHead;
    @BindView(R.id.info)
    RelativeLayout info;
    @BindView(R.id.anonymity)
    TextView anonymity;
    @BindView(R.id.evaluate_Starbar)
    AppCompatRatingBar evaluateStarbar;
    @BindView(R.id.evaluate_ver)
    TextView evaluateVer;
    @BindView(R.id.header2)
    LinearLayout header2;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.expresscompany)
    TextView expresscompany;
    @BindView(R.id.expresscompany1)
    TextView expresscompany1;
    @BindView(R.id.isUrgent)
    TextView isUrgent;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userPhone)
    TextView userPhone;
    @BindView(R.id.startAddr)
    TextView startAddr;
    @BindView(R.id.recipients)
    TextView recipients;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.urgent_price)
    TextView urgentPrice;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.sTime)
    TextView sTime;
    @BindView(R.id.gTime)
    TextView gTime;
    @BindView(R.id.require)
    TextView require;
    @BindView(R.id.consume)
    TextView consume;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.city)
    View city;
    @BindView(R.id.expresscompanyphone)
    TextView expresscompanyphone;
    @BindView(R.id.recipientsPhone)
    TextView recipientsPhone;
    @BindView(R.id.endAddr)
    TextView endAddr;
    @BindView(R.id.recipients_layout)
    LinearLayout recipientsLayout;
    @BindView(R.id.price_layout)
    LinearLayout priceLayout;
    @BindView(R.id.total_layout)
    LinearLayout totalLayout;
    @BindView(R.id.require_layout)
    TableRow requireLayout;
    @BindView(R.id.consume_layout)
    TableRow consumeLayout;
    @BindView(R.id.gTime_layout)
    TableRow gTimeLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.imageLayout)
    RelativeLayout imageLayout;
    @BindView(R.id.ordertype)
    TextView type;
    @BindView(R.id.weight)
    TextView weight;

    @BindView(R.id.order_status)
    TextView orderStatus;

    private OrdersEntity oe;
    public static final String ORDER = "order";
    public static final String ORDER_ID = "order_id";


    private void getOrder(String orderId) {
        Map<String, Object> map = new HashMap<>();
        newCall(Config.Url.getUrl(Config.ORDER_DETAILS), map);
    }

    protected void init() {
        oe = getIntent().getParcelableExtra(ORDER);
        String orderId = getIntent().getStringExtra(ORDER_ID);
        if (oe == null && isEmpty(orderId)) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (oe != null) init();
        else getOrder(orderId);
        orderStatus.setText(oe.getOrderStatus().getDescribe());
        switch (oe.getOrderStatus()) {
            case UnPayed:
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    imageLayout.setVisibility(View.VISIBLE);
                recipientsLayout.setVisibility(View.VISIBLE);
                break;
            case ReceivedOrder:
            case UnReceivedOrder:
                if (!oe.getOrderStatus().equals(ReceivedOrder)) {
                    priceLayout.setVisibility(View.GONE);
                }
                totalLayout.setVisibility(View.GONE);
                gTimeLayout.setVisibility(View.GONE);
                if (oe.getOrderStatus().equals(UnReceivedOrder)) {
                    consumeLayout.setVisibility(View.GONE);
                }
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    recipientsLayout.setVisibility(View.GONE);
                break;
            case GoodsDelivery:
                totalLayout.setVisibility(View.GONE);
                gTimeLayout.setVisibility(View.GONE);
                consumeLayout.setVisibility(View.GONE);
                recipientsLayout.setVisibility(View.VISIBLE);
                break;
            case CancelOrder:
                priceLayout.setVisibility(View.GONE);
                totalLayout.setVisibility(View.GONE);
                gTimeLayout.setVisibility(View.GONE);
                consumeLayout.setVisibility(View.GONE);
                if (TextUtils.equals(oe.getType(), "Intercity"))
                    recipientsLayout.setVisibility(View.GONE);
                break;
        }
        if (TextUtils.equals(oe.getType(), "Intercity")) {
//            city.setVisibility(View.VISIBLE);
            type.setText("保\n价\n费");
            expresscompany.setText(oe.getUserChoiceCommpanyName());
            expresscompanyphone.setText(oe.getLogisticsNumber());
            urgentPrice.setText("¥" + oe.getInsuredFee());
            img1.setImageResource(LogoConfig.getLogoRes(oe.getUserChoiceCommpanyName()));
        } else {
            expresscompany1.setText(oe.getUserChoiceCommpanyName());
            urgentPrice.setText("¥" + oe.getUrgentFee());
        }
        if (TextUtils.equals(oe.getUrgent(), "yes"))
            isUrgent.setVisibility(View.VISIBLE);
        userName.setText(oe.getCreateUserName());
        userPhone.setText(oe.getCreateUserPhone());
        startAddr.setText(String.format("%s%s%s%s%s", oe.getStartPro(), oe.getStartPro(),
                oe.getStartDistrict(), oe.getStartStreet(), oe.getStartHouseNumber()));
        recipients.setText(oe.getReceiverName());                 // 收件人
        recipientsPhone.setText(oe.getReceiverPhone());            // 电话
        endAddr.setText(String.format("%s%s%s%s%s", oe.getEndPro(), oe.getEndCity(), oe.getEndDistrict(),
                oe.getEndStreet(), oe.getEndHouseNumber()));
        price.setText("¥" + oe.getCost());
        totalPrice.setText("¥" + (oe.getCost() + oe.getUrgentFee()));
        orderNum.setText(oe.getOrderNumber());
        sTime.setText(oe.getCreateDate());      // 建单时间
        gTime.setText(oe.getPostmanPickupDate());                      // 送达时间
        require.setText("2小时内");                    // 时效
        weight.setText(oe.getWeight() + "kg");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 取件消耗
            if (!isEmpty(oe.getPostmanPickupDate())
                    && !isEmpty(oe.getCreateDate())) {
                long et = format.parse(oe.getPostmanPickupDate()).getTime();
                long st = format.parse(oe.getCreateDate()).getTime();
                long t = (et - st);
                int h = (int) (t / 60 / 60 / 1000);
                int m = (int) ((t % (60 * 60 * 1000)) / 60 / 1000);
                consume.setText(String.format("%d小时%d分钟", h, m));
            } else
                consumeLayout.setVisibility(View.GONE);
        } catch (ParseException e) {
            consumeLayout.setVisibility(View.GONE);
        }
        loadImg();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_details1;
    }


    private void loadImg() {
        if (oe.getType().equals("Intercity")
                && (oe.getOrderStatus().equals(GoodsDelivery)
                || oe.getOrderStatus().equals(Complete)
                || oe.getOrderStatus().equals(PayStatus.UnPayed))
                && !isEmpty(oe.getTradeImg())) {
            final Request request = new Request.Builder().url(Config.IMG + oe.getTradeImg()).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OrderDetailsActivity1.this, "加载交易快照失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final Bitmap img = BitmapFactory.decodeStream(response.body().byteStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageLayout.setVisibility(View.VISIBLE);
                            image.setImageBitmap(img);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.ORDER_DETAILS:
                oe = new Gson().fromJson(json.getString("order"), OrdersEntity.class);
                init();
                break;
        }
    }
}
