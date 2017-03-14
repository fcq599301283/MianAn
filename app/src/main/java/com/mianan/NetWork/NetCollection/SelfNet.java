package com.mianan.NetWork.NetCollection;

import com.mianan.NetWork.callBack.DefaultCallback;
import com.mianan.NetWork.callBack.SimpleCallback;
import com.mianan.NetWork.netUtil.BaseUrl;

import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/3
 */

public class SelfNet {

    public static void getInfoback(Map<String, String> map, SimpleCallback callback) {
        BaseRequest.NormalPostBackRequest(BaseUrl.GET_INFO, map, callback);
    }

    public static void getInfo(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_INFO, map, callback);
    }
}
