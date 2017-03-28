package com.mianan.netWork.netUtil;

import com.mianan.data.Goods;
import com.mianan.data.Ticket;
import com.mianan.netWork.api.NetApiObservableFactory;
import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.netWork.callBack.TotalCallBack;
import com.mianan.netWork.netCollection.BaseRequest;
import com.mianan.netWork.netCollection.ShopNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2017/3/28
 */
public class ShopNetUtils {

    public static void getGoods(final TotalCallBack totalCallBack) {

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onStart() {
                super.onStart();
                totalCallBack.onStart();
            }

            @Override
            public void onCompleted() {
                totalCallBack.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                totalCallBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    final JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NormalKey.code)) {
                        case 200:
                            totalCallBack.onSuccess(jsonObject);
                            final JSONArray jsonArray = jsonObject.getJSONArray(NormalKey.content);
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(Goods.class).findAll().deleteAllFromRealm();
                                    realm.createOrUpdateAllFromJson(Goods.class, jsonArray);
                                }
                            });
                            break;
                        default:
                            totalCallBack.onFail(jsonObject.getString(NormalKey.code), jsonObject.getString(NormalKey.msg));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    totalCallBack.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    totalCallBack.onError(e);
                }
            }


        };

        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_GOODS, new HashMap<String, String>()), subscriber);
    }

    public static void getMyTickets(Map<String, String> map, final TotalCallBack totalCallBack) {

        Subscriber<ResponseBody> subscriber = new Subscriber<ResponseBody>() {
            @Override
            public void onStart() {
                super.onStart();
                totalCallBack.onStart();
            }

            @Override
            public void onCompleted() {
                totalCallBack.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                totalCallBack.onError(e);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    final JSONObject jsonObject = new JSONObject(responseBody.string());
                    switch (jsonObject.getInt(NormalKey.code)) {
                        case 200:
                            totalCallBack.onSuccess(jsonObject);
                            final JSONArray jsonArray = jsonObject.getJSONArray(NormalKey.content);
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(Ticket.class).findAll().deleteAllFromRealm();
                                    realm.createOrUpdateAllFromJson(Ticket.class, jsonArray);
                                }
                            });
                            break;
                        default:
                            totalCallBack.onFail(jsonObject.getString(NormalKey.code), jsonObject.getString(NormalKey.msg));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    totalCallBack.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    totalCallBack.onError(e);
                }
            }
        };

        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_MY_TICKETS, map), subscriber);
    }

    public static void buyTickets(Map<String, String> map, final DefaultCallback callback) {
        ShopNet.buyTickets(map, new DefaultCallback(callback.getBaseView()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                callback.onSuccess(jsonObject);
                //购买完成后刷新本地积分数据
                BTNetUtils.refreshMarkAndTimeBack(null);
            }
        });
    }
}
