package com.mianan.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.data.MarkAndTime;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.BTNetUtils;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class TodayTimeFrag extends BaseFragment {
    @Bind(R.id.chronometer)
    Chronometer chronometer;
    @Bind(R.id.today_grade)
    TextView todayGrade;
    private TempUser.onMarkChange onMarkChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_today_time);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        registerObservers(false);
    }

    private void initView() {
        chronometer.setText(TempUser.getMarkAndTime().getTodayTime());
        todayGrade.setText(String.format(getString(R.string.todayTotalMark), TempUser.getMarkAndTime().getTodayMark()));
        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    chronometer.setText(markAndTime.getTodayTime());
                    todayGrade.setText(String.format(getString(R.string.todayTotalMark), markAndTime.getTodayMark()));
                }
            };
        }
        TempUser.registerOnMarkChangeObserver(onMarkChange, register);
    }

    public void onRefresh() {
        BTNetUtils.refreshMarkAndTimeBack(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
            }

            @Override
            public void onFail(String code, String msg) {
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast("加载异常");
            }
        });

    }

}
