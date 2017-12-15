package com.android.slowlifecourier.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.adapter.Baseadapter;
import com.android.slowlifecourier.view.PagerSlidingTabStrip;
import com.sgrape.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class SearchAddressActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        PoiSearch.OnPoiSearchListener, TextWatcher, LocationSource,
        AMapLocationListener {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.pager_tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.addr_pager)
    ViewPager pager;
    @BindView(R.id.addr_search)
    EditText search;
    PoiSearch poiSearch;
    private PopupWindow win;
    private ListView winListView;
    private Baseadapter<PoiItem> winAdapter;
    private AMap aMap;
    private PagerAdapter pagerAdapter;
    private AMapLocation location;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.layout_default)
    View defView;
    private Baseadapter<PoiItem> adapter;
    private Object object = new Object();
    private UiSettings uiSettings;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private ImageView lockView;
    private LatLng latLng;
    private String city = "";
    public final static String CITY_NAME = "cityname";


    /**
     * 初始化控件
     */
    protected void init() {
        super.init();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabs.setViewPager(pager);
        search.addTextChangedListener(this);
        listView.setOnItemClickListener(this);

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setHttpTimeOut(8000);
        mLocationOption.setOnceLocation(true);
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
        city = getIntent().getStringExtra(CITY_NAME);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_address;
    }

    public void finish(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onChanged(AMapLocation location) {
        this.location = location;
        pagerAdapter.getItem(pager.getCurrentItem()).loadData();
        search();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("addr", adapter.getData().get(position));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSuccess(Object tag, JSONObject json) throws JSONException {

    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.gps_localtion);
//            int size = DisplayUtil.dip2px(this, 5);
//            bMap = Bitmap.createScaledBitmap(bMap, size, size, true);
            BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
            MarkerOptions options = new MarkerOptions();
            options.icon(des);
            options.anchor(0.5f, 0.5f);
            options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
            aMap.addMarker(options);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 18));
            latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            Frag frag = pagerAdapter.getItem(pager.getCurrentItem());
            frag.loadData();
        } else {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
            String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            System.out.println(errText);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }


    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Frag getItem(int position) {
            return frags[position];
        }

        Frag[] frags = new Frag[]{
                new Frag(""),
                new Frag("写字楼|楼宇|建筑"),
                new Frag("小区|住宅"),
                new Frag("学校|高校")
        };
        String[] titles = new String[]{
                "全部", "写字楼", "小区", "学校"
        };
    }


    @SuppressLint("ValidFragment")
    public static class Frag extends ListFragment implements PoiSearch.OnPoiSearchListener {
        PoiSearch poiSearch;
        private PoiSearch.Query query;
        Adapter adapter;
        String arg;
        LatLng point;

        public Frag() {
            super();
        }

        public Frag(String arg) {
            this();
            Bundle bundle = new Bundle();
            bundle.putString("param", arg);
            setArguments(bundle);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            loadData();
        }

        public void loadData() {
            SearchAddressActivity act = (SearchAddressActivity) getActivity();
            if (point == null || (point.latitude != act.latLng.latitude
                    && point.longitude != act.latLng.longitude)) {

            }
            arg = getArguments().getString("param");
            if (!getUserVisibleHint() || getView() == null)
                return;
            if (adapter == null) {
                adapter = new Adapter(getView().getContext(), null);
                setListAdapter(adapter);
            }
            search();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            SearchAddressActivity act = (SearchAddressActivity) getActivity();
            Intent intent = new Intent();
            intent.putExtra("addr", adapter.getItem(position));
            act.finish(intent);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (adapter == null || adapter.getCount() == 0) loadData();
        }

        public void search() {
            point = ((SearchAddressActivity) getActivity()).latLng;
            System.out.println(point);
            if (point == null) return;
            query = new PoiSearch.Query(arg, "", "");
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);// 设置查第一页
            poiSearch = new PoiSearch(getContext(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步搜索
            // 设置搜索区域为以lp点为圆心，其周围3000米范围
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(point.latitude,
                    point.longitude), 5000));//设置周边搜索的中心点以及半径
        }

        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
            adapter.notifyDataSetChanged(poiResult.getPois());
            getArguments().putParcelableArrayList("pois", poiResult.getPois());
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }

    }


    static class Adapter extends Baseadapter<PoiItem> {

        public Adapter(Context context, List<PoiItem> list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        protected View getView(View view, int position) {
            ViewHolder vh = (ViewHolder) view.getTag();
            if (vh == null) {
                vh = new ViewHolder(view);
                view.setTag(vh);
            }
            if (vh.local.getVisibility() == View.VISIBLE)
                vh.local.setVisibility(View.GONE);
            PoiItem item = list.get(position);
            vh.describe.setText(String.format("%s%s%s", item.getCityName(), item.getDirection(), item.getSnippet()));
            vh.addr.setText(item.getTitle());
            return view;
        }

        @Override
        protected int getLayoutId(int position) {
            return R.layout.item_addr;
        }
    }

    static class ViewHolder {
        @BindView(R.id.local)
        View local;
        @BindView(R.id.address)
        TextView addr;
        @BindView(R.id.addr_describe)
        TextView describe;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    /**
     * poisearch popupwindow
     */
    private void showPoiPopWin(final List<PoiItem> pois) {
        if (win == null) {
            win = new PopupWindow(this);
            winListView = new ListView(this);
            final int itemPad = DisplayUtil.dip2px(this, 5);
            winAdapter = new Baseadapter<PoiItem>(this, pois) {

                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = new TextView(SearchAddressActivity.this);
                        convertView.setPadding(itemPad, itemPad, itemPad, itemPad);
                        convertView.setBackgroundColor(Color.WHITE);
                    }
                    TextView tv = (TextView) convertView;
                    PoiItem poi = list.get(position);
                    tv.setText(poi.getTitle());
                    return tv;
                }

                @Override
                protected View getView(View view, int position) {
                    return null;
                }

                @Override
                protected int getLayoutId(int position) {
                    return 0;
                }
            };
            winListView.setAdapter(winAdapter);
            win.setContentView(winListView);
            win.setWidth(search.getMeasuredWidth());
            win.setHeight(getResources().getDisplayMetrics().heightPixels / 3);
            win.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            win.setOutsideTouchable(true);
            win.setFocusable(true);
            win.setTouchable(true);
            winListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("addr", pois.get(position));
                    setResult(Activity.RESULT_OK, intent);
                    win.dismiss();
                }
            });
            win.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE
                            || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        win.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            winAdapter.notifyDataSetChanged(pois);
        }
        win.showAsDropDown(search);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onCreate(null);
        if (aMap == null) {
            if (mapView != null) {
                aMap = mapView.getMap();
                uiSettings = aMap.getUiSettings();
                uiSettings.setScaleControlsEnabled(true);
            }
        }
        mapView.onResume();
        if (lockView == null) {
            lockView = new ImageView(this);
            Bitmap ic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_lock);
            lockView.setImageBitmap(ic);
            ViewGroup content = ((ViewGroup) findViewById(android.R.id.content));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
            lp.topMargin = (DisplayUtil.dip2px(this, 220) - ic.getHeight()) / 2 + DisplayUtil.dip2px(this, 45);
            lp.leftMargin = (getResources().getDisplayMetrics().widthPixels - ic.getWidth()) / 2;
            content.addView(lockView, lp);
        }
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        latLng = aMap.getProjection().fromScreenLocation(new Point((int) ev.getX(), (int) ev.getY()));
                        Frag frag = pagerAdapter.getItem(pager.getCurrentItem());
                        frag.loadData();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }


    private void search() {
        synchronized (object) {
            String keyWord = search.getText().toString().trim();
            if (isEmpty(keyWord)) return;
            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);// 设置查第一页
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步搜索
            // 设置搜索区域为以lp点为圆心，其周围3000米范围
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(location.getLatitude(),
//                location.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> list = poiResult.getPois();
        if (adapter == null) {
            adapter = new Adapter(this, list);
            listView.setAdapter(adapter);
        } else adapter.notifyDataSetChanged(list);
        if (listView.getVisibility() != View.VISIBLE) {
            listView.setVisibility(View.VISIBLE);
            defView.setVisibility(View.GONE);
            lockView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        search();
    }

}
