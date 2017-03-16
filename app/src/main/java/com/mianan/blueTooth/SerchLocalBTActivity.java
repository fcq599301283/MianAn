package com.mianan.blueTooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ListView;

import com.mianan.R;
import com.mianan.utils.BTUtils;
import com.mianan.utils.IntentUtils;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class SerchLocalBTActivity extends BaseActivity {
    @Bind(R.id.serch)
    Button serch;
    @Bind(R.id.listView)
    ListView listView;

    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private DeviceAdpter deviceAdpter;

    private BluetoothAdapter bluetoothAdapter;

    private MyHandler.OnStateChange onStateChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch);
        ButterKnife.bind(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initView();
    }

    private void initView() {
        if (!bluetoothAdapter.isEnabled()) {
            showNormalDialog("请开启蓝牙先");
        }


        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        deviceAdpter = new DeviceAdpter(getActivity(), 1, bluetoothDevices);
        listView.setAdapter(deviceAdpter);

        onStateChange = new NormalCallback(this);

        MyHandler.getInstance().register(onStateChange, true);

        checkPermission();
    }

    private void serch() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            serch.setText("开始搜索");
        } else {
            bluetoothAdapter.startDiscovery();
            serch.setText("正在搜索,点击立刻停止");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);

        MyHandler.getInstance().register(onStateChange, false);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //TODO 提示权限已经被禁用 且不在提示
                IntentUtils.getAppDetailSettingIntent(getActivity(), "我们需要位置权限才能进行搜索，\n请在设置界面授权");
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, BTUtils.REQUEST_ENABLE_BT);
        }
    }

    @OnClick(R.id.serch)
    public void onClick() {
        serch();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                    deviceAdpter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                serch.setText("开始搜索");
                ToastUtils.showShort(getActivity(), "搜索完毕");
            }
        }
    };

}
