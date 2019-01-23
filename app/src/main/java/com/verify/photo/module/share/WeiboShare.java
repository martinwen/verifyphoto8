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

public class WeiboShare extends AShare {

    public WeiboShare(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected boolean isInstallApp() {
        if (!isInstallQQ()) {
            ToastUtil.showToast(R.string.share_uninstall_weibo,true);
            return false;
        }
        return true;
    }

    private boolean isInstallQQ() {
        return umShareAPI.isInstall(activity, SHARE_MEDIA.SINA);
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
        return SHARE_MEDIA.SINA;
    }

    @Override
    public void setShareContent(ShareContent shareContent) {
        UMImage image;
        if (shareContent.isFlashShare()) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
        } else {
            if (TextUtils.isEmpty(shareContent.getIcon())) {
                image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
            } else {
                image = new UMImage(activity, shareContent.getIcon());
            }
        }
        UMWeb web = new UMWeb(shareContent.getLinkUrl());
        web.setTitle(shareContent.getTitle());//标题
        web.setThumb(image);  //缩略图
        web.setDescription(shareContent.getTitle() + "( 来自 @眺望 )" + shareContent.getSummary());
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
        web.setDescription(shareContent.getSummary());
        new ShareAction(activity).setPlatform(getShareMedia())
                .withMedia(web)
                .setCallback(getCallBackListener())
                .share();
    }
}
