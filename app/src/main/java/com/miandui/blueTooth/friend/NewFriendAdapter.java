package com.miandui.blueTooth.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Friend;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netCollection.FriendNet;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.MyGlide;
import com.miandui.utils.TempUser;
import com.miandui.utils.view.customView.CirecleImage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class NewFriendAdapter extends ArrayAdapter<Friend> {
    private NewFriendFragemnt newFriendFragemnt;

    public NewFriendAdapter(Context context, int resource, NewFriendFragemnt newFriendFragemnt, List<Friend> objects) {
        super(context, resource, objects);
        this.newFriendFragemnt = newFriendFragemnt;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_new_friend, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Friend friend = getItem(position);
        viewHolder.name.setText(friend.getNickname());
        MyGlide.with_default_head(getContext(), friend.getHead(), viewHolder.headImage);
        viewHolder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle(friend, NormalKey.refuse);
            }
        });

        viewHolder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle(friend, NormalKey.agree);
            }
        });
        return convertView;
    }

    private void handle(Friend friend, String status) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification_sender, friend.getIdentification());
        map.put(NormalKey.identification_receiver, TempUser.getAccount());
        map.put(NormalKey.status, status);
        FriendNet.handle(map, new DefaultCallback(newFriendFragemnt) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                newFriendFragemnt.getData();
            }
        });
    }



    static class ViewHolder {
        @Bind(R.id.head)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.refuse)
        TextView refuse;
        @Bind(R.id.agree)
        TextView agree;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
