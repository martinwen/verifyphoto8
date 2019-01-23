package com.verify.photo.module.share;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.verify.photo.R;
import com.verify.photo.bean.share.ShareContent;
import com.verify.photo.utils.ToastUtil;

public class WeixinShare extends AShare {

    public WeixinShare(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected boolean isInstallApp() {
        if (!this.checkWeixin()) {
            ToastUtil.showToast(R.string.share_uninstall_wechat,true);
            return false;
        }
        return true;
    }

    @Override
    protected void prepareShare(ShareContent shareContent) {
        if (shareContent.isShareAPP) { //暂时方案，分享app
            setShareContentApp(shareContent);
            return;
        }
        setShareContent(shareContent);
    }

    @Override
    protected SHARE_MEDIA getShareMedia() {
        return SHARE_MEDIA.WEIXIN;
    }

    @Override
    public void setShareContent(ShareContent shareContent) {
        String subContent = "";
        UMImage image;
        if (TextUtils.isEmpty(shareContent.getSummary())) {
            subContent = "    ";
        } else {
            subContent = shareContent.getSummary();
        }
        if (TextUtils.isEmpty(shareContent.getIcon())) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
        } else {
            image = new UMImage(activity, shareContent.getIcon());
        }
        UMWeb web = new UMWeb(shareContent.getLinkUrl());
        web.setTitle(shareContent.getTitle());//标题
        web.setThumb(image);  //缩略图
        web.setDescription(subContent);
        new ShareAction(activity).setPlatform(getShareMedia())
                .withMedia(web)
                .setCallback(getCallBackListener())
                .share();
    }

    @Override
    public void setShareContentApp(ShareContent shareContent) {
        UMImage image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
        UMWeb web = new UMWeb(shareContent.getLinkUrl());
        web.setTitle(shareContent.getTitle());//标题
        web.setThumb(image);  //缩略图
        new ShareAction(activity).setPlatform(getShareMedia())
                .withMedia(web)
                .setCallback(getCallBackListener())
                .share();
    }

    private boolean checkWeixin() {
        return umShareAPI.isInstall(activity, getShareMedia());
    }
}
