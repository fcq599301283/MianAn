package com.mianan.blueTooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mianan.R;
import com.mianan.data.Friend;
import com.mianan.utils.BTUtils;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.view.customView.ClearableEditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class FriendActivity extends BaseActivity {
    @Bind(R.id.serchText)
    ClearableEditText serchText;
    @Bind(R.id.listView)
    ListView listView;

    private FriendAdapter adapter;
    private List<Friend> friends = new ArrayList<>();
    private MyHandler.OnStateChange onStateChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyHandler.getInstance().register(onStateChange, false);
    }

    private void initView() {
        if (!BTUtils.bluetoothAdapter.isEnabled()) {
            showNormalDialog("请开启蓝牙先");
            return;
        }
        Set<BluetoothDevice> pairedDevices = BTUtils.bluetoothAdapter.getBondedDevices();

        Iterator<BluetoothDevice> it = pairedDevices.iterator();
        while (it.hasNext()) {
            Friend friend = new Friend();
            BluetoothDevice bluetoothDevice = it.next();
            friend.setName(bluetoothDevice.getName());
            friend.setDevice(bluetoothDevice);
            friends.add(friend);
        }

        adapter = new FriendAdapter(this, 1, friends);
        listView.setAdapter(adapter);

        onStateChange = new NormalCallback(this);

        MyHandler.getInstance().register(onStateChange, true);
    }

    @OnClick({R.id.title_image, R.id.right_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_image:
                finish();
                break;
            case R.id.right_icon:
                Intent serchIntent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(serchIntent);
                break;
        }
    }
}
