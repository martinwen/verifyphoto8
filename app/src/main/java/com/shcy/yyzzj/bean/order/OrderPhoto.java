package com.shcy.yyzzj.bean.order;

import java.io.Serializable;

/**
 * Created by licong on 2018/10/10.
 */

public class OrderPhoto implements Serializable{
    int id;
    String image;
    String width;
    String height;
    String createTime;
    private String printImage;
    private int includeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrintImage() {
        return printImage;
    }

    public void setPrintImage(String printImage) {
        this.printImage = printImage;
    }

    public int getIncludeCount() {
        return includeCount;
    }

    public void setIncludeCount(int includeCount) {
        this.includeCount = includeCount;
    }
}
