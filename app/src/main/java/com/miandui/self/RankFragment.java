package com.miandui.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class RankFragment extends BaseFragment {
    @Bind(R.id.listView)
    ListView listView;

    private RankAdapter adapter;
    private List<Friend> friends;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_rank);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        friends = new ArrayList<>();
        adapter = new RankAdapter(getContext(), 1, friends);
        listView.setAdapter(adapter);
        getData();
    }

    public void onRefresh() {
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        FriendNet.getRankBack(map, new DefaultCallback(getBaseView()) {
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
        });
    }

    private void initListView(List<Friend> friends) {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        Collections.sort(friends, comparator);
        adapter = new RankAdapter(getContext(), 1, friends);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private Comparator<Friend> comparator = new Comparator<Friend>() {
        @Override
        public int compare(Friend o1, Friend o2) {
            int rank1, rank2;
            if (TextUtils.isEmpty(o1.getPlace())) {
                rank1 = 0;
            } else {
                rank1 = Integer.valueOf(o1.getPlace());
            }
            if (TextUtils.isEmpty(o2.getPlace())) {
                rank2 = 0;
            } else {
                rank2 = Integer.valueOf(o2.getPlace());
            }

            return Integer.compare(rank1, rank2);
        }
    };
}
