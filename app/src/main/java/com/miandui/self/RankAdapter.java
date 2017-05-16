package com.miandui.self;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.Friend;
import com.miandui.utils.view.customView.CirecleImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class RankAdapter extends ArrayAdapter<Friend> {
    private List<Friend> friends;

    public RankAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = getItem(position);

        ViewHolder0 viewHolder0;
        if (position == 0) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_rank0, null);
                viewHolder0 = new ViewHolder0(convertView);
                convertView.setTag(viewHolder0);
            } else {
                viewHolder0 = (ViewHolder0) convertView.getTag();
            }
            viewHolder0.rank.setText(position + 1 + "、");
            viewHolder0.name.setText(friend.getNickname());
            viewHolder0.grade.setText(friend.getMark() + "分");
        } else {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_rank, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.rank.setText(friend.getPlace() + "、");
            viewHolder.name.setText(friend.getNickname());
            viewHolder.grade.setText(friend.getMark() + "分");
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }


    static class ViewHolder0 {
        @Bind(R.id.rank)
        TextView rank;
        @Bind(R.id.head)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.imageNameLay)
        RelativeLayout imageNameLay;
        @Bind(R.id.mark)
        TextView grade;
        @Bind(R.id.rootView)
        RelativeLayout rootView;

        ViewHolder0(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @Bind(R.id.rank)
        TextView rank;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.mark)
        TextView grade;
        @Bind(R.id.rootView)
        RelativeLayout rootView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
