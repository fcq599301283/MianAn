package com.mianan;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.mianan.blueTooth.BlueToothFrag;
import com.mianan.netWork.netUtil.BTNetUtils;
import com.mianan.self.SelfFragment;
import com.mianan.service.MyService;
import com.mianan.shop.ShopFragment;
import com.mianan.utils.TempUser;
import com.mianan.utils.TimeCount;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.broadCast.FinishActivityRecever;
import com.mianan.utils.normal.SPUtils;
import com.mianan.utils.view.FragmentUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        FinishActivityRecever.sendFinishBroadcast(this);

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
        selfLay.setSelected(false);
        btLay.setSelected(true);
        shopLay.setSelected(false);
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
        CrashReport.setUserId(TempUser.getAccount());
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

}
