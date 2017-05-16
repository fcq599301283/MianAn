package com.miandui.netWork.netCollection;

import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netUtil.BaseUrl;

import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/5/16
 */

public class FriendNet {
    public static void apply(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.APPLY, map, callback);
    }

    public static void getApply(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_APPLY, map, callback);
    }

    public static void getFriend(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_FRIEND, map, callback);
    }

    public static void handle(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.HANDLE, map, callback);
    }

    public static void getRankBack(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostBackRequest(BaseUrl.GET_RANK, map, callback);
    }
}
