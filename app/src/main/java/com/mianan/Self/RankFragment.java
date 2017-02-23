package com.mianan.Self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mianan.R;
import com.mianan.data.Friend;
import com.mianan.utils.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < 10; i++) {
            Friend friend = new Friend();
            friend.setName("name" + (i + 1));
            friend.setGrade(150 - i + "");
            friends.add(friend);
        }
        adapter = new RankAdapter(getContext(), 1, friends);
        listView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
