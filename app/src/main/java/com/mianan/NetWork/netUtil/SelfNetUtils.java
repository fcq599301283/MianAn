package com.mianan.NetWork.netUtil;

import android.os.Handler;
import android.text.TextUtils;

import com.mianan.NetWork.NetCollection.SelfNet;
import com.mianan.NetWork.callBack.DefaultCallback;
import com.mianan.data.DataUtil;
import com.mianan.data.UserInfo;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseView;

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
                                    refreshInfo(baseView);
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

    public static void refreshInfo(BaseView baseView) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        SelfNet.getInfoback(map, new DefaultCallback(baseView) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    DataUtil.saveFromJson(UserInfo.class, jsonObject.getJSONObject(NormalKey.content));
                    TempUser.reloadUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
