package com.mianan.start;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.mianan.MainActivity;
import com.mianan.NetWork.NetCollection.LoginNet;
import com.mianan.NetWork.callBack.DefaultCallback;
import com.mianan.NetWork.netUtil.LoginUtils;
import com.mianan.NetWork.netUtil.NormalKey;
import com.mianan.R;
import com.mianan.utils.BroadCast.FinishActivityRecever;
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
 * on 2017/2/22
 */

public class RegisterActivity extends BaseActivity {
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

    private FinishActivityRecever finishActivityRecever;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setNeedCollaspInput(false);
        finishActivityRecever = new FinishActivityRecever(this);
        finishActivityRecever.register();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivityRecever.unregister();
        countDownTimer.cancel();
    }

    private void register() {
        if (!StringUtils.isNotEmpty(account, password, password2, vertifyCode)) {
            showToast("请填写完整信息");
        } else {
            if (!password.getText().toString().equals(password2.getText().toString())) {
                showNormalDialog("两次密码不一致.");
                return;
            }
            LoginUtils.register(account.getText().toString(), password.getText().toString(),
                    vertifyCode.getText().toString(), new DefaultCallback(getBaseView()) {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            final NormalDialog dialog = new NormalDialog(getActivity());
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
                                    MainActivity.start(getActivity());
                                }
                            });
                        }
                    });
        }
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
        LoginNet.SendvertifyCodeInLogin(map, new DefaultCallback(getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                sendVertifyCodeLay.setEnabled(false);
                countDownTimer.start();
            }
        });
    }

    @OnClick({R.id.sendVertifyCodeLay, R.id.register, R.id.rootView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendVertifyCodeLay:
                sendVertifyCode();
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
