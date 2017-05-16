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
import com.miandui.utils.view.LoadingDialog;

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

public class NewFriendFragemnt extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewFriendAdapter adapter;
    private ArrayList<Friend> friends = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.fragment_new_friend);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new NewFriendAdapter(getContext(), R.layout.item_new_friend, this, friends);
        listView.setAdapter(adapter);
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
    public void setOnLoadingDialogDiamiss(LoadingDialog.onDismiss on) {

    }

    @Override
    public void onRefresh() {
        getData();
    }

    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("identification_receiver", TempUser.getAccount());
        FriendNet.getApply(map, new DefaultCallback(getBaseView()) {
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
        adapter = new NewFriendAdapter(getContext(), R.layout.item_new_friend, NewFriendFragemnt.this, friends);
        listView.setAdapter(adapter);
    }
}
