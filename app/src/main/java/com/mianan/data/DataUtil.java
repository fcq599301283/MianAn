package com.mianan.data;

import android.util.Log;

import org.json.JSONObject;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/1/8
 */

public class DataUtil {

    public static void saveFromJson(final Class c, final JSONObject jsonObject) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateObjectFromJson(c, jsonObject);
                Log.d("DataUtil", realm.where(c).findAll().toString());
            }
        });
        realm.close();
    }
}
