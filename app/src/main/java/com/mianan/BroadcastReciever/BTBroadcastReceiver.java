package com.mianan.BroadcastReciever;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/3/5
 */

public class BTBroadcastReceiver extends BroadcastReceiver {

    public static int currentScanMode;
    public static boolean IsBTOn;

    private static List<ScanModeChange> scanModeChanges = new ArrayList<>();
    private static List<BTStateChange> btStateChanges = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                setScanMode(intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE));
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                if (BluetoothAdapter.STATE_ON == intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)) {
                    setIsBTOn(true);
                } else {
                    setIsBTOn(false);
                }
                break;
        }
    }

    private static void setScanMode(int mode) {
        currentScanMode = mode;
        for (int i = 0; i < scanModeChanges.size(); i++) {
            scanModeChanges.get(i).onChange(mode);
        }
    }

    private static void setIsBTOn(boolean isON) {
        IsBTOn = isON;
        for (int i = 0; i < btStateChanges.size(); i++) {
            btStateChanges.get(i).onChange(isON);
        }
    }

    public static void registerScanModeChange(ScanModeChange scanModeChange, boolean isRegister) {
        if (scanModeChange == null) {
            return;
        }
        if (isRegister) {
            scanModeChanges.add(scanModeChange);
        } else {
            scanModeChanges.remove(scanModeChange);
        }
    }

    public static void registerBTStateChange(BTStateChange btStateChange, boolean isRegister) {
        if (btStateChange == null) {
            return;
        }
        if (isRegister) {
            btStateChanges.add(btStateChange);
        } else {
            btStateChanges.remove(btStateChange);
        }
    }

    public interface ScanModeChange {
        void onChange(int mode);
    }

    public interface BTStateChange {
        void onChange(boolean isOn);
    }
}
