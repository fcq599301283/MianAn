package com.mianan.start;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
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

public class LoginActivity extends BaseActivity {
    @Bind(R.id.account)
    ClearableEditText account;
    @Bind(R.id.password)
    ClearableEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    private void login() {
        if (!StringUtils.isNotEmpty(account, password)) {
            showToast("请填写完整信息");
            return;
        }

    }

    private void findPassword() {
        final String[] stringItems = {"找回密码", "短信验证登录"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, getWindow().getDecorView());
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
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
                break;
        }
    }
}
