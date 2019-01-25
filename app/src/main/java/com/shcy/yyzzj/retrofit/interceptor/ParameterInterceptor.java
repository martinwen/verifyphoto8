package com.shcy.yyzzj.retrofit.interceptor;


import com.shcy.yyzzj.config.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by licong on 2018/9/25.
 */

public final class ParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl.Builder builder = original.url().newBuilder();
//        if (original.url().toString().contains(Constants.PHOTO_URL)) {
//            if (original.method().equals(NetCode.POST)) {
//                if (SetUtils.getInstance().isLogin()) {// 参数防篡改
//                    String code = DesUtil.randomNum();
//                    builder.addQueryParameter("code", code);
//                    builder.addQueryParameter("sct", DesUtil.createMD5(
//                            SetUtils.getInstance().getUser().getSid(), code));
//                    builder.addQueryParameter("sid", SetUtils.getInstance().getUser().getSid());
//                }
//            }
//        }
        if (original.url().toString().contains(Constants.PHOTO_URL)){
            builder = getParamsBuilder(builder);
        }
        HttpUrl httpUrl = builder.build();
        Request request = original.newBuilder()
                .url(httpUrl)
                .build();
        return chain.proceed(request);
    }

    public  HttpUrl.Builder getParamsBuilder(HttpUrl.Builder builder){
//        builder.addQueryParameter("vid", DeviceInfo.getInstance(MyApplication.getContext()).getSharePreImei());
//        builder.addQueryParameter("dv","android");
//        builder.addQueryParameter("os",android.os.Build.VERSION.RELEASE);
//        builder.addQueryParameter("rl",DeviceInfo.getInstance(MyApplication.getContext()).getDisplayWH());
//        builder.addQueryParameter("ac",DeviceInfo.getInstance(MyApplication.getContext()).GetNetworkType());
        return builder;
    }
}
