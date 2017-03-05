package com.mianan.BlueTooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.Friend;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.view.customView.SwitchView;

import java.util.ArrayList;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_bluetooth);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        for (int i = 1; i <= 2; i++) {
            Friend friend = new Friend();
            friend.setName("name" + i);
            connectedFriends.add(friend);
        }
        adapter = new ConnectedFriendAdapter(getContext(), 1, connectedFriends);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.right_icon)
    public void onClick() {
        startActivity(new Intent(getContext(), FriendActivity.class));
    }
}
