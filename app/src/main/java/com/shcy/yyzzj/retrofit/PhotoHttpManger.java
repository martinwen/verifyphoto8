package com.shcy.yyzzj.retrofit;



import com.shcy.yyzzj.api.PhotoApi;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.convert.FastJsonConverterFactory;
import com.shcy.yyzzj.retrofit.interceptor.HeaderInterceptor;
import com.shcy.yyzzj.retrofit.interceptor.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 * Created by licong on 2018/9/25.
 */

public class PhotoHttpManger {
    private final static long DEFAULT_TIMEOUT = 30;

    public PhotoHttpManger() {
    }

    private static class SingletonHolder {
        private static final PhotoHttpManger INSTANCE = new PhotoHttpManger();
    }

    public static PhotoHttpManger getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public <T> T createApiService(String baseUrl, Class<T> iApiService) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .client(getOkHttpClient()).build().create(iApiService);
    }

    private OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        // 添加请求头
        mBuilder.addInterceptor(new HeaderInterceptor());
//        // 添加参数
//        mBuilder.addInterceptor(new ParameterInterceptor());
//        //添加日志
        mBuilder.addInterceptor(new LoggerInterceptor());
//
//        mBuilder.cookieJar(NetCookieJar.create());

//        mBuilder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean shcy(String hostname, SSLSession session) {
//                if (hostname.equals("forknews")){
//                    return true;
//                }else {
//                    return true;
//                }
//            }
//        });

        // 失败重试
//        mBuilder.retryOnConnectionFailure(true);
        mBuilder.retryOnConnectionFailure(false);

        //设置超时时间
        mBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        return mBuilder.build();
    }


    public static PhotoApi getPhotoApi() {
        return SingletonHolder.INSTANCE.createApiService(Constants.PHOTO_URL, PhotoApi.class);
    }
}
