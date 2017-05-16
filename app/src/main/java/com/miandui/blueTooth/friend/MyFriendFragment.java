package com.miandui.blueTooth.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miandui.R;
import com.miandui.data.Friend;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netCollection.FriendNet;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.view.customView.ClearableEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/5/16
 */

public class MyFriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.serchText)
    ClearableEditText serchText;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FriendAdapter friendAdapter;
    private ArrayList<Friend> friends = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.activity_my_friend);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        friendAdapter = new FriendAdapter(getContext(), R.layout.item_friend, friends);
        listView.setAdapter(friendAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        getData();
    }

    @Override
    public void hideLoadingDialog() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingDialog(String msg) {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        FriendNet.getFriend(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Gson gson = new Gson();
                    friends = gson.fromJson(jsonObject.getString(NormalKey.content),
                            new TypeToken<List<Friend>>() {
                            }.getType());
                    if (friends != null && !friends.isEmpty()) {
                        initListView(friends);
                    } else {
                        initListView(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String code, String msg) {
                if ("201".equals(code)) {
                    initListView(null);
                } else {
                    super.onFail(code, msg);
                }
            }
        });
    }


    private void initListView(ArrayList<Friend> friends) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friendAdapter = new FriendAdapter(getContext(), R.layout.item_new_friend, friends);
        listView.setAdapter(friendAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
