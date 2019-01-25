package com.shcy.yyzzj.bean.pay;

/**
 * Created by licong on 2018/12/10.
 */

public class PrintOrderPrice {
    private String totalAmount;
    private String expressName;
    private String printAmount;
    private int expressType;
    private int printCount;
    private String firstPrintAmount;
    private String expressPrice;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getPrintAmount() {
        return printAmount;
    }

    public void setPrintAmount(String printAmount) {
        this.printAmount = printAmount;
    }

    public int getExpressType() {
        return expressType;
    }

    public void setExpressType(int expressType) {
        this.expressType = expressType;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public String getFirstPrintAmount() {
        return firstPrintAmount;
    }

    public void setFirstPrintAmount(String firstPrintAmount) {
        this.firstPrintAmount = firstPrintAmount;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }
}
