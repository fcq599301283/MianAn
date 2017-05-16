package com.miandui.netWork.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 */

public interface NormalPostAPI {
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String path, @FieldMap Map<String, String> map);
}
