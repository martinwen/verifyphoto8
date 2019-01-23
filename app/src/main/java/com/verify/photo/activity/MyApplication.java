package com.verify.photo.activity;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.verify.photo.config.Constants;
import com.verify.photo.utils.CommontUtil;
import com.verify.photo.utils.SetUtils;
import com.verify.photo.utils.fresco.FrescoUtils;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by licong on 2018/9/25.
 */

public class MyApplication extends Application {

    public static MainActivity mainActivity;

    private static Context sContext;

    {
        PlatformConfig.setWeixin(Constants.WX_APPID, Constants.WX_APPSCRET);
        PlatformConfig.setQQZone(Constants.QQ_APPID, Constants.QQ_APPKEY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CommontUtil.init(this);
        sContext = getApplicationContext();
        Constants.TOKEN = SetUtils.getInstance().getToken();
        Fresco.initialize(this, FrescoUtils.getInstance().getFrescoConfig(this));
        initX5WebViewCore();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, Constants.UM_APPKEY);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    public static Context getContext() {
        return sContext;
    }

    private void initX5WebViewCore() {
        QbSdk.setDownloadWithoutWifi(true);//非WiFi下载内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
