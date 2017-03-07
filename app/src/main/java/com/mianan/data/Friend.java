package com.mianan.data;

import android.bluetooth.BluetoothDevice;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class Friend {
    private String name;
    private String headImage;
    private String grade;
    private BluetoothDevice device;

    public String getName() {
        return ""+name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return ""+headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getGrade() {
        return ""+grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
