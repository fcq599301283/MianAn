package com.mianan.start;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.R;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.StringUtils;
import com.mianan.utils.view.customView.ClearableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/2/22
 */

public class registerActivity extends BaseActivity {
    @Bind(R.id.account)
    ClearableEditText account;
    @Bind(R.id.password)
    ClearableEditText password;
    @Bind(R.id.password2)
    ClearableEditText password2;
    @Bind(R.id.vertifyCode)
    ClearableEditText vertifyCode;
    @Bind(R.id.sendVertifyCode)
    TextView sendVertifyCode;
    @Bind(R.id.sendVertifyCodeLay)
    FrameLayout sendVertifyCodeLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setNeedCollaspInput(false);
    }

    private void register() {
        if (!StringUtils.isNotEmpty(account, password, password2, vertifyCode)) {
            showToast("请填写完整信息");
        } else {
            final NormalDialog dialog = new NormalDialog(this);
            dialog.content("注册成功，欢迎加入!")//
                    .btnNum(1)
                    .isTitleShow(false)
                    .contentGravity(Gravity.CENTER)
                    .btnText("进入")//
                    .btnTextColor(Color.parseColor("#000000"))
                    .show();

            dialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                }
            });
        }
    }

    @OnClick({R.id.sendVertifyCodeLay, R.id.register, R.id.rootView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendVertifyCodeLay:
                break;
            case R.id.register:
                register();
                break;
            case R.id.rootView:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View v = getCurrentFocus();
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;
        }
    }
}
