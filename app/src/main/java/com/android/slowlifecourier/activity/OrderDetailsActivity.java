package com.android.slowlifecourier.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adressselectorlib.AddressSelector;
import com.android.slowlifecourier.adressselectorlib.CityInterface;
import com.android.slowlifecourier.adressselectorlib.OnItemClickListener;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.bluetoothprint.print.PrintQueue;
import com.android.slowlifecourier.bluetoothprint.printutil.PrintOrderDataMaker;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter;
import com.android.slowlifecourier.bluetoothprint.printutil.PrinterWriter58mm;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;
import com.android.slowlifecourier.dialog.MessageDialog;
import com.android.slowlifecourier.greendao.DBManagerOrder;
import com.android.slowlifecourier.greendao.SpecialOrder;
import com.android.slowlifecourier.objectmodle.City;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.objectmodle.PrintOrder;
import com.android.slowlifecourier.util.SimpleCallback;
import com.android.slowlifecourier.view.Order;
import com.google.gson.Gson;
import com.interfaceconfig.Config;
import com.sgrape.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.android.slowlifecourier.R.id.order_list;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class OrderDetailsActivity extends BaseActivity implements LocationSource, AMapLocationListener {
    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sure_bt)
    Button sureBt;
    @BindView(com.slowlife.map.R.id.mapView)
    MapView mapView;
    @BindView(order_list)
    LinearLayout orderlist;
    @BindView(R.id.take_address)
    TextView takeAddress;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.give_user)
    TextView giveUser;
    @BindView(R.id.phone1)
    TextView phone1;
    @BindView(R.id.scrollview)
    ScrollView scrollView;

    private boolean mFirstFix;
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private AMapLocation mapLocation;
    private Marker mLocMarker;
    private OrderEntity order;
    private int position;
    private AreaDialog dialog;
    private String temPath;
    private JSONArray areaList;
    private Order tag;
    private List<Order> viewList = new LinkedList<>();
    private Map<String, Object> area = new HashMap<>();
//选择地区
    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private AddressSelector addressSelector;
    private JSONArray proArray, cityArray, districtArray;
    private JSONObject proObject, ctiyObject, districtObject;
    private String urgentCost;
    BluetoothAdapter mAdapter;
    private boolean isHave;
    private DBManagerOrder dbManagerOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        dbManagerOrder=new DBManagerOrder(this);
        initMap();
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position", -1);
            temPath = savedInstanceState.getString("temPath");
            if (position == -1) {
                finish();
                return;
            }
        }
        temPath = new File(getExternalCacheDir(), "tem").getAbsolutePath();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        if (aMap == null) {
            if (mapView == null) return;
            aMap = mapView.getMap();
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setScaleControlsEnabled(true);
        }
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
        mFirstFix = false;
        deactivate();
    }

    protected void init() {
        initOrder();
    }

    void initViews() {
        viewList.get(0).update(order);
        Info info = ((MyApplication) getApplication()).getInfo();
        if (TextUtils.equals("CityWide", order.getType())) {
            Map<String, Object> map = new HashMap<>();
//        userId:用户id；pro:省,city：市,district：区县,street:街道,type:[CityWide(同城), Intercity(城际);] expressCompanyId:快递公司id
            map.put("userId", info.getId());
            map.put("pro", order.getStartPro());
            map.put("city", order.getStartCity());
            map.put("district", order.getStartDistrict());
            map.put("street", order.getStartStreet());
            map.put("type", order.getType());
            map.put("expressCompanyId", order.getUserChoiceCommpanyId());
            newCall(Config.Url.getUrl(Config.AREAINFO), map);
        } else {
            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("pid", "")
                    .addFormDataPart("cid", "").build();
            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(OrderDetailsActivity.this) {
                @Override
                public void onSuccess(String tag, JSONObject json) throws JSONException {
                    proArray = json.getJSONArray("Pro");
                    for (int i = 0; i < proArray.length(); i++) {
                        JSONObject jsonObject = proArray.getJSONObject(i);
                        City city = new City();
                        city.setId(jsonObject.getString("id"));
                        city.setName(jsonObject.getString("name"));
                        cities1.add(city);
                        Log.d("MyBaseAdapter", "区域=" + cities1);
                    }
                }
            });
        }
        takeAddress.setText(String.format("%s%s%s%s%s", order.getStartPro(), order.getStartCity(),
                order.getStartDistrict(), order.getStartStreet(), order.getStartHouseNumber())
                .replace("null", ""));
        giveUser.setText(order.getOrderNumber());
        phone1.setText(order.getCreateUserPhone());
    }

    @Override
    protected void initData() {
        Map<String, Object> params = new HashMap<>();
        Intent intent = getIntent();
        String id = intent.getStringExtra("orderId");
        if (isEmpty(id)) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        position = intent.getIntExtra("position", -1);
        if (isEmpty(id)) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("id", id);
        newCall(Config.Url.getUrl(Config.ORDERDETAILS), params);
    }

    @OnClick({R.id.back_rl, R.id.sure_bt})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.sure_bt:
                submit();
                break;
            case R.id.quality_layout:
                break;
            case R.id.sure_add:
                addOrder();
                break;
        }
    }

    private void printOrder(PrintOrder printOrder) {
        if (TextUtils.isEmpty(AppInfo.btAddress)){
            ToastUtil.showToast(this,"已接单。。。\n如需打印订单请开启打印机功能");
            return;
        }
        if (mAdapter.getState()== BluetoothAdapter.STATE_OFF ){//蓝牙被关闭时强制打开
            mAdapter.enable();
            ToastUtil.showToast(this,"蓝牙被关闭请打开...");
        }else {
            PrintOrderDataMaker printOrderDataMaker = new PrintOrderDataMaker(this,"",
                    PrinterWriter58mm.TYPE_58, PrinterWriter.HEIGHT_PARTING_DEFAULT,printOrder);
            ArrayList<byte[]> printData = (ArrayList<byte[]>) printOrderDataMaker
                    .getPrintData(PrinterWriter58mm.TYPE_58);
            PrintQueue.getQueue(this).add(printData);
        }
    }

    private void submit() {

        final Info info = ((MyApplication) getApplication()).getInfo();
        final JSONArray arr = new JSONArray();
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        if (viewList.isEmpty() || viewList.size() == 0) {
            Toast.makeText(this, "请添加包裹", Toast.LENGTH_SHORT).show();
            return;
        }
//        for (Order order : viewList) {
        for (int i=0;i<viewList.size();i++){
            Order order=viewList.get(i);
            try {
                final JSONObject data = order.getData();
                if (data == null) {
                    scrollView.scrollTo(0, order.getTop());
                    return;
                }
                if (arr.length() == 0) {
                    data.put("id", this.order.getId());
                }
                arr.put(data);
                PrintOrder printOrder=new Gson().fromJson(data.toString(),PrintOrder.class);
                if (TextUtils.equals(this.order.getType(), "CityWide")) {
                    printOrder(printOrder);
                    continue;
                }
                final String path = order.getImgPath();
                Log.e("getTradeImg","submit"+this.order.getTradeImg());
                if ((!isHave)&&isEmpty(path)) {
                    Toast.makeText(this, i+"请选择物流快照"+printOrder.getEndHouseNumber(), Toast.LENGTH_SHORT).show();
                    scrollView.smoothScrollTo(0, (order.getTop()));
                    return;
                }
                printOrder.setUserChoiceCommpanyName(this.order.getUserChoiceCommpanyName());
                printOrder(printOrder);
                if (!isEmpty(path)){
                    final File file = new File(path);
                    fis = new FileInputStream(file);
                    baos = new ByteArrayOutputStream();
                    int len;
                    final byte[] buff = new byte[1024];
                    while ((len = fis.read(buff)) > 0) {
                        baos.write(buff, 0, len);
                        baos.flush();
                    }
                    final RequestBody fileData = RequestBody.create(MediaType.parse("application/octet-stream"),
                            baos.toByteArray());
                    requestBodyBuilder.addFormDataPart("pictures", file.getName(), fileData);
                    fis.close();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (baos != null) baos.close();
                } catch (IOException e) {

                }
            }
        }
        final AMapLocation aMapLocation=((MyApplication) getContext().getApplicationContext()).getLocation();
        System.out.println(arr.toString());
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", info.getId());
        params.put("orderStr", arr);
        params.put("type", order.getType());
        params.put("address",aMapLocation .getAddress());
        MessageDialog dialog = new MessageDialog(getContext());
        dialog.show();
        dialog.setMessage("确定取货吗?");
        dialog.setListener(new MessageDialog.DialogButtonClickListener() {
            @Override
            public void done(Dialog dialog) {
                String url = null;
                if (TextUtils.equals(order.getType(), "CityWide")) {
                    url = Config.Url.getUrl(Config.ORDERTOTALCITYWIDE);
                    newCall(url, params);
                } else {
//            requestBodyBuilder.addFormDataPart("userId", info.getId())
//                    .addFormDataPart("orderStr", arr.toString());
//            Request.Builder builder = new Request.Builder().url(Config.Url.getUrl(Config.ORDERTOTALINTERCITY))
//                    .method("POST", requestBodyBuilder.build());
//            new OkHttpClient().newCall(builder.build()).enqueue(new SimpleCallback(OrderDetailsActivity.this) {
//                @Override
//                public void onSuccess(String tag, JSONObject json) throws JSONException {
//                    Toast.makeText(OrderDetailsActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent();
//                    intent.putExtra("position", position);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
//                    sureBt.setEnabled(true);
//                }
//            });
                    url = Config.Url.getUrl(Config.ORDERTOTALINTERCITY);
                    requestBodyBuilder.addFormDataPart("userId", info.getId())
                            .addFormDataPart("address",aMapLocation .getAddress())
                            .addFormDataPart("orderStr", arr.toString());
                    Request.Builder builder = new Request.Builder().url(url).method("POST", requestBodyBuilder.build())
                            .tag(url.substring(Config.HOST.length(), url.length()));
                    newCall(builder.build());
                }
                sureBt.setEnabled(false);
                dialog.dismiss();
            }
            @Override
            public void cancel(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {

            case 102:
                PoiItem poiItem = data.getParcelableExtra("addr");
                if (tag != null && poiItem != null)
                    tag.setAddress(poiItem);
                break;
            case 103:
//                Bitmap bitmap = BitmapFactory.decodeFile(temPath);
                File cache = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".jpg");
                compressPicture(temPath,cache);

                tag.setImgPath(cache.getAbsolutePath());
                Log.e("什么鬼","");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {
        switch (tag.toString()) {
            case Config.ORDERDETAILS:
                order = new Gson().fromJson(json.getString("order"), OrderEntity.class);
                isHave = order.getTradeImg() != null;
                initViews();
                break;
            case Config.ORDERTOTALINTERCITY:
            case Config.ORDERTOTALCITYWIDE:
                order.setStatus("UnPayed");
                dbManagerOrder.insertSpecialOrder(order);
                Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);

                finish();
                break;
            case Config.AREAINFO:
                System.out.println(json);
                areaList = json.getJSONArray("tariff");
                JSONObject urgentObject=json.getJSONObject("urgent");
                urgentCost=urgentObject.getString("urgentCost");
                break;
        }
    }

    @Override
    public void fail(Object tag, int code, JSONObject json) throws JSONException {
        super.fail(tag, code, json);
        sureBt.setEnabled(true);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.gps_point);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", position);
        outState.putString("temPath", temPath);
        super.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        // latitude=29.589898#longitude=106.480093

        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (this.mapLocation == null) this.mapLocation = amapLocation;
            if (mListener != null) mListener.onLocationChanged(amapLocation);
            LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            if (!mFirstFix) {
                mFirstFix = true;
                addMarker(location);//添加定位图标
//                mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
            } else {
                mLocMarker.setPosition(location);
            }
//            changeCamera(
//                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                            new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 18, 30, 30)));
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
//            Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
        }
    }

    public void showAreaDialog(Order order, int selectPosition) {
        tag = order;
        if (dialog == null) {
//            if (TextUtils.equals(this.order.getType(), "CityWide")) {
                dialog = new AreaDialog(this, new AreaAdapter());
//            } else
//                dialog = new AreaDialog(this, new Adapter());
        }
        dialog.show(selectPosition);
    }

//    public void sao(Order order) {
//        this.tag = order;
//        startActivityForResult(new Intent(this, CaptureActivity.class), 99);
//    }

    public void localtion(Order order) {
        this.tag = order;
        Intent intent = new Intent(this, SearchAddressActivity.class);
        startActivityForResult(intent, 102);
    }

    class AreaDialog extends Dialog {
        private ListView listview;
        private BaseAdapter adapter;

        public AreaDialog(@NonNull Context context, BaseAdapter adapter) {
            super(context, R.style.DialogStyle);
            setContentView(R.layout.dialog_area);
            this.adapter = adapter;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            listview = (ListView) findViewById(R.id.dialog_area_listview);
            listview.setAdapter(adapter);
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(metrics.widthPixels / 3 * 2, metrics.heightPixels / 2);
            listview.setLayoutParams(lp);
            findViewById(R.id.dialog_area_done).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tag.setArea(area,areaList, listview.getCheckedItemPosition());
                            dismiss();
                        }
                    }
            );
        }

        public void show(int selectPosition) {
            show();
            listview.setItemChecked(selectPosition, true);
        }
    }

    class AreaAdapter extends BaseAdapter {

        private int pad;

        public AreaAdapter() {
            pad = DisplayUtil.dip2px(OrderDetailsActivity.this, 8);
        }

        @Override
        public int getCount() {
            return areaList == null ? 0 : areaList.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return areaList.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox area;
            if (convertView == null) {
                area = new CheckBox(OrderDetailsActivity.this);
                area.setPadding(pad, pad, pad, pad);
                area.setTextColor(Color.BLACK);
                area.setFocusableInTouchMode(false);
                area.setClickable(false);
                area.setFocusable(false);
                area.setButtonDrawable(null);
                area.setBackgroundResource(R.drawable.dialog_area_list_item_selector);
            } else area = (CheckBox) convertView;
            area.setTextColor(Color.BLACK);
            try {
                area.setText(areaList.getJSONObject(position).getString("endStreet"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return area;
        }

    }

    private void initOrder(){
        Order order = new Order(this, this.order,urgentCost,true);
        order.setTop(scrollView.getChildAt(0).getMeasuredHeight());
        orderlist.addView(order.getContent());
        viewList.add(order);
    }

    private void addOrder() {
        Log.e("getTradeImg add","add"+order.getTradeImg());
        OrderEntity orderEntity= null;
        try {
            orderEntity = (OrderEntity) order.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (!TextUtils.equals(orderEntity.getType(), "CityWide")) {
            orderEntity.setWeight(1);
        }else {
            orderEntity.setWeight(5);
        }
        Order order = new Order(this, orderEntity,urgentCost, false);
        order.setTop(scrollView.getChildAt(0).getMeasuredHeight());
        orderlist.addView(order.getContent());
        viewList.add(order);
        Log.e("getTradeImg add","add  2   "+orderEntity.getTradeImg());
    }

    public void cutImg(Order order) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(temPath);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(it, 103);
        this.tag = order;
    }

    public void deleteOrder(Order order) {
        if (order != null && viewList.contains(order)) {
            viewList.remove(order);
            orderlist.removeView(order.getContent());
        }
    }

    public void selectArea(Order order){
        tag=order;
        View view= LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.dialog_select_district,null);
        final android.app.Dialog selectAreaDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog).create();
        addressSelector = (AddressSelector) view.findViewById(R.id.address);
        addressSelector.setTabAmount(3);
        addressSelector.setTextEmptyColor(Color.parseColor("#333333"));
        addressSelector.setTextSelectedColor(R.color.blue);
        addressSelector.setListTextSelectedColor(R.color.colorPrimary);
        addressSelector.setCities(cities1);
        View delete = view.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAreaDialog.dismiss();
            }
        });
        selectAreaDialog.show();
        selectAreaDialog.setContentView(view);
        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(final AddressSelector addressSelector, CityInterface city, int tabPosition, int position) {
                switch (tabPosition) {
                    case 0:
                        try {
                            proObject = proArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", proObject.getString("id"))
                                    .addFormDataPart("cid", "").build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(OrderDetailsActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    cityArray = json.getJSONArray("City");
                                    for (int i = 0; i < cityArray.length(); i++) {
                                        JSONObject jsonObject = cityArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities2.add(city);
                                        addressSelector.setCities(cities2);
                                        Log.d("MyBaseAdapter", "区域=" + cities1);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities2 = new ArrayList<>();
                        break;
                    case 1:
                        try {
                            ctiyObject = cityArray.getJSONObject(position);
                            RequestBody requestBody2 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("pid", "")
                                    .addFormDataPart("cid", ctiyObject.getString("id")).build();
                            Request request = new Request.Builder().url(Config.Url.getUrl(Config.APPGETAREA)).post(requestBody2).build();
                            new OkHttpClient().newCall(request).enqueue(new SimpleCallback(OrderDetailsActivity.this) {
                                @Override
                                public void onSuccess(String tag, JSONObject json) throws JSONException {
                                    districtArray = json.getJSONArray("District");
                                    for (int i = 0; i < districtArray.length(); i++) {
                                        JSONObject jsonObject = districtArray.getJSONObject(i);
                                        City city = new City();
                                        city.setId(jsonObject.getString("id"));
                                        city.setName(jsonObject.getString("name"));
                                        cities3.add(city);
                                        addressSelector.setCities(cities3);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cities3 = new ArrayList<>();
                        break;
                    case 2:
                        try {
                            districtObject = districtArray.getJSONObject(position);
                            area.put("pro", proObject.getString("name"));
                            area.put("city", ctiyObject.getString("name"));
                            area.put("district", districtObject.getString("name"));
                            tag.setArea(area,areaList,position);
                            selectAreaDialog.dismiss();
//                            tag.setArea(area,areaList,0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()) {
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
    }

    public void setUrgentCostFQ(Order o){
        tag=o;
        tag.setUrgentCost(urgentCost);
    }

    /**
     *图片压缩
     * @param srcPath
     * @param desPath
     */
    public static void compressPicture(String srcPath, File desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 320f;//
        float ww = 320f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = (float) (w / ww);
        } else if (w < h && h > hh) {
            be = (float) (h / hh);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

