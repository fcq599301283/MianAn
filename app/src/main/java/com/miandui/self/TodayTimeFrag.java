package com.miandui.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.data.MarkAndTime;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.netUtil.BTNetUtils;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.normal.TimeUtils;
import com.miandui.utils.view.customView.TimeCountView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class TodayTimeFrag extends BaseFragment {
    @Bind(R.id.today_grade)
    TextView todayGrade;
    @Bind(R.id.timeCountView)
    TimeCountView timeCountView;
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
        timeCountView.setSeconds(TimeUtils.translateHHMMSStoSecond2(TempUser.getMarkAndTime().getTodayTime()));
        todayGrade.setText(String.format(getString(R.string.todayTotalMark), TempUser.getMarkAndTime().getTodayMark()));
        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    timeCountView.setSeconds(TimeUtils.translateHHMMSStoSecond2(markAndTime.getTodayTime()));
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
