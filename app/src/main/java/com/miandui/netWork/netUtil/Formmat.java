package com.miandui.netWork.netUtil;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.utils.base.BaseView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2016/9/9
 */
public class Formmat {
    private BaseView baseView;
    private String path;
    private MultipartUtility multipartUtility;
    private Handler handler;
    private SimpleCallback simpleCallBack;
    private String successToast;
    private String Failmsg;

    public Formmat(final BaseView baseView1, String path) throws IOException {
        this.baseView = baseView1;
        this.path = path;
        handler = new Handler(baseView1.getActivity().getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        baseView.showLoadingDialog("上传中...");
                        break;
                    case 2:
                        if (simpleCallBack != null) {
                            simpleCallBack.onSuccess(null);
                        }
                        baseView.hideLoadingDialog();
                        if (successToast != null) {
                            baseView.showToast(successToast);
                        } else {
                            baseView.showToast("发布成功");
                        }

                        break;
                    case 3:
                        baseView.showToast("服务器异常");
                        baseView.hideLoadingDialog();
                        break;
                    case 4:
                        if (Failmsg != null) {
                            baseView.showToast(Failmsg);
                        }
                        baseView.hideLoadingDialog();
                        break;
                    case 5:
                        baseView.showToast("数据解析异常");
                        baseView.hideLoadingDialog();
                    default:
                        baseView.hideLoadingDialog();
                        baseView.showToast("" + msg.what);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        multipartUtility = new MultipartUtility(path, "UTF-8");
    }

    public Formmat addFormField(String name, String value) {
        multipartUtility.addFormField(name, value);
        return this;
    }

    public Formmat addFilePart(String fieldName, File uploadFile) throws IOException {
        multipartUtility.addFilePart(fieldName, uploadFile);
        return this;
    }

    public Formmat addFormField(Map<String, String> map) {
        for (String key : map.keySet()) {
            if (!TextUtils.isEmpty(map.get(key))) {
                addFormField(key, map.get(key));
            }
        }
        return this;
    }

//    public Formmat addFilePart(List<String> list, Context context) throws IOException {
//        if (list == null || list.size() < 1) {
//            return this;
//        }
//        startLoading();
//
//        for (String path : list) {
//            if (!TextUtils.isEmpty(path)) {
//                multipartUtility.addFilePart("images", SendImageHelper.getLittleImage(path, context));
//            }
//        }
//        return this;
//    }
//
//    public Formmat addFilePart(List<String> list, String key) throws IOException {
//        if (list == null || list.size() < 1) {
//            return this;
//        }
//        startLoading();
//
//        for (String path : list) {
//            if (!TextUtils.isEmpty(path)) {
//                multipartUtility.addFilePart(key, SendImageHelper.getLittleImage(path, context));
//            }
//        }
//        return this;
//    }

    public void doUpload() {
        try {
            startLoading();

            List<String> result = multipartUtility.finish();
            String result_String = result.toString();
            Log.d("result", "--" + result_String);

            JSONObject jsonObject = null;
            try {
                JSONArray jsonArray = new JSONArray(result_String);
                jsonObject = jsonArray.getJSONObject(0);
                switch (jsonObject.getInt(NormalKey.code)) {
                    case 200:
                        endLoading(2);
                        break;
                    default:
                        Failmsg = jsonObject.getString(NormalKey.msg);
                        endLoading(4);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                endLoading(5);
            }
        } catch (IOException e) {
            e.printStackTrace();
            endLoading(3);
        }
    }

    private void startLoading() {
        Message start = new Message();
        start.what = 1;
        handler.sendMessage(start);
    }

    private void endLoading(int i) {
        Message end = new Message();
        end.what = i;
        handler.sendMessage(end);
    }

    public SimpleCallback getSimpleCallBack() {
        return simpleCallBack;
    }

    public void setSimpleCallBack(SimpleCallback simpleCallBack) {
        this.simpleCallBack = simpleCallBack;
    }

    public void setSuccessToast(String successToast) {
        this.successToast = successToast;
    }
}
