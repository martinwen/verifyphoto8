package com.shcy.yyzzj.bean.order;

import java.io.Serializable;

/**
 * Created by licong on 2018/10/10.
 */

public class Order implements Serializable{

    private int id;
    private int userId;//创建订单用户 id
    private String orderNumber;//订单编号
    private int status;//0—订单关闭 10-待支付 20-支付成功  21—代发货   22—已发货   24—已完成 30-已退款
    private String amount;//订单金额 单位：元
    private String refundAmount;//退款金额 单位：分
    private String createTime;
    private OrderPhoto photo;
    private OrderSpec spec;
    private String payTime;
    private String expireTime;//订单超时时间
    private String refundTime;
    private long expireUtc;

    private int type;//订单类型 1-证件照订单 2-冲印实物订单
    private int printCount;//冲印数量 默认为1 最小为1
    private String printAmount;//单件加印价格
    private String firstPrintAmount;//首版价格
    private String province;//所在省，自治区，直辖市
    private String city;//所在市
    private String detailedAddress;//详细地址
    private String district;//所在县区
    private String recipientsName;//联系人
    private String recipientsMobile;//联系电话
    private String expressName;//快递类型
    private String expressPrice;//快递价格
    private String expressCompany;//物流
    private String expressNumber;//物流单号
    private String deliveryTime;//发货时间
    private String finishTime;//订单结束时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public OrderPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(OrderPhoto photo) {
        this.photo = photo;
    }

    public OrderSpec getSpec() {
        return spec;
    }

    public void setSpec(OrderSpec spec) {
        this.spec = spec;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getExpireUtc() {
        return expireUtc;
    }

    public void setExpireUtc(long expireUtc) {
        this.expireUtc = expireUtc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public String getPrintAmount() {
        return printAmount;
    }

    public void setPrintAmount(String printAmount) {
        this.printAmount = printAmount;
    }

    public String getFirstPrintAmount() {
        return firstPrintAmount;
    }

    public void setFirstPrintAmount(String firstPrintAmount) {
        this.firstPrintAmount = firstPrintAmount;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRecipientsName() {
        return recipientsName;
    }

    public void setRecipientsName(String recipientsName) {
        this.recipientsName = recipientsName;
    }

    public String getRecipientsMobile() {
        return recipientsMobile;
    }

    public void setRecipientsMobile(String recipientsMobile) {
        this.recipientsMobile = recipientsMobile;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
