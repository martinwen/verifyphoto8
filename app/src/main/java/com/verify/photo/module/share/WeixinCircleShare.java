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

public class WeixinCircleShare extends WeixinShare {

    public WeixinCircleShare(Activity activity) {
        super(activity);
    }

    @Override
    public void setShareContent(ShareContent shareContent) {
        String subContent = "";
        UMImage image;
        if (TextUtils.isEmpty(shareContent.getSummary())) {
            subContent = "   ";
        } else {
            subContent = shareContent.getSummary();
        }
        if (shareContent.isFlashShare()) {
            image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
        } else {
            if (TextUtils.isEmpty(shareContent.getIcon())) {
//				if (shareContent.isUseLogo){
                image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_logo));
//				}else{
//					image  = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.drawable.share_wx_default));
//				}
            } else {
                image = new UMImage(activity, shareContent.getIcon());
            }
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
        web.setDescription(shareContent.getSummary());
        new ShareAction(activity).setPlatform(getShareMedia())
                .withMedia(web)
                .setCallback(getCallBackListener())
                .share();
    }

    @Override
    protected SHARE_MEDIA getShareMedia() {
        return SHARE_MEDIA.WEIXIN_CIRCLE;
    }
}
