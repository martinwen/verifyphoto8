package com.shcy.yyzzj.retrofit.interceptor;


import android.text.TextUtils;

import com.shcy.yyzzj.activity.MyApplication;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.utils.PublicUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by licong on 2018/9/25.
 */

public final class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String schannel = PublicUtil.getChannelMap().get(PublicUtil.getChannelName(MyApplication.getContext()));
        String contnet = (TextUtils.isEmpty(schannel) ? "3000" : schannel);
        Request original = chain.request();
        Request request = original.newBuilder()
                //通过Interceptor来定义静态请求头
                //header()如果有重名的将会覆盖，
                .header("Content-type", "application/json;charset=utf-8")
                .header("accessToken", Constants.TOKEN)
                .header("buildNo", PublicUtil.getAppPackageInfo().versionCode + "")
                .header("version", PublicUtil.getAppPackageInfo().versionName)
                .header("clientType", "Android")
                .header("deviceNo", Constants.getPsuedoID())
                .header("channelCode", contnet)
                .header("packageName", PublicUtil.getAppPackageInfo().packageName)
//                .addHeader("version",Constants.getHttpHeader())
                //addHeader()允许相同key值的header存在
                .build();
        return chain.proceed(request);
    }
}
