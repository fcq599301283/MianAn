package com.mianan.blueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.broadcastReciever.BTBroadcastReceiver;
import com.mianan.data.Friend;
import com.mianan.data.MarkAndTime;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.BTNetUtils;
import com.mianan.utils.BTUtils;
import com.mianan.utils.LinkService;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.customView.SwitchView;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class BlueToothFrag extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
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
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ConnectedFriendAdapter adapter;
    private List<Friend> connectedFriends = new ArrayList<>();

    private BTBroadcastReceiver.ScanModeChange scanModeChange;
    private MyHandler.OnStateChange onStateChange;
    private TempUser.onMarkChange onMarkChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_bluetooth);
        ButterKnife.bind(this, rootView);
        initView();
        registerObservers(true);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        registerObservers(false);
    }

    private void initButton() {
        if (BTUtils.bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            if (!openBluetooth.getState2()) {
                openBluetooth.setState(true);
            }
            startBTModel();
        } else {
            if (openBluetooth.getState2()) {
                openBluetooth.setState(false);
            }
            closeBTModel();
        }

        openSingleModel.setState(LinkService.getInstance().isSingleMode());
    }

    private boolean openBT() {
        if (BTUtils.bluetoothAdapter == null) {
            showNormalDialog("没有在你的设备上找到蓝牙");
            return false;
        }

        if (BTUtils.bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
            return false;
        }

        startBTModel();
        return true;

    }

    private void initView() {
        chronometer.setText(TempUser.getMarkAndTime().getTodayTime());
        todayGrade.setText("今日积分:" + TempUser.getMarkAndTime().getTodayMark() + "分");

        openBluetooth.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                openBluetooth.setState(openBT());
            }

            @Override
            public void toggleToOff() {
                closeBTModel();
                openBluetooth.setState(false);
            }
        });

        openSingleModel.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                if (MyHandler.getInstance().getCurrentState() == MyHandler.STATE_CONNECTED) {
                    final NormalDialog normalDialog = new NormalDialog(getContext());
                    normalDialog.content("你已与其他设备连接，开启单人模式会断开所有连接，是否继续?")
                            .btnText("是", "否")
                            .setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    openSingleModel.setState(true);
                                    startSingleModel();
                                    normalDialog.dismiss();
                                }
                            }, new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    normalDialog.dismiss();
                                    openSingleModel.setState(false);
                                }
                            });
                    normalDialog.show();
                } else {
                    startSingleModel();
                    openSingleModel.setState(true);
                }
            }

            @Override
            public void toggleToOff() {
                closeSingleModel();
                openSingleModel.setState(false);
            }
        });

        adapter = new ConnectedFriendAdapter(getContext(), 1, connectedFriends);
        listView.setAdapter(adapter);
        initButton();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        connectCount.setText("连接人数:" + connectedFriends.size() + "人");
    }

    private void registerObservers(boolean register) {

        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    todayGrade.setText("今日积分:" + markAndTime.getTodayMark() + "分");
                    chronometer.setText(markAndTime.getTodayTime());
                }
            };
        }

        if (scanModeChange == null) {
            scanModeChange = new BTBroadcastReceiver.ScanModeChange() {
                @Override
                public void onChange(final int mode) {
                    openBluetooth.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE == mode && BTUtils.bluetoothAdapter.isEnabled()) {
                                if (!openBluetooth.getState2()) {
                                    openBluetooth.setState(true);
                                    startBTModel();
                                }
                            } else {
                                if (openBluetooth.getState2()) {
                                    openBluetooth.setState(false);
                                    closeBTModel();
                                }
                            }
                        }
                    }, 300);
                }
            };
        }

        if (onStateChange == null) {
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
        }

        TempUser.registerOnMarkChangeObserver(onMarkChange, register);
        BTBroadcastReceiver.registerScanModeChange(scanModeChange, register);
        MyHandler.getInstance().register(onStateChange, register);

    }

    private void startBTModel() {
        LinkService.getInstance().setBTModel(true);
    }

    private void closeBTModel() {
        LinkService.getInstance().setBTModel(false);
    }

    private void startSingleModel() {
        LinkService.getInstance().setSingleMode(true);
    }

    private void closeSingleModel() {
        LinkService.getInstance().setSingleMode(false);
    }

    @OnClick(R.id.right_icon)
    public void onClick() {
        startActivity(new Intent(getContext(), FriendActivity.class));
    }

    @Override
    public void onRefresh() {
        BTNetUtils.refreshMarkAndTimeBack(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(String code, String msg) {
                swipeRefreshLayout.setRefreshing(false);
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                showToast("加载异常");
            }
        });
    }
}
