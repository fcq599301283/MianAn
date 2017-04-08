package com.mianan.self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mianan.R;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.BTNetUtils;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.utils.base.BaseFragment;
import com.mianan.utils.normal.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class OtherDyasFrag extends BaseFragment {
    @Bind(R.id.barChar)
    BarChart mChart;
    @Bind(R.id.averagerTime)
    TextView averagerTime;
    @Bind(R.id.averagerGrade)
    TextView averagerGrade;

    private List<Integer> marks = new ArrayList<>();
    private long totalTime;
    private int totalMark;
    private static final String TAG = "OtherDyasFrag";
    private ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_other_day);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mChart.setMaxVisibleValueCount(14);
        mChart.setScaleEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(15f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setGranularity(15);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);

        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.getLegend().setEnabled(false);

        getData();

//        setData(13, 24);
//        Log.d("1111111", TimeUtils.getTodayDate());
//        Log.d("1111111", TimeUtils.getData(1));
    }

    private void setData(List<Integer> marks/*int count, float range*/) {

        float start = 1f;


//        for (int i = (int) start; i < start + count + 1; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//            yVals1.add(new BarEntry(i, val));
//        }

        yVals1.clear();
        for (int i = 1; i <= marks.size(); i++) {
            yVals1.add(new BarEntry(i, marks.get(i - 1)));
        }

        Log.d("OtherDyasFrag", "yVals1:" + yVals1);

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setColors(getResources().getColor(R.color.yeff));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.3f);

            if (mChart.getData() != null) {
                mChart.getData().clearValues();
            }

            mChart.setData(data);
        }
        mChart.invalidate();

        averagerTime.setText("日均时间:" + TimeUtils.getDataByMMHHSS(totalTime / 14) /*TimeUtils.getHHMMSS(totalTime / 14)*/);
        averagerGrade.setText("日均积分:" + totalMark / 14);

    }

    private void getData() {
        BTNetUtils.getRecord(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject(NormalKey.content);
                    marks.clear();
                    totalMark = 0;
                    for (int i = 13; i >= 0; i--) {
                        marks.add(data.getInt(TimeUtils.getData(-i)));
                        totalMark = totalMark + data.getInt(TimeUtils.getData(-i));
                    }
                    totalTime = TimeUtils.translateHHMMSStoSecond2(data.getString(NormalKey.time));

                    setData(marks);
//                    setData(13, 24);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String code, String msg) {
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast("异常");
            }
        });
    }

    public void onRefresh() {
        getData();
        Log.d("refresh", "getRecord");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showAnim() {
        mChart.animateY(2000);
    }
}
