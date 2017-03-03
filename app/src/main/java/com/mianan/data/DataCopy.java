package com.mianan.data;

/**
 * Created by FengChaoQun
 * on 2017/3/2
 */

public class DataCopy {

    public static UserInfo copy(UserInfo j) {
        UserInfo i = new UserInfo();
        i.setIdentification(j.getIdentification());
        i.setBirthday(j.getBirthday());
        i.setHead(j.getHead());
        i.setMotto(j.getMotto());
        i.setNickname(j.getNickname());
        i.setSex(j.getSex());
        return i;
    }
}
