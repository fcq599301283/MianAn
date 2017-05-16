package com.miandui.blueTooth;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.miandui.R;
import com.miandui.blueTooth.friend.FriendActivity;
import com.miandui.broadcastReciever.BTBroadcastReceiver;
import com.miandui.data.Friend;
import com.miandui.data.MarkAndTime;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.netUtil.BTNetUtils;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.BTUtils;
import com.miandui.utils.IntentUtils;
import com.miandui.utils.LinkService;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.runtimePermission.AndPermission;
import com.miandui.utils.runtimePermission.CheckPermission;
import com.miandui.utils.runtimePermission.PermissionNo;
import com.miandui.utils.runtimePermission.PermissionYes;
import com.miandui.utils.runtimePermission.Rationale;
import com.miandui.utils.runtimePermission.RationaleListener;
import com.miandui.utils.view.NoramlTitleUtils;
import com.miandui.utils.view.customView.SwitchView;

import org.json.JSONException;
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
    @Bind(R.id.refreshLocalDevice)
    TextView refreshLocalDevice;
    @Bind(R.id.refreshIcon)
    ImageView refreshIcon;
    @Bind(R.id.title_image)
    ImageView titleImage;

    private RoundDeviceAdapter deviceAdapter;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private List<Friend> connectedFriends = new ArrayList<>();
    private List<BluetoothDevice> connectedDevices = new ArrayList<>();

    private BTBroadcastReceiver.ScanModeChange scanModeChange;
    private MyHandler.OnStateChange onStateChange;
    private TempUser.onMarkChange onMarkChange;

    private Animation rotate;

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
        if (refreshIcon != null) {
            refreshIcon.clearAnimation();
        }
    }

    private void initButton() {
        if (BTUtils.bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            if (!openBluetooth.getState2()) {
                openBluetooth.setState(true);
                tryToGetRoundDevice();
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

        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        rotate.setInterpolator(new LinearInterpolator());

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

        deviceAdapter = new RoundDeviceAdapter(getContext(), 1, bluetoothDevices, connectedDevices);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MyHandler.getInstance().getCurrentState() == MyHandler.STATE_CONNECTED) {
                    final NormalDialog normalDialog = new NormalDialog(getContext());
                    normalDialog.content("你已经连接了一个设备,是否断开?")
                            .btnText("是", "否")
                            .setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    LinkService.getInstance().setBTModel(true);
                                    normalDialog.dismiss();
                                }
                            }, new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    normalDialog.dismiss();
                                }
                            });
                    normalDialog.show();
                } else {
                    LinkService.getInstance().Connect(bluetoothDevices.get(position), getContext());
                }
            }
        });

        initButton();
        swipeRefreshLayout.setOnRefreshListener(this);

        NoramlTitleUtils.buildNormalPOpmenu(titleImage);
    }

    private void showSignDialog(JSONObject jsonObject) throws JSONException {
        String date = jsonObject.getJSONObject(NormalKey.content).getString(NormalKey.day_sign_in);
        String mark = jsonObject.getJSONObject(NormalKey.content).getString(NormalKey.mark_sign_in);

        final Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialog);
        View view = View.inflate(getActivity(), R.layout.dialog_sign_in, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.data.setText("连续签到:" + date + "天");
        viewHolder.mark.setText("签到获得积分:" + mark + "分");
        viewHolder.ticketMark.setText(mark + "积分");
        viewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        viewHolder.gainMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void notifyDataSetChanged() {
        deviceAdapter.notifyDataSetChanged();
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
            onStateChange = new NormalCallback(getBaseView()) {
                @Override
                public void onChange(Message msg) {
                    super.onChange(msg);
                    switch (msg.what) {
                        case MyHandler.STATE_CONNECTED:
                            BluetoothDevice device = (BluetoothDevice) msg.obj;
                            if (!connectedDevices.contains(device)) {
                                connectedDevices.add(device);
                                notifyDataSetChanged();
                            }
                            break;
                        case MyHandler.connectLose:
                            connectedDevices.clear();
                            notifyDataSetChanged();
                            break;
                    }
                }
            };
//            onStateChange = new MyHandler.OnStateChange() {
//                @Override
//                public void onChange(Message msg) {
//                    switch (msg.what) {
//                        case MyHandler.STATE_CONNECTED:
//                            BluetoothDevice device = (BluetoothDevice) msg.obj;
//
//                            if (!connectedDevices.contains(device)) {
//                                connectedDevices.add(device);
//                                notifyDataSetChanged();
//                            }
//                            break;
//                        case MyHandler.connectLose:
//                            connectedDevices.clear();
//                            notifyDataSetChanged();
//                            break;
//                    }
//                }
//            };
        }

        TempUser.registerOnMarkChangeObserver(onMarkChange, register);
        BTBroadcastReceiver.registerScanModeChange(scanModeChange, register);
        MyHandler.getInstance().register(onStateChange, register);

        if (register) {
            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(mReceiver, filter);
        } else {
            // Unregister broadcast listeners
            getActivity().unregisterReceiver(mReceiver);
        }

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

    private void tryToGetRoundDevice() {
        if (AndPermission.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                getRoundDevice();
            } else {
                showPermissionRejectedDialog();
            }
        } else {
            requestBasicPermission();
        }
    }

    private void getRoundDevice() {
        if (!BTUtils.bluetoothAdapter.isEnabled()) {
            showNormalDialog("请开启蓝牙");
            return;
        }
        if (BTUtils.bluetoothAdapter.isDiscovering()) {
            showToast("正在刷新");
            return;
        }
        BTUtils.bluetoothAdapter.startDiscovery();
        refreshLocalDevice.setText("正在刷新");
        refreshIcon.startAnimation(rotate);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                refreshLocalDevice.setText("刷新");
                if (rotate != null) {
                    refreshIcon.clearAnimation();
                }
            }
        }
    };

    private void requestBasicPermission() {
        AndPermission.with(getActivity())
                .requestCode(IntentUtils.REQUEST_LOCATION_PERMISSION)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(getActivity(), rationale).show();
                    }
                })
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //申请权限成功 检查权限
    @PermissionYes(IntentUtils.REQUEST_LOCATION_PERMISSION)
    private void getBasicGrant(List<String> grantedPermissions) {
        if (!CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                || !CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            showPermissionRejectedDialog();
        } else {
            getRoundDevice();
        }
    }

    //申请权限失败
    @PermissionNo(IntentUtils.REQUEST_LOCATION_PERMISSION)
    private void getBasicDenine(List<String> deniedPermissions) {
        showToast("申请权限被拒绝");
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            showPermissionRejectedDialog();
        } else {
            requestBasicPermission();
        }
    }

    private void showPermissionRejectedDialog() {
        AndPermission.defaultSettingDialog(this, IntentUtils.REQUEST_LOCATION_PERMISSION)
                .setTitle("申请权限失败")
                .setMessage("需要位置权限才能搜索附近的蓝牙,请在设置页面的权限管理中授权，否则该功能无法使用.")
                .setPositiveButton("确定")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
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

    @OnClick({R.id.right_icon, R.id.signIn, R.id.refreshLocalDevice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_icon:
                startActivity(new Intent(getContext(), FriendActivity.class));
                break;
            case R.id.signIn:
                signIn();
                break;
            case R.id.refreshLocalDevice:
//                getRoundDevice();
                tryToGetRoundDevice();
                break;
        }
    }

    private void signIn() {
        BTNetUtils.signIn(new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    showSignDialog(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析数据异常");
                }
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.close)
        ImageView close;
        @Bind(R.id.data)
        TextView data;
        @Bind(R.id.mark)
        TextView mark;
        @Bind(R.id.ticketMark)
        TextView ticketMark;
        @Bind(R.id.ticketLay)
        FrameLayout ticketLay;
        @Bind(R.id.rule)
        TextView rule;
        @Bind(R.id.divider)
        ImageView divider;
        @Bind(R.id.gainMark)
        TextView gainMark;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
