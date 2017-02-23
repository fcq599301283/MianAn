package com.mianan.start;

import android.content.Intent;
import android.os.Bundle;

import com.mianan.R;
import com.mianan.utils.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/2/22
 */

public class FlashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.enter)
    public void onClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
