package com.verify.photo.retrofit.interceptor;


import android.text.TextUtils;

import com.verify.photo.activity.MyApplication;
import com.verify.photo.config.Constants;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.SetUtils;

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
