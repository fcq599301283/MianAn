package com.miandui.netWork.netUtil;

/**
 * Created by FengChaoQun
 * on 2016/8/3
 */
public interface BaseUrl {
        String BASE_URL = "http://139.224.58.165:8080/miandui/";
//    String BASE_URL = "http://qjzhzw.tunnel.qydev.com/miandui/";
    String LOGIN = "login/";
    String REGISTER = "register/";
    String REGISTER_VERTIFY_CODE = "vertification/";
    String FORGET_PASSWORD_VERTIFY_CODE = "forget_vertification/";
    String LOGIN_BY_CODE = "forget_login/";
    String MODIFT_INFO = "information_post/";
    String GET_INFO = "information_get/";
    String UPLOAD_RECORD = "timing_post/";
    String GET_RECORD = "mark_get_days/";
    String GET_TODAY_RECORD = "mark_get_today/";
    String GET_GOODS = "goods_list_get/";
    String BUY_TICKETS = "discount_buy/";
    String GET_MY_TICKETS = "discount_list_get/";
    String GET_TICKET_RECORD = "buy_list_get/";
    String USE_TICKET = "discount_use/";
    String GET_SHOPS = "shop_list_get/";
    String SIGN_IN = "daily_sign_in/";
    String APPLY = "friend_add_request/";
    String GET_APPLY = "friend_add_list_receiver/";
    String GET_FRIEND = "friend_list_get/";
    String HANDLE = "friend_add_response/";
    String GET_RANK = "friend_rank_list_get/";
    String GET_PRIZE_LIST = "prize_list_get/";
    String GET_PRIZE = "prize_get/";
    String BUY_PRIZE_CHANCE = "prize_num_buy/";
    String GET_AD = "advertisement_list_get/";
}

