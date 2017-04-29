package com.mianan.blueTooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mianan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class RoundDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    private List<BluetoothDevice> connectedDevices = new ArrayList<>();

    public RoundDeviceAdapter(Context context, int resource, List<BluetoothDevice> objects, List<BluetoothDevice> connectedDevices) {
        super(context, resource, objects);
        if (connectedDevices != null) {
            this.connectedDevices = connectedDevices;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_connected_friend, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice friend = getItem(position);
        if (friend != null) {
            viewHolder.name.setText(TextUtils.isEmpty(friend.getName()) ? "未命名" : friend.getName());
        }

        if (connectedDevices.contains(friend)) {
            viewHolder.connectState.setText("已连接");
        } else {
            viewHolder.connectState.setText("未连接");
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.connect_state)
        TextView connectState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
