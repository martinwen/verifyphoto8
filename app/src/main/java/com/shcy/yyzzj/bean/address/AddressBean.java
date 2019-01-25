package com.shcy.yyzzj.bean.address;

import java.io.Serializable;

/**
 * Created by licong on 2018/11/22.
 */

public class AddressBean implements Serializable{
    private int areaId;
    private String province;
    private String city;
    private String detailedAddress;
    private String district;
    private int id;
    private String recipientsName;
    private String recipientsMobile;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
