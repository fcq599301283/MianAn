package com.mianan.NetWork.NetCollection;

import com.mianan.NetWork.callBack.DefaultCallback;
import com.mianan.NetWork.netUtil.BaseUrl;

import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/2
 */

public class LoginNet {

    public static void login(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.LOGIN, map, callback);
    }

    public static void register(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.REGISTER, map, callback);
    }

    public static void SendvertifyCodeInLogin(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.REGISTER_VERTIFY_CODE, map, callback);
    }

    public static void SendvertifyCodeInLoginByCode(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.FORGET_PASSWORD_VERTIFY_CODE, map, callback);
    }

    public static void LoginByCode(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.LOGIN_BY_CODE, map, callback);
    }
}
