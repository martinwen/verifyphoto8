package com.shcy.yyzzj.bean.album;

/**
 * Created by licong on 2018/9/29.
 */

public class ALbumBean {
    private String image;
    private String createTime;
    private int width;
    private int height;
    private int id;
    private String specName;
    private int includeCount;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getIncludeCount() {
        return includeCount;
    }

    public void setIncludeCount(int includeCount) {
        this.includeCount = includeCount;
    }
}
