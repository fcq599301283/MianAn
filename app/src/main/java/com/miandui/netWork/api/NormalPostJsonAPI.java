package com.miandui.netWork.api;

import com.miandui.data.UploadRecordBean;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 */

public interface NormalPostJsonAPI {
    @POST
    Observable<ResponseBody> post(@Url String path, @Body UploadRecordBean jsonObject);
}
