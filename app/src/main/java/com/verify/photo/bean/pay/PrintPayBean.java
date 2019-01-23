package com.verify.photo.bean.pay;

import java.io.Serializable;

/**
 * Created by licong on 2018/12/8.
 */

public class PrintPayBean implements Serializable{
    private String photoname;
    private String idnumber;
    private int includecount;
    private String url;
    private int type;//1相册 2正常下单

    public String getPhotoname() {
        return photoname;
    }

    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public int getIncludecount() {
        return includecount;
    }

    public void setIncludecount(int includecount) {
        this.includecount = includecount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
