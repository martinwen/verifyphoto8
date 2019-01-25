package com.shcy.yyzzj.module.share;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.share.ShareContent;
import com.shcy.yyzzj.utils.ToastUtil;

public class QQShare extends AShare {
    public QQShare(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected boolean isInstallApp() {
        if (!isInstallQQ()) {
            ToastUtil.showToast(R.string.share_uninstall_qq,true);
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
    private boolean isInstallQQ() {
        return umShareAPI.isInstall(activity, SHARE_MEDIA.QQ);
    }
    @Override
    protected SHARE_MEDIA getShareMedia() {
        return SHARE_MEDIA.QQ;
    }
    @Override
    public void setShareContent(ShareContent shareContent) {
        UMImage image;
        String summury;//QQ 分享内容不能为空
        if (shareContent.isFlashShare()) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
        } else {
            if (TextUtils.isEmpty(shareContent.getIcon())) {
//                if (shareContent.isUseLogo){
                image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
//                }else{
//                    image  = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.drawable.share_wx_default));
//                }
            } else {
                image = new UMImage(activity, shareContent.getIcon());
            }
        }
        if (TextUtils.isEmpty(shareContent.getSummary())) {
            summury = "   ";
        } else {
            summury = shareContent.getSummary();
        }
        UMWeb web = new UMWeb(shareContent.getLinkUrl());
        web.setTitle(shareContent.getTitle());//标题
        web.setThumb(image);  //缩略图
        web.setDescription(summury);
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
