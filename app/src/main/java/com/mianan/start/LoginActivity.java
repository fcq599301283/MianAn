package com.mianan.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.mianan.netWork.netUtil.LoginUtils;
import com.mianan.R;
import com.mianan.utils.TempUser;
import com.mianan.utils.broadCast.FinishActivityRecever;
import com.mianan.utils.base.BaseActivity;
import com.mianan.utils.normal.SPUtils;
import com.mianan.utils.normal.StringUtils;
import com.mianan.utils.view.customView.ClearableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by FengChaoQun
 * on 2017/2/22
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.account)
    ClearableEditText account;
    @Bind(R.id.password)
    ClearableEditText password;

    private FinishActivityRecever finishActivityRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        finishActivityRecever = new FinishActivityRecever(this);
        finishActivityRecever.register();
//        String lastAccount = (String) SPUtils.get(getActivity(), SPUtils.ACCOUNT, SPUtils.DEFAULT_STRING);
//        if (lastAccount != null && !lastAccount.equals(SPUtils.DEFAULT_STRING)) {
//            account.setText(lastAccount);
//            account.setSelection(lastAccount.length());
//        }
        if (TempUser.getAccount() != null) {
            account.setText(TempUser.getAccount());
            account.setSelection(TempUser.getAccount().length());
        }
//        String passwordText=(String)SPUtils.get(getActivity(),SPUtils.PASSWORD,SPUtils.DEFAULT_STRING);
//        if (passwordText!=null&&!passwordText.equals(SPUtils.DEFAULT_STRING)){
//            password.setText(passwordText);
//            password.setSelection(passwordText.length());
//        }
        if (TempUser.getPassword() != null) {
            password.setText(TempUser.getPassword());
            password.setSelection(TempUser.getPassword().length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivityRecever.unregister();
    }

    private void login() {
        if (!StringUtils.isNotEmpty(account, password)) {
            showToast("请填写完整信息");
            return;
        }
        if (account.getText().toString().length() != 11) {
            showNormalDialog("手机号位数不对");
            return;
        }

        LoginUtils.login(account.getText().toString(), password.getText().toString(), this);
    }

    private void findPassword() {
        final String[] stringItems = {"找回密码", "短信验证登录"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, getWindow().getDecorView());
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        showToast("开发中");
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), LoginByCodeActivity.class));
                        break;
                }
            }
        });
    }

    @OnClick({R.id.account, R.id.password, R.id.login, R.id.findpassword, R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.findpassword:
                findPassword();
                break;
            case R.id.register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
        }
    }
}
