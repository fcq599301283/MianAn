package com.miandui.data;

import io.realm.RealmObject;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class Friend extends RealmObject {
    private String identification;
    private String nickname;
    private String head;
    private String mark;
    private String place;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getNickname() {
        return "" + nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return "" + head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMark() {
        return "" + mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
