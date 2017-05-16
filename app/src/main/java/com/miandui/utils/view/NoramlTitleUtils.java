package com.miandui.utils.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.miandui.MyApplication;
import com.miandui.R;
import com.miandui.utils.normal.SPUtils;

/**
 * Created by FengChaoQun
 * on 2017/5/16
 */

public class NoramlTitleUtils {

    public static void buildNormalPOpmenu(final View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View menuView = View.inflate(view.getContext(), R.layout.popmenu_normal, null);
                final PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(getActivity().getResources().
//                getDrawable(R.drawable.nothing));
//        popupWindow.setAnimationStyle(R.style.popwindow_anim);

                menuView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int mShowMorePopupWindowWidth = menuView.getMeasuredWidth();

                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);

//                popupWindow.showAsDropDown(v,
//                        -mShowMorePopupWindowWidth + this.getResources().getDimensionPixelSize(R.dimen.x30) + v.getWidth(),
//                        3);
                popupWindow.showAsDropDown(v, 0, 0);

                View exitLay = menuView.findViewById(R.id.exitLay);
                View aboutLay = menuView.findViewById(R.id.aboutLay);
                View settingLay = menuView.findViewById(R.id.settingLay);
                exitLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        SPUtils.put(view.getContext(), SPUtils.IS_QUIT, true);
                        MyApplication.getInstance().exit();
                    }
                });
                aboutLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                settingLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }
        });


    }
}
