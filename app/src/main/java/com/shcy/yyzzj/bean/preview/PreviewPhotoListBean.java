package com.shcy.yyzzj.bean.preview;

import java.io.Serializable;
import java.util.List;

/**
 * Created by licong on 2018/10/9.
 */

public class PreviewPhotoListBean implements Serializable{
    private int width;
    private int heigth;
    List<PreviewPhotoBean> photoList;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public List<PreviewPhotoBean> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PreviewPhotoBean> photoList) {
        this.photoList = photoList;
    }
}
