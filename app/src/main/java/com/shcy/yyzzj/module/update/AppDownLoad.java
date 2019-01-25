package com.shcy.yyzzj.module.update;

import android.app.Activity;
import android.text.TextUtils;


import com.shcy.yyzzj.activity.MyApplication;
import com.shcy.yyzzj.bean.VersionBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.DeviceInfo;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.utils.MD5Helper;
import com.shcy.yyzzj.utils.ProgressBarUtil;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.SetUtils;
import com.shcy.yyzzj.utils.ToastUtil;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppDownLoad {

    private boolean isBackstageCheck = true; // 为了区分检测时是否显示进度条
    private boolean isCheck = false; // 防止重复点击更新APP
    private ProgressBarUtil pb;

    private Activity activity;


    private AppDownLoad(Activity activity) {
        this.activity = activity;
    }

    public AppDownLoad setBackstageCheck(boolean isBackstageCheck) {
        this.isBackstageCheck = isBackstageCheck;
        if (!this.isBackstageCheck) {
            pb = ProgressBarUtil.create(activity);
        }
        return this;
    }

    public static AppDownLoad create(Activity activity) {
        return new AppDownLoad(activity);
    }

    public void checkVersion() {
        if (!this.isBackstageCheck) {
            pb.show("正在检测新版本...");
        }
        if (!this.isCheck) {
            this.isCheck = true;
            String schannel = PublicUtil.getChannelMap().get(PublicUtil.getChannelName(MyApplication.getContext()));
            String contnet = (TextUtils.isEmpty(schannel) ? "3000" : schannel);
            PhotoHttpManger.getPhotoApi()
                    .AppCheckApi(contnet, PublicUtil.getAppPackageInfo().packageName)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResultSub<VersionBean>() {
                        @Override
                        public void onSuccsess(HttpResult<VersionBean> data) {
                            isCheck = false;
                            if (pb != null) {
                                pb.cancel();
                            }
                            setData(data);
                        }

                        @Override
                        public void onFilad(NetException e) {
                            if (pb != null) {
                                pb.cancel();
                            }
                            ToastUtil.showToast(e.toastMsg);
                        }
                    });
        }
    }

    private void setData(HttpResult<VersionBean> data) {
        if (data.isSucess()) {
            VersionBean vbean = data.getData();
            SetUtils.getInstance().setBuildNo(vbean.getBuildNo());
            SetUtils.getInstance().setForce(vbean.getForceUpdate());
            if (!TextUtils.isEmpty(vbean.getBuildNo()) && Integer.parseInt(DeviceInfo.getInstance(MyApplication.getContext()).getVersionCode()) < (Integer.parseInt(vbean.getBuildNo()))) {
                SetUtils.getInstance().setIsupdate(true);
                if (!activity.isFinishing()) {
                    DialogUtil.showDialogForDownloadApp(activity,
                            vbean.getDownloadUrl(), vbean.getDescribe());

                }
            } else {
                SetUtils.getInstance().setIsupdate(false);
            }
        } else {
            if (!isBackstageCheck) {
                ToastUtil.showToast("网络不给力", true);
            }
        }

    }

    private boolean isExistLocApk(String md5) {
        if (TextUtils.isEmpty(md5)) { // 如果接口里没有md5,则立即下载APK
            return true;
        }
        if (!new File(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk").exists()) {
            return false;
        }
        try {
            String locationMD5 = MD5Helper
                    .getMD5Checksum(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk");
            if (md5.equals(locationMD5)) { // MD5相同，则已经下载好APK了，可以按装
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
