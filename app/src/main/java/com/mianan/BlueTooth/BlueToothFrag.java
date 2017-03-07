package com.mianan.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.mianan.BroadcastReciever.BTBroadcastReceiver;
import com.mianan.MainActivity;
import com.mianan.R;
import com.mianan.Self.TodayTimeFrag;
import com.mianan.data.Friend;
import com.mianan.data.Record;
import com.mianan.utils.BTUtils;
import com.mianan.utils.LinkService;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.customView.SwitchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class BlueToothFrag extends BaseFragment {
    @Bind(R.id.chronometer)
    Chronometer chronometer;
    @Bind(R.id.today_grade)
    TextView todayGrade;
    @Bind(R.id.connect_count)
    TextView connectCount;
    @Bind(R.id.openBluetooth)
    SwitchView openBluetooth;
    @Bind(R.id.openSingleModel)
    SwitchView openSingleModel;
    @Bind(R.id.listView)
    ListView listView;

    private ConnectedFriendAdapter adapter;
    private List<Friend> connectedFriends = new ArrayList<>();

    private BTBroadcastReceiver.ScanModeChange scanModeChange;
    private MyHandler.OnStateChange onStateChange;

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_bluetooth);
        ButterKnife.bind(this, rootView);
        mainActivity = (MainActivity) getActivity();
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        calculateTime();
        initButton();
    }

    private void initButton() {
        if (BTUtils.bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            openBluetooth.setState(true);
        } else {
            openBluetooth.setState(false);
        }
    }

    private boolean openBT() {
        if (BTUtils.bluetoothAdapter == null) {
            showNormalDialog("没有在你的设备上找到蓝牙");
            return false;
        }
//        if (!BTUtils.bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }

        if (BTUtils.bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
            return false;
        }

        LinkService.getInstance().reset();

        return true;

    }

    public void setTodayTime(long totalTime) {
        if (chronometer == null) {
            return;
        }
        if (totalTime == 0) {
            chronometer.setBase(SystemClock.elapsedRealtime());
        } else {
            chronometer.setBase(totalTime / 1000);
            chronometer.setText(TodayTimeFrag.FormatMiss(totalTime / 1000));
        }
    }

    public void setTodayMark(String mark) {
        if (todayGrade == null) {
            return;
        }
        todayGrade.setText("今日积分:" + mark + "分");
    }

    private void calculateTime() {
        Calendar calendar = Calendar.getInstance();
        String date = "" + calendar.get(Calendar.YEAR)
                + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("date", "" + date);
        List<Record> records = realm.where(Record.class).equalTo("date", Long.valueOf(date)).findAll();
        long totalTime = 0;
        for (int i = 0; i < records.size(); i++) {
            totalTime += records.get(i).getTotalTime();
        }
        Log.d("totalTime", "" + (totalTime / 1000));
        if (totalTime == 0) {
            chronometer.setBase(SystemClock.elapsedRealtime());
        } else {
            chronometer.setBase(totalTime / 1000);
            chronometer.setText(TodayTimeFrag.FormatMiss(totalTime / 1000));
        }

    }

    private void initView() {

        setTodayTime(mainActivity.todayTotalTime);

        openBluetooth.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                openBluetooth.setState(openBT());
            }

            @Override
            public void toggleToOff() {
                BTUtils.bluetoothAdapter.disable();
                openBluetooth.setState(false);
            }
        });

        scanModeChange = new BTBroadcastReceiver.ScanModeChange() {
            @Override
            public void onChange(final int mode) {
                Log.d("modeChange", "" + mode);
                openBluetooth.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE == mode && BTUtils.bluetoothAdapter.isEnabled()) {
                            if (!openBluetooth.getState2()) {
                                openBluetooth.setState(true);
                                Log.d("modeChange", "" + true);
                            }
                        } else {
                            if (openBluetooth.getState2()) {
                                openBluetooth.setState(false);
                                Log.d("modeChange", "" + false);
                            }
                        }
                    }
                }, 300);
            }
        };

        BTBroadcastReceiver.registerScanModeChange(scanModeChange, true);

        adapter = new ConnectedFriendAdapter(getContext(), 1, connectedFriends);
        listView.setAdapter(adapter);

        onStateChange = new MyHandler.OnStateChange() {
            @Override
            public void onChange(Message msg) {
                switch (msg.what) {
                    case MyHandler.STATE_CONNECTED:
                        BluetoothDevice device = (BluetoothDevice) msg.obj;
                        Friend friend = new Friend();
                        friend.setName(device.getName());
                        if (!connectedFriends.contains(friend)) {
                            connectedFriends.add(friend);
                            notifyDataSetChanged();
                        }
                        break;
                    case MyHandler.connectLose:
                        connectedFriends.clear();
                        notifyDataSetChanged();
                        break;
                }
            }
        };

        MyHandler.getInstance().register(onStateChange, true);
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        connectCount.setText("连接人数:" + connectedFriends.size() + "人");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        BTBroadcastReceiver.registerScanModeChange(scanModeChange, false);
    }

    @OnClick(R.id.right_icon)
    public void onClick() {
        startActivity(new Intent(getContext(), FriendActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
