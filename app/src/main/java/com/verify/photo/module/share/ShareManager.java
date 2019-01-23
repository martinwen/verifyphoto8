package com.verify.photo.module.share;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.umeng.socialize.UMShareAPI;
import com.verify.photo.R;
import com.verify.photo.bean.share.ShareContent;
import com.verify.photo.dialog.SpringDialog;
import com.verify.photo.utils.ToastUtil;


/**
 * Created by licong on 2018/4/24.
 */

public class ShareManager implements View.OnClickListener {
    private Activity activity;
    private SpringDialog springDialog;
    private ShareContent shareContent;
    private String id;
    protected AShare qzoneShare;
    protected AShare weixinShare;
    protected AShare weixinCircleShare;
    protected AShare qqShare;
    protected AShare weiboShare;

    public void shareContent(ShareContent shareContent, String id) {
        this.shareContent = shareContent;
        this.id = id;
        initSpringDialog();
        springDialog.show();
    }

    public void setShareContent(ShareContent shareContent) {
        this.shareContent = shareContent;
    }

    public void startWx(ShareContent shareContent) {
        this.shareContent = shareContent;
        weixinShare();
    }

    public void startPyq(ShareContent shareContent) {
        this.shareContent = shareContent;
        weixinCircleShare();
    }

    public ShareManager(Activity activity, ShareCallBackListener callBackListener) {
        this.activity = activity;
        qzoneShare = new QzoneShare(activity);
        weixinShare = new WeixinShare(activity);
        weixinCircleShare = new WeixinCircleShare(activity);
        qqShare = new QQShare(activity);
        weiboShare = new WeiboShare(activity);
        qzoneShare.setCallBackListener(callBackListener);
        weixinShare.setCallBackListener(callBackListener);
        weixinCircleShare.setCallBackListener(callBackListener);
        qqShare.setCallBackListener(callBackListener);
        weiboShare.setCallBackListener(callBackListener);
    }

    public ShareManager(Activity activity) {
        this.activity = activity;
        qzoneShare = new QzoneShare(activity);
        weixinShare = new WeixinShare(activity);
        weixinCircleShare = new WeixinCircleShare(activity);
        qqShare = new QQShare(activity);
        weiboShare = new WeiboShare(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inner_share_weibo:
                weiboShare();
                break;
            case R.id.inner_share_ten_qzone:
                qqZoneShare();
                break;
            case R.id.inner_share_ten_qq:
                qqShare();
                break;
            case R.id.inner_share_weixin:
//				Config.IsToastTip = false;
                weixinShare();
                break;
            case R.id.inner_share_weixinhaoyou:
//				Config.IsToastTip = false;
                weixinCircleShare();
                break;
            case R.id.inner_share_copylink:
                ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(shareContent.getLinkUrl());
                ToastUtil.showToast("复制成功");
                break;
            case R.id.share_dialog_cancel:
                break;
            default:
                break;
        }

        if (springDialog.isShowing()) {
            springDialog.dismiss();
        }
    }

    public interface ShareCallBackListener {
        void onComplete();

        void onError();
    }

    private void initSpringDialog() {
        springDialog = new SpringDialog(activity,
                R.style.fn_fullsreen_dialog_tra);
        springDialog.setContentView(R.layout.fn_share_ui);
        springDialog.setLayout(activity.getWindowManager(),
                activity.getWindow(), null, null);
        springDialog.setCancelable(true);
        springDialog.setCanceledOnTouchOutside(true);
        updateUi();
    }

    private void updateUi() {
        springDialog.findViewById(R.id.inner_share_weibo).setOnClickListener(
                this);
        springDialog.findViewById(R.id.inner_share_weixin).setOnClickListener(
                this);
        springDialog.findViewById(R.id.inner_share_weixinhaoyou)
                .setOnClickListener(this);
        springDialog.findViewById(R.id.inner_share_ten_qzone)
                .setOnClickListener(this);
        springDialog.findViewById(R.id.inner_share_ten_qq)
                .setOnClickListener(this);
        springDialog.findViewById(R.id.inner_share_copylink)
                .setOnClickListener(this);
        springDialog.findViewById(R.id.share_dialog_cancel)
                .setOnClickListener(this);

    }

    public void weiboShare() {
        weiboShare.starShare(shareContent, id);
    }

    public void qqShare() {
        this.qqShare.starShare(this.shareContent, id);
    }

    public void qqZoneShare() {
        this.qzoneShare.starShare(this.shareContent, id);
    }

    public void weixinShare() {
        this.weixinShare.starShare(this.shareContent, id);
    }

    public void weixinCircleShare() {
        this.weixinCircleShare.starShare(this.shareContent, id);
    }

    //分享回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (activity == null) {
            return;
        }
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

}
