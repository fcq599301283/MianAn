package com.mianan.netWork.netUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by FengChaoQun
 * on 2016/11/21
 */

public class ShowMsgHandler extends Handler {
    public static final int CODE_FAIL = 1;
    private Context context;
    private String toastMsg;

    public ShowMsgHandler(Context context) {
        super(context.getMainLooper());
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case CODE_FAIL:
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();

        }
    }

    public void showToast(String toastMsg) {
        this.toastMsg = toastMsg;
        this.sendEmptyMessage(CODE_FAIL);
    }
}
