package com.android.slowlifecourier.bluetoothprint;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlifecourier.R;
import com.android.slowlifecourier.bluetoothprint.base.AppInfo;
import com.android.slowlifecourier.bluetoothprint.bt.BluetoothActivity;
import com.android.slowlifecourier.bluetoothprint.bt.BtUtil;
import com.android.slowlifecourier.bluetoothprint.print.PrintQueue;
import com.android.slowlifecourier.bluetoothprint.print.PrintUtil;
import com.android.slowlifecourier.bluetoothprint.util.ToastUtil;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 蓝牙搜索界面
 * Created by liuguirong on 2017/8/3.
 */

public class SearchBluetoothActivity extends BluetoothActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.back_rl)
    RelativeLayout backRl;
    private BluetoothAdapter bluetoothAdapter;
    private ListView lv_searchblt;
    private TextView tv_title;
    private TextView tv_summary;
    private SearchBleAdapter searchBleAdapter;
    int PERMISSION_REQUEST_COARSE_LOCATION = 2;
    private static final int REQUEST_ENABLE_BT = 2;
    private static String PERMISSIONS[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbooth);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if(lacksPermissions(PERMISSIONS))
                ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSIONS.length);
            else
                setResult(PERMISSIONS.length);
        }

        lv_searchblt = (ListView) findViewById(R.id.lv_searchblt);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_summary = (TextView) findViewById(R.id.tv_summary);
        //初始化蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        searchBleAdapter = new SearchBleAdapter(SearchBluetoothActivity.this, null);
        lv_searchblt.setAdapter(searchBleAdapter);
        init();
//        searchDeviceOrOpenBluetooth();
        lv_searchblt.setOnItemClickListener(this);
        tv_title.setOnClickListener(this);
        tv_summary.setOnClickListener(this);
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)// 判断是否缺少权限
                return true;
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean bool = false;
        for(int result : grantResults)
            if(result != PackageManager.PERMISSION_GRANTED) {
                bool = true;
                break;
            }
        if(requestCode == permissions.length && bool){
            Log.d("AdActivity", "--onRequestPermissionsResult>>>权限没开完");
            Toast.makeText(this, "请开启必要的权限", Toast.LENGTH_SHORT).show();
            finish();
        }else setResult(PERMISSIONS.length);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void init() {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            tv_title.setText("未连接蓝牙打印机");
            tv_summary.setText("系统蓝牙已关闭,请开启");

        } else {
            if (!PrintUtil.isBondPrinter(this, bluetoothAdapter)) {
                //未绑定蓝牙打印机器
                tv_title.setText("未连接蓝牙打印机");
                tv_summary.setText("点击后搜索蓝牙打印机");

            } else {
                //已绑定蓝牙设备
                tv_title.setText(getPrinterName());
                String blueAddress = "点击后搜索蓝牙打印机";
                tv_summary.setText(blueAddress);
            }
        }
    }

    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {//蓝牙被关闭时强制打开
            bluetoothAdapter.enable();
        }
        if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {//蓝牙打开时搜索蓝牙
            searchDeviceOrOpenBluetooth();
        }
    }

    private String getPrinterName() {
        String dName = PrintUtil.getDefaultBluetoothDeviceName(this);
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        if (TextUtils.isEmpty(AppInfo.btAddress))
            dName = "未连接";
        return dName;
    }

    private String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "未知设备";
        }
        return dName;
    }

    /**
     * 开始搜索
     * search device
     */
    private void searchDeviceOrOpenBluetooth() {
        if (BtUtil.isOpen(bluetoothAdapter)) {
            BtUtil.searchDevices(bluetoothAdapter);
        }
    }

    /**
     * 关闭搜索
     * cancel search
     */
    @Override
    protected void onStop() {
        super.onStop();
        BtUtil.cancelDiscovery(bluetoothAdapter);
    }

    @Override
    public void btStartDiscovery(Intent intent) {
        tv_title.setText("正在搜索蓝牙设备…");
        tv_summary.setText("");
    }

    @Override
    public void btFinishDiscovery(Intent intent) {
        tv_title.setText("搜索完成");
        tv_summary.setText("点击重新搜索");
    }

    @Override
    public void btFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.d("1", "!");
        if (null != bluetoothAdapter && device != null) {
            searchBleAdapter.addDevices(device);
            String dName = device.getName() == null ? "未知设备" : device.getName();
            Log.d("未知设备", dName);
            Log.d("1", "!");
        }
    }

    @Override
    public void btBondStatusChange(Intent intent) {
        super.btBondStatusChange(intent);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDING://正在配对
                Log.d("BlueToothTestActivity", "正在配对......");
                break;
            case BluetoothDevice.BOND_BONDED://配对结束
                Log.d("BlueToothTestActivity", "完成配对");
                connectBlt(device);
                break;
            case BluetoothDevice.BOND_NONE://取消配对/未配对
                Log.d("BlueToothTestActivity", "取消配对");
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (null == searchBleAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = searchBleAdapter.getItem(position);
        if (null == bluetoothDevice) {
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("绑定" + getPrinterName(bluetoothDevice.getName()) + "?")
                .setMessage("点击确认绑定蓝牙设备")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BtUtil.cancelDiscovery(bluetoothAdapter);


                            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                                connectBlt(bluetoothDevice);
                            } else {
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                createBondMethod.invoke(bluetoothDevice);
                            }
                            PrintQueue.getQueue(getApplicationContext()).disconnect();
                            String name = bluetoothDevice.getName();
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
                            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), "");
                            ToastUtil.showToast(SearchBluetoothActivity.this, "蓝牙绑定失败,请重试");
                        }
                    }
                })
                .create()
                .show();


    }

    /***
     * 配对成功连接蓝牙
     * @param bluetoothDevice
     */

    private void connectBlt(BluetoothDevice bluetoothDevice) {
        if (null != searchBleAdapter) {
            searchBleAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
        }
        init();
        searchBleAdapter.notifyDataSetChanged();
        PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
        PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:

                break;
            case R.id.tv_summary:

                searchDeviceOrOpenBluetooth();
                break;
        }
    }

    @OnClick(R.id.back_rl)
    public void onViewClicked() {
        finish();
    }
}
