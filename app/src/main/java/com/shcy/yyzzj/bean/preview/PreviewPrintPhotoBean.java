package com.shcy.yyzzj.bean.preview;

import java.io.Serializable;

/**
 * Created by licong on 2018/11/22.
 */

public class PreviewPrintPhotoBean implements Serializable {
    private String printPhotoUrl;
    private int includeCount;
    private PreviewPrintPhotoSpecBean spec;
    private String price;
    private String printPrice;

    public String getPrintPhotoUrl() {
        return printPhotoUrl;
    }

    public void setPrintPhotoUrl(String printPhotoUrl) {
        this.printPhotoUrl = printPhotoUrl;
    }

    public int getIncludeCount() {
        return includeCount;
    }

    public void setIncludeCount(int includeCount) {
        this.includeCount = includeCount;
    }

    public PreviewPrintPhotoSpecBean getSpec() {
        return spec;
    }

    public void setSpec(PreviewPrintPhotoSpecBean spec) {
        this.spec = spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrintPrice() {
        return printPrice;
    }

    public void setPrintPrice(String printPrice) {
        this.printPrice = printPrice;
    }
}
