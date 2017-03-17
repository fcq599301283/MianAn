package com.mianan.blueTooth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.data.Friend;
import com.mianan.utils.LinkService;
import com.mianan.utils.MyGlide;
import com.mianan.utils.view.customView.CirecleImage;

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
        viewHolder.name.setText(friend.getName());
        MyGlide.with_default_head(getContext(), friend.getHeadImage(), viewHolder.headImage);
        viewHolder.grade.setText(friend.getGrade());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LinkService.getInstance().getState() == LinkService.STATE_CONNECTED) {
                    final NormalDialog normalDialog = new NormalDialog(getContext());
                    normalDialog.content("你已经连接了一个设备,是否断开?")
                            .btnText("是", "否")
                            .setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    LinkService.getInstance().safeReset();
                                    normalDialog.dismiss();
                                }
                            }, new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    normalDialog.dismiss();
                                }
                            });
                    normalDialog.show();
                } else {
//                    LinkService.getInstance().connect(friend.getDevice());
                    LinkService.getInstance().safeConnet(friend.getDevice(), getContext());
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.headImage)
        CirecleImage headImage;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.grade)
        TextView grade;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
