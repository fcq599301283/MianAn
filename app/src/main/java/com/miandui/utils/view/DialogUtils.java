package com.miandui.utils.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;

import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.miandui.MyApplication;

/**
 * Created by FengChaoQun
 * on 2017/1/2
 */

public class DialogUtils {

    public static void showExitDialog(final Activity activity) {
        final NormalDialog dialog = new NormalDialog(activity);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content("是否确定退出程序?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
                .showAnim(new ZoomInEnter())//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        MyApplication.getInstance().exit();
                    }
                });

    }

}
