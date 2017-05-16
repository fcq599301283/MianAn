package com.miandui.self;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miandui.R;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.netUtil.BTNetUtils;
import com.miandui.netWork.netUtil.NormalKey;
import com.miandui.utils.base.BaseFragment;
import com.miandui.utils.normal.TimeUtils;
import com.miandui.utils.view.customView.RecordChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class OtherDyasFrag extends BaseFragment {
    @Bind(R.id.averagerGrade)
    TextView averagerGrade;
    @Bind(R.id.recordChart)
    RecordChart recordChart;
    @Bind(R.id.duration)
    TextView duration;
    @Bind(R.id.history)
    TextView history;
    @Bind(R.id.averagerTime)
    TextView averagerTime;

    private List<Integer> marks = new ArrayList<>();
    private List<Float> times = new ArrayList<>();
    private long totalTime;
    private int totalMark;

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
//        initView();

        Calendar calendar = Calendar.getInstance();
        String endTime = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        String startTime = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        duration.setText(startTime + "-" + endTime);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.back);
        Matrix matrix = new Matrix();
        matrix.postRotate(180); /*翻转180度*/
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newImage = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        history.setCompoundDrawables(null, null, new BitmapDrawable(getResources(), newImage), null);

        getData();
    }

    private void setData(List<Float> times) {

        averagerTime.setText("日均时间:" + TimeUtils.getDataByMMHHSS(totalTime / 7));
        averagerGrade.setText("日均积分:" + totalMark / 7);

//        ArrayList<Float> arrayList = new ArrayList<>();
//        for (Integer mark : marks) {
//            arrayList.add(Float.valueOf(mark));
//        }

        Log.d("OtherDyasFrag", "arrayList:" + times);
        recordChart.setValues(times);

    }

    private void getData() {
        BTNetUtils.getRecord(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject data = jsonObject.getJSONObject(NormalKey.content);
                    marks.clear();
                    times.clear();
                    totalMark = 0;
                    for (int i = 6 /*13*/; i >= 0; i--) {
                        int currentMark = data.getJSONObject(TimeUtils.getData(-i)).getInt(NormalKey.mark);
                        marks.add(currentMark);
                        times.add(TimeUtils.translateHHMMSStoHours(data.getJSONObject(TimeUtils.getData(-i)).getString(NormalKey.time)));
                        totalMark = totalMark + currentMark;
                    }
                    totalTime = TimeUtils.translateHHMMSStoSecond2(data.getString(NormalKey.time));

                    setData(times);
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
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
