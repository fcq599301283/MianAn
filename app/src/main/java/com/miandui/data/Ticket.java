package com.miandui.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class Ticket extends RealmObject {

    @PrimaryKey
    private long id;
    private String goods_id;
    private String goods_name;
    private String goods_introduction;
    private String goods_image;
    private String mark_need;
    private String money_need;
    private String shop_name;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_introduction() {
        return goods_introduction;
    }

    public void setGoods_introduction(String goods_introduction) {
        this.goods_introduction = goods_introduction;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getMark_need() {
        return mark_need;
    }

    public void setMark_need(String mark_need) {
        this.mark_need = mark_need;
    }

    public String getMoney_need() {
        return money_need;
    }

    public void setMoney_need(String money_need) {
        this.money_need = money_need;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceAndMark() {
        return mark_need + "+" + money_need + "¥";
    }

}
