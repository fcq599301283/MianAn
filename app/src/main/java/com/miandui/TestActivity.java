package com.miandui;

import android.os.Bundle;

import com.miandui.utils.base.BaseActivity;
import com.miandui.utils.view.customView.TimeCountView;

/**
 * Created by FengChaoQun
 * on 2017/5/27
 */

public class TestActivity extends BaseActivity {
    private TimeCountView timeCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        timeCountView = (TimeCountView) findViewById(R.id.timeCountView);

        timeCountView.setSeconds(3500);
    }
}
