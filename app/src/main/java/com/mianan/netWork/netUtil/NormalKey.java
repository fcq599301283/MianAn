package com.mianan.netWork.netUtil;

/**
 * Created by FengChaoQun
 * on 2016/8/28
 * 和服务器交互的一些常用字段
 */
public interface NormalKey {

    /**
     * 2016/12/14 21:42
     * description:分隔符
     */
    String SPLIT = "#";
    String SPLIT2 = "\\$";
    String SPLIT3 = "\\^";

    /**
     * 2016/12/14 21:44
     * description:服务器返回常用字段
     */
    String code = "code";
    String msg = "msg";
    String content = "content";
    int CODE_200 = 200;
    String REQUEST_SUCCESS = "200";

    String identification = "identification";
    String password = "password";
    String userId = "userId";
    String isUploaded = "isUploaded";
    String time = "time";

    String head = "head";
    String nickname = "nickname";
    String sex = "sex";
    String birthday = "birthday";
    String motto = "motto";

    String timing = "timing";
    String date_start = "date_start";
    String date_end = "date_end";
    String time_start = "time_start";
    String time_end = "time_end";

    String used = "0";
    String pastDue = "-1";
    String valid = "1";

    String goods_id = "goods_id";
    String goods_amount = "goods_amount";
    String status = "status";
    String id = "id";

    String shop_id="shop_id";

}
