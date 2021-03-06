package com.miandui.netWork.netUtil;

import com.miandui.data.Goods;
import com.miandui.data.Shop;
import com.miandui.data.Ticket;
import com.miandui.netWork.api.NetApiObservableFactory;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.callBack.TotalCallBack;
import com.miandui.netWork.netCollection.BaseRequest;
import com.miandui.netWork.netCollection.ShopNet;
import com.miandui.utils.base.BaseView;

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

    public static void getShops(final TotalCallBack totalCallBack) {

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
                                    realm.where(Shop.class).findAll().deleteAllFromRealm();
                                    realm.createOrUpdateAllFromJson(Shop.class, jsonArray);
                                }
                            });
                            break;
                        case 201:
                            deleteShops();
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

        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_SHOPS, new HashMap<String, String>()), subscriber);
    }

    public static void getShops2(final BaseView b) {
        ShopNet.getShops(new DefaultCallback(b) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                final JSONArray jsonArray;
                try {
                    jsonArray = jsonObject.getJSONArray(NormalKey.content);
                    Realm realm = b.getRelm();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Shop.class).findAll().deleteAllFromRealm();
                            realm.createOrUpdateAllFromJson(Shop.class, jsonArray);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    b.showToast("解析返回数据时出现异常");
                }
            }

            @Override
            public void onFail(String code, String msg) {
                if ("201".equals(code)) {
                    deleteShops();
                } else {
                    super.onFail(code, msg);
                }
            }
        });
    }

    public static void getGoods(String shopId, final TotalCallBack totalCallBack) {

        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.shop_id, shopId);

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
                        case 201:
                            deleteGoods();
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

        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_GOODS, map), subscriber);
    }

    public static void getGoods2(String shopId, final BaseView baseView) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.shop_id, shopId);
        ShopNet.getGoods(map, new DefaultCallback(baseView) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                final JSONArray jsonArray;
                try {
                    jsonArray = jsonObject.getJSONArray(NormalKey.content);
                    Realm realm = baseView.getRelm();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Goods.class).findAll().deleteAllFromRealm();
                            realm.createOrUpdateAllFromJson(Goods.class, jsonArray);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    baseView.showToast("解析返回数据时出现异常");
                }

            }

            @Override
            public void onFail(String code, String msg) {
                if ("201".equals(code)) {
                    deleteGoods();
                } else {
                    super.onFail(code, msg);
                }
            }
        });
    }

    public static void getMyTickets(Map<String, String> map, final boolean getRecord, final TotalCallBack totalCallBack) {

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
                            deleteTicket(getRecord);
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.createOrUpdateAllFromJson(Ticket.class, jsonArray);
                                }
                            });
                            realm.close();
                            break;
                        case 201:
                            deleteTicket(getRecord);
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

        if (getRecord) {
            BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_TICKET_RECORD, map), subscriber);
        } else {
            BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.GET_MY_TICKETS, map), subscriber);
        }

    }

    public static void getMyTickets2(Map<String, String> map, final boolean getRecord, final BaseView baseView) {
        DefaultCallback defaultCallback = new DefaultCallback(baseView) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                final JSONArray jsonArray;
                try {
                    jsonArray = jsonObject.getJSONArray(NormalKey.content);
                    deleteTicket(getRecord);
                    Realm realm = baseView.getRelm();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.createOrUpdateAllFromJson(Ticket.class, jsonArray);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    baseView.showToast("解析返回数据时出现异常");
                }

            }

            @Override
            public void onFail(String code, String msg) {
                if ("201".equals(code)) {
                    deleteTicket(getRecord);
                } else {
                    super.onFail(code, msg);
                }

            }
        };

        if (getRecord) {
            ShopNet.getInvalidTickets(map, defaultCallback);
        } else {
            ShopNet.getValidTickets(map, defaultCallback);
        }
    }

    private static void deleteTicket(boolean record) {
        Realm realm = Realm.getDefaultInstance();
        if (record) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Ticket.class).not().equalTo(NormalKey.status, NormalKey.valid).findAll().deleteAllFromRealm();
                }
            });
        } else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Ticket.class).equalTo(NormalKey.status, NormalKey.valid).findAll().deleteAllFromRealm();
                }
            });
        }
        realm.close();
    }

    private static void deleteShops() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Shop.class).findAll().deleteAllFromRealm();
            }
        });
        realm.close();
    }

    private static void deleteGoods() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Goods.class).findAll().deleteAllFromRealm();
            }
        });
        realm.close();
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
