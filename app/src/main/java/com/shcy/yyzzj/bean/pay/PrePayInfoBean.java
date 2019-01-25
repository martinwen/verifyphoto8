package com.shcy.yyzzj.bean.pay;

/**
 * Created by licong on 2018/10/12.
 */

public class PrePayInfoBean {
    private String orderNumber;
    private String expireTime;
    private String payType;//1-微信支付 2-支付宝支付
    private WechatPayParameter weixinParameter;
    private String alipayParameter;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public WechatPayParameter getWeixinParameter() {
        return weixinParameter;
    }

    public void setWeixinParameter(WechatPayParameter weixinParameter) {
        this.weixinParameter = weixinParameter;
    }

    public String getAlipayParameter() {
        return alipayParameter;
    }

    public void setAlipayParameter(String alipayParameter) {
        this.alipayParameter = alipayParameter;
    }
}
