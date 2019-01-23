package com.verify.photo.module.share;

import android.app.Activity;
import android.os.Handler;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.verify.photo.bean.share.ShareContent;
import com.verify.photo.utils.ProgressBarUtil;

/**
 * Created by licong on 2018/4/24.
 */

public abstract class AShare {

    protected Activity activity;
    protected ProgressBarUtil dialog;
    UMShareAPI umShareAPI;
    private String id;
    private ShareManager.ShareCallBackListener callBackListener;//分享回调
    public static final int Expiredtoken = 21315;//token 过期
    public static final int Tokenexpired = 21327;//token 过期

    Handler mHandler = new Handler();


    protected abstract boolean isInstallApp();

    protected abstract void prepareShare(ShareContent shareContent);

    protected abstract void setShareContent(ShareContent shareContent);

    protected abstract void setShareContentApp(ShareContent shareContent);

    public void starShare(ShareContent shareContent,String id) {
        umShareAPI = UMShareAPI.get(activity);
        this.id = id;
        if (isInstallApp()) {
            showMessage("正在加载");
            prepareShare(shareContent);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endMessage();
                }
            }, 1000);

        }

    }

    public void shareApp(ShareContent shareContent) {
        umShareAPI = UMShareAPI.get(activity);
        if (isInstallApp()) {
            prepareShare(shareContent);
        }

    }

    protected abstract SHARE_MEDIA getShareMedia();

    protected UMShareListener getCallBackListener() {
        return new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                if (callBackListener != null) {
                    callBackListener.onComplete();
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable throwable) {
                endMessage();
                if (callBackListener != null) {
                    callBackListener.onError();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                endMessage();
            }
        };
    }

    private void showMessage(String t) {
        if (activity == null || activity.isFinishing())
            return;
        if (dialog == null) {
            dialog = ProgressBarUtil.create(activity);
        }
        this.dialog.show(t);
    }

    private void endMessage() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    /**
     * 分享回调监听
     *
     * @param callBackListener 回调监听
     */
    public void setCallBackListener(ShareManager.ShareCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public void closeDialoq() {
        if (dialog != null) {
            dialog.cancel();
        }
    }
}
