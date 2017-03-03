package com.mianan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.mianan.Self.SelfFragment;
import com.mianan.utils.BroadCast.FinishActivityRecever;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.SPUtils;
import com.mianan.utils.view.FragmentUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        doSomeWhenEnter();
        FragmentUtil.add(mFragmentManager, R.id.fragment, new SelfFragment());
        showSelf();
        FinishActivityRecever.sendFinishBroadcast(this);
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
    }

    private void showBT() {
        selfLay.setSelected(false);
        btLay.setSelected(true);
        shopLay.setSelected(false);
    }

    private void showShop() {
        selfLay.setSelected(false);
        btLay.setSelected(false);
        shopLay.setSelected(true);
    }

    private void doSomeWhenEnter() {
        SPUtils.put(getActivity(), SPUtils.IS_FIRS_COME, false);
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
}
