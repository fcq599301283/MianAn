package com.miandui.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/3/2
 */

public class UserInfo extends RealmObject {

    /**
     * motto : 您尚未设置签名
     * birthday : 1970-01-01
     * nickname : 您尚未设置昵称
     * head : http://qjzhzw.tunnel.qydev.com/static/head/wolf_KbNIpdq.png
     * sex : 男
     */

    @PrimaryKey
    private String identification;
    private String motto;
    private String birthday;
    private String nickname;
    private String head;
    private String sex;
    private String time;
    private String num_prize;

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum_prize() {
        return num_prize;
    }

    public void setNum_prize(String num_prize) {
        this.num_prize = num_prize;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "identification='" + identification + '\'' +
                ", motto='" + motto + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head='" + head + '\'' +
                ", sex='" + sex + '\'' +
                ", time='" + time + '\'' +
                ", num_prize='" + num_prize + '\'' +
                '}';
    }
}
