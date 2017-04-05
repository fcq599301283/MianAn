package com.mianan.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/4/5
 */
public class Shop extends RealmObject {

    /**
     * shop_image : http://139.224.58.165:8080/static/shop/temp2.jpg
     * shop_address : 321231
     * shop_id : 01
     * shop_telephone : 132231
     * shop_name : N多寿司
     */

    @PrimaryKey
    private String shop_id;
    private String shop_image;
    private String shop_address;
    private String shop_telephone;
    private String shop_name;
    private String goods_number;

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_telephone() {
        return shop_telephone;
    }

    public void setShop_telephone(String shop_telephone) {
        this.shop_telephone = shop_telephone;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }
}
