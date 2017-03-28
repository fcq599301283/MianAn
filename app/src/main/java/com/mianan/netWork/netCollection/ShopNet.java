package com.mianan.netWork.netCollection;

import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.netWork.netUtil.BaseUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopNet {
    public static void getGoods(DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_GOODS, new HashMap<String, String>(), callback);
    }

    public static void buyTickets(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.BUY_TICKETS, map, callback);
    }

    public static void useTicket(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.USE_TICKET, map, callback);
    }
}
