package com.miandui.netWork.netCollection;

import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.netUtil.BaseUrl;

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

    public static void getPrizeList(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_PRIZE_LIST, map, callback);
    }

    public static void getGetPrize(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_PRIZE, map, callback);
    }

    public static void buyPrizeChance(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.BUY_PRIZE_CHANCE, map, callback);
    }

    public static void getAD(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.GET_AD, map, callback);
    }
}
