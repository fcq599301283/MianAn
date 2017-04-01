package com.mianan.blueTooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.utils.LinkService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class DeviceAdpter extends ArrayAdapter<BluetoothDevice> {

    public DeviceAdpter(Context context, int resource, List<BluetoothDevice> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_bt_device, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final BluetoothDevice bluetoothDevice = getItem(position);
        if (TextUtils.isEmpty(bluetoothDevice.getName())) {
            viewHolder.name.setText("未命名");
        } else {
            viewHolder.name.setText(bluetoothDevice.getName());
        }
        viewHolder.mac.setText(bluetoothDevice.getAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyHandler.getInstance().getCurrentState() == MyHandler.STATE_CONNECTED) {
                    final NormalDialog normalDialog = new NormalDialog(getContext());
                    normalDialog.content("你已经连接了一个设备,是否断开?")
                            .btnText("是", "否")
                            .setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    LinkService.getInstance().setBTModel(true);
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
                    LinkService.getInstance().Connect(bluetoothDevice, getContext());
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.mac)
        TextView mac;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
