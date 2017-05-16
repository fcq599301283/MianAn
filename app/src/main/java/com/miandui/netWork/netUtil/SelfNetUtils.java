package com.miandui.netWork.netUtil;

import android.os.Handler;
import android.text.TextUtils;

import com.miandui.data.DataUtil;
import com.miandui.data.UserInfo;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.netCollection.SelfNet;
import com.miandui.utils.TempUser;
import com.miandui.utils.base.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/3
 */

public class SelfNetUtils {

    public static void ModifyPersonalInfo(final Map<String, String> map, final String iamgePath, final BaseView baseView) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Formmat formmat = new Formmat(baseView, BaseUrl.BASE_URL + BaseUrl.MODIFT_INFO);
                    formmat.addFormField(map)
                            .setSuccessToast("修改成功");
                    if (!TextUtils.isEmpty(iamgePath)) {
                        formmat.addFilePart(NormalKey.head, new File(iamgePath));
                    }
                    formmat.setSimpleCallBack(new DefaultCallback(baseView) {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Handler handler = new Handler(baseView.getActivity().getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInfo(null);
                                    baseView.getActivity().finish();
                                }
                            }, 500);
                        }
                    });
                    formmat.doUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public static void refreshInfo(final SimpleCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        SelfNet.getInfoback(map, new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    DataUtil.saveFromJson(UserInfo.class, jsonObject.getJSONObject(NormalKey.content));
                    TempUser.reloadUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (callback != null) {
                    callback.onSuccess(jsonObject);
                }

            }

            @Override
            public void onFail(String code, String msg) {
                if (callback != null) {
                    callback.onFail(code, msg);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (callback != null) {
                    callback.onError(throwable);
                }
            }
        });
    }
}
