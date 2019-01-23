package com.verify.photo.bean.share;

import java.io.Serializable;

/**
 * Created by licong on 2018/4/24.
 */

public class ShareContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String linkUrl;
    private String icon;
    private String title;
    private String summary;
    public boolean isShareAPP = false; //分享App时的
    public boolean isFlashShare; //快讯分享

    public ShareContent(){

    }

    public boolean isFlashShare() {
        return isFlashShare;
    }

    public void setFlashShare(boolean flashShare) {
        isFlashShare = flashShare;
    }

    public ShareContent(String linkUrl, String icon, String title, String summary) {
        this.linkUrl = linkUrl;
        this.icon = icon;
        this.title = title;
        this.summary = summary;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

}
