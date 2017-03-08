package com.mianan;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.mianan.BlueTooth.BlueToothFrag;
import com.mianan.NetWork.callBack.SimpleCallback;
import com.mianan.NetWork.netUtil.BTNetUtils;
import com.mianan.NetWork.netUtil.NormalKey;
import com.mianan.Self.SelfFragment;
import com.mianan.data.Record;
import com.mianan.service.MyService;
import com.mianan.utils.BroadCast.FinishActivityRecever;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.SPUtils;
import com.mianan.utils.normal.TimeUtils;
import com.mianan.utils.view.FragmentUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

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

    public long todayTotalTime;
    public String totalMark = "0";

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
        FragmentUtil.add(mFragmentManager, R.id.fragment, selfFragment);
        FragmentUtil.add(mFragmentManager, R.id.fragment, blueToothFrag);

        showBT();
        FinishActivityRecever.sendFinishBroadcast(this);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateTime();
        getTotalMark();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBinded) {
            unbindService(serviceConnection);
            isBinded = false;
        }
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
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
        blueToothFrag.setTodayTime(totalTime);
        selfFragment.setTodayTime(totalTime);
        todayTotalTime = totalTime;
    }


    private void getTotalMark() {
        BTNetUtils.getTodayMark(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
//                Calendar calendar = Calendar.getInstance();
//                String data = "" + calendar.get(Calendar.YEAR)
//                        + "-" + (calendar.get(Calendar.MONTH) + 1)
//                        + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                try {
                    totalMark = jsonObject.getJSONObject(NormalKey.content).getString(TimeUtils.getTodayDate());
                    selfFragment.setTodayMark(totalMark);
                    blueToothFrag.setTodayMark(totalMark);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String code, String msg) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
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
                .show(selfFragment)
                .commit();
    }

    private void showBT() {
        selfLay.setSelected(false);
        btLay.setSelected(true);
        shopLay.setSelected(false);
        mFragmentManager.beginTransaction()
                .hide(selfFragment)
                .show(blueToothFrag)
                .commit();
    }

    private void showShop() {
        selfLay.setSelected(false);
        btLay.setSelected(false);
        shopLay.setSelected(true);
    }

    private void doSomeWhenEnter() {
        SPUtils.put(getActivity(), SPUtils.IS_FIRS_COME, false);
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

    public MyService getMyService() {
        return myService;
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
