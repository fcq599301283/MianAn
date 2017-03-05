package com.mianan.BlueTooth;

import android.os.Bundle;
import android.widget.ListView;

import com.mianan.R;
import com.mianan.data.Friend;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.view.customView.ClearableEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        for (int i = 1; i <= 10; i++) {
            Friend friend = new Friend();
            friend.setName("name" + i);
            friend.setGrade("" + (150 - i));
            friends.add(friend);
        }
        adapter = new FriendAdapter(this, 1, friends);
        listView.setAdapter(adapter);
    }
}
