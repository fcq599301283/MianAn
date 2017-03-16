package com.mianan.netWork;

import android.content.Context;


import com.mianan.netWork.netUtil.BaseUrl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by FengChaoQun
 * on 2016/8/3
 * 所有网络操作统一管理类
 */
public class RetrofitFactory {
    private static final int DEFAULT_TIME = 8;            //默认超时为8s
    private static Retrofit retrofit;
    private static Retrofit retrofit_with_header;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            //添加日志
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseUrl.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitWithHeader(final Context context) {
        if (retrofit_with_header == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
//                                    .addHeader("Content-Type", "application/json")
//                                    .addHeader("User-Phone", (String) SPUtils.get(context, SPUtils.PHONENUMNBER, SPUtils.DEFAULT_STRING))
//                                    .addHeader("User-Digest", (String) SPUtils.get(context, SPUtils.DIGEST, SPUtils.DEFAULT_STRING))
                                    .build();
//                            Log.d("digest2", (String) SPUtils.get(context, SPUtils.DIGEST, SPUtils.DEFAULT_STRING));
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                    .build();
            retrofit_with_header = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseUrl.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit_with_header;
    }

}
