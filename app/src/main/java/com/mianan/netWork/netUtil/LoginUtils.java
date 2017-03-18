package com.mianan.netWork.netUtil;

import com.mianan.MainActivity;
import com.mianan.netWork.netCollection.LoginNet;
import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.data.DataUtil;
import com.mianan.data.UserInfo;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/2
 */

public class LoginUtils {

    public static void login(final String account, final String password, final BaseView baseView) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, account);
        map.put(NormalKey.password, password);
        LoginNet.login(map, new DefaultCallback(baseView) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    DataUtil.saveFromJson(UserInfo.class, jsonObject.getJSONObject(NormalKey.content));
                    TempUser.setAccount(account);
                    TempUser.setPassword(password);
                    TempUser.reloadUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity.start(baseView.getActivity());
                baseView.getActivity().finish();
            }
        });
    }

    public static void loginByCode(final String account, String code, final BaseView baseView) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, account);
        map.put(NormalKey.code, code);
        LoginNet.LoginByCode(map, new DefaultCallback(baseView) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    DataUtil.saveFromJson(UserInfo.class, jsonObject.getJSONObject(NormalKey.content));
                    TempUser.setAccount(account);
                    TempUser.reloadUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity.start(baseView.getActivity());
                baseView.getActivity().finish();
            }
        });
    }

    public static void register(final String account, String password, String code, final DefaultCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, account);
        map.put(NormalKey.password, password);
        map.put(NormalKey.code, code);
        LoginNet.register(map, new DefaultCallback(callback.getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    DataUtil.saveFromJson(UserInfo.class, jsonObject.getJSONObject(NormalKey.content));
                    TempUser.setAccount(account);
                    TempUser.reloadUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(jsonObject);
            }
        });
    }

}
