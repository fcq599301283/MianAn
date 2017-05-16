package com.miandui.blueTooth.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Friend;
import com.miandui.utils.MyGlide;
import com.miandui.utils.view.customView.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class FriendAdapter extends ArrayAdapter<Friend> {
    public FriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_friend, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Friend friend = getItem(position);
        viewHolder.name.setText(friend.getNickname());
        MyGlide.with_default_head(getContext(), friend.getHead(), viewHolder.headImage);
        viewHolder.grade.setText(friend.getMark());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.mark)
        TextView grade;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
