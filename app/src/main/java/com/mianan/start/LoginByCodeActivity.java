package com.mianan.start;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mianan.NetWork.NetCollection.LoginNet;
import com.mianan.NetWork.callBack.DefaultCallback;
import com.mianan.NetWork.netUtil.LoginUtils;
import com.mianan.NetWork.netUtil.NormalKey;
import com.mianan.R;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.StringUtils;
import com.mianan.utils.view.customView.ClearableEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/3/3
 */

public class LoginByCodeActivity extends BaseActivity {
    @Bind(R.id.account)
    ClearableEditText account;
    @Bind(R.id.vertifyCode)
    ClearableEditText vertifyCode;
    @Bind(R.id.sendVertifyCode)
    TextView sendVertifyCode;
    @Bind(R.id.sendVertifyCodeLay)
    FrameLayout sendVertifyCodeLay;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_code);
        ButterKnife.bind(this);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendVertifyCode.setText(millisUntilFinished / 1000 + "秒后重新发送");
            }

            @Override
            public void onFinish() {
                sendVertifyCodeLay.setEnabled(true);
                sendVertifyCode.setText("发送验证码");
            }
        };
    }

    private void sendVertifyCode() {
        if (!StringUtils.isNotEmpty(account)) {
            showNormalDialog("请输入手机号");
            return;
        }

        if (account.getText().toString().length() != 11) {
            showNormalDialog("手机号位数不对");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, account.getText().toString());
        LoginNet.SendvertifyCodeInLoginByCode(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                sendVertifyCodeLay.setEnabled(false);
                countDownTimer.start();
            }
        });
    }

    private void login() {
        if (!StringUtils.isNotEmpty(account, vertifyCode)) {
            showNormalDialog("请填写完整信息");
            return;
        }
        if (account.getText().toString().length() != 11) {
            showNormalDialog("手机号位数不对");
            return;
        }

        LoginUtils.loginByCode(account.getText().toString(), vertifyCode.getText().toString(), getBaseView());

    }

    @OnClick({R.id.sendVertifyCodeLay, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendVertifyCodeLay:
                sendVertifyCode();
                break;
            case R.id.login:
                login();
                break;
        }
    }
}
