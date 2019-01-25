package com.shcy.yyzzj.bean.preview;

import java.io.Serializable;

/**
 * Created by licong on 2018/10/9.
 */

public class PreviewPhotoBean implements Serializable{
    private String color;
    private String colorTone;
    private String photoNumber;
    private String photoUrl;
    private String amount;
    private int chekedStatus;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorTone() {
        return colorTone;
    }

    public void setColorTone(String colorTone) {
        this.colorTone = colorTone;
    }

    public String getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(String photoNumber) {
        this.photoNumber = photoNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getChekedStatus() {
        return chekedStatus;
    }

    public void setChekedStatus(int chekedStatus) {
        this.chekedStatus = chekedStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
