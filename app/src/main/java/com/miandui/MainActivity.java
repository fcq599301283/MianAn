package com.miandui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.miandui.blueTooth.BlueToothFrag;
import com.miandui.netWork.netUtil.BTNetUtils;
import com.miandui.self.SelfFragment;
import com.miandui.service.MyService;
import com.miandui.shop.ShopFragment;
import com.miandui.utils.IntentUtils;
import com.miandui.utils.TempUser;
import com.miandui.utils.TimeCount;
import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.broadCast.FinishActivityReceiver;
import com.miandui.utils.normal.SPUtils;
import com.miandui.utils.runtimePermission.AndPermission;
import com.miandui.utils.runtimePermission.CheckPermission;
import com.miandui.utils.runtimePermission.PermissionNo;
import com.miandui.utils.runtimePermission.PermissionYes;
import com.miandui.utils.runtimePermission.Rationale;
import com.miandui.utils.runtimePermission.RationaleListener;
import com.miandui.utils.view.FragmentUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * modified by FengChaoQun on 2017/5/8 16:24
 * description:优化代码
 * 主界面 管理三个fragment
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.fragment)
    FrameLayout fragment;
    @Bind(R.id.bt_lay)
    FrameLayout btLay;
    @Bind(R.id.shop_lay)
    FrameLayout shopLay;
    @Bind(R.id.self_lay)
    FrameLayout selfLay;

    private SelfFragment selfFragment;
    private BlueToothFrag blueToothFrag;
    private ShopFragment shopFragment;

    private MyService myService;
    private boolean isBinded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        doSomeWhenEnter();
        selfFragment = new SelfFragment();
        blueToothFrag = new BlueToothFrag();
        shopFragment = new ShopFragment();
        FragmentUtil.add(mFragmentManager, R.id.fragment, selfFragment);
        FragmentUtil.add(mFragmentManager, R.id.fragment, blueToothFrag);
        FragmentUtil.add(mFragmentManager, R.id.fragment, shopFragment);

        showBT();

        FinishActivityReceiver.sendFinishBroadcast(this);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        BTNetUtils.refreshMarkAndTimeBack(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinded) {
            unbindService(serviceConnection);
            isBinded = false;
        }
        stopService(new Intent(this, MyService.class));
        TimeCount.getInstance().endRecord();
    }

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    private void showSelf() {
        selfLay.setSelected(true);
        btLay.setSelected(false);
        shopLay.setSelected(false);
        mFragmentManager.beginTransaction()
                .hide(blueToothFrag)
                .hide(shopFragment)
                .show(selfFragment)
                .commit();
    }

    private void showBT() {
        btLay.setSelected(true);
        shopLay.setSelected(false);
        selfLay.setSelected(false);
        mFragmentManager.beginTransaction()
                .hide(selfFragment)
                .hide(shopFragment)
                .show(blueToothFrag)
                .commit();
    }

    private void showShop() {
        mFragmentManager.beginTransaction()
                .hide(selfFragment)
                .hide(blueToothFrag)
                .show(shopFragment)
                .commit();
        selfLay.setSelected(false);
        btLay.setSelected(false);
        shopLay.setSelected(true);
    }

    private void doSomeWhenEnter() {
        SPUtils.put(getActivity(), SPUtils.IS_FIRS_COME, false);
        SPUtils.put(getActivity(), SPUtils.IS_QUIT, false);
        CrashReport.setUserId(TempUser.getAccount());
        requestBasicPermission();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.bt_lay, R.id.shop_lay, R.id.self_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_lay:
                showBT();
                break;
            case R.id.shop_lay:
                showShop();
                break;
            case R.id.self_lay:
                showSelf();
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder myBinder = (MyService.MyBinder) service;
            myService = myBinder.getMyService();
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    //获取APP运行的基本权限
    private void requestBasicPermission() {
        AndPermission.with(getActivity())
                .requestCode(IntentUtils.GET_BASIC_PERMISSION)
                .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_PHONE_STATE)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(getActivity(), rationale).show();
                    }
                })
                .send();
    }

    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //申请位置权限成功 检查权限
    @PermissionYes(IntentUtils.REQUEST_LOCATION_PERMISSION)
    private void getLocationGrant(List<String> grantedPermissions) {
        if (!CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                || !CheckPermission.isGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (blueToothFrag != null) {
                blueToothFrag.showLocationRejectedDialog();
            }
        } else {
            if (blueToothFrag != null) {
                blueToothFrag.getRoundDevice();
            }
        }
    }

    //申请位置权限失败
    @PermissionNo(IntentUtils.REQUEST_LOCATION_PERMISSION)
    private void getLocationcDenine(List<String> deniedPermissions) {
        showToast("申请权限被拒绝");
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            if (blueToothFrag != null) {
                blueToothFrag.showLocationRejectedDialog();
            }
        } else {
            if (blueToothFrag != null) {
                blueToothFrag.requestLocationPermission();
            }
        }
    }


    //申请基础权限成功 检查权限
    @PermissionYes(IntentUtils.GET_BASIC_PERMISSION)
    private void getBasicGrant(List<String> grantedPermissions) {
        if (!CheckPermission.isGranted(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !CheckPermission.isGranted(getActivity(), android.Manifest.permission.READ_PHONE_STATE)) {
            showPermissionRejectedDialog();
        }
    }

    //申请基础权限失败
    @PermissionNo(IntentUtils.GET_BASIC_PERMISSION)
    private void getBasicDenine(List<String> deniedPermissions) {
        showToast("获取基本权限失败!");
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            showPermissionRejectedDialog();
        } else {
            requestBasicPermission();
        }
    }

    private void showPermissionRejectedDialog() {
        AndPermission.defaultSettingDialog(this, IntentUtils.GET_BASIC_PERMISSION)
                .setTitle("权限申请失败")
                .setMessage("我们需要了解你手机的状态并存储一些必要的信息,请在设置界面的权限管理中开启,否则无法使用本软件.")
                .setPositiveButton("好,去设置")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().exit();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentUtils.GET_BASIC_PERMISSION) {
            requestBasicPermission();
        }
    }

}
