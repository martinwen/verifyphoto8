package com.shcy.yyzzj.utils;


import com.shcy.yyzzj.bean.login.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class UserUtil {

    private static UserUtil mInstance;
    PreferenceUtils mUtils;
    String userNickName = "userNickName";
    String gender = "gender";
    String mobile = "mobile";
    String avatar = "avatar";
    String userSignature = "userSignature";
    String wxOpenId = "wxOpenId";
    String qqOpenId = "qqOpenId";
    String weiBoOpenId = "weiBoOpenId";
    String id = "id";
    String loginStatus = "loginStatus";



    public UserUtil() {
        this.mUtils = new PreferenceUtils("user_info");
    }

    public static UserUtil getInstance() {
        if (mInstance == null)
            mInstance = new UserUtil();
        return mInstance;
    }

    public String getUserNickName() {
        return mUtils.getString(userNickName, "");
    }

    public void setUserNickName(String userNickName) {
        this.mUtils.putString(this.userNickName, userNickName);
    }

    public int getGender() {
        return mUtils.getInt(gender,0);
    }

    public void setGender(int gender) {
        this.mUtils.putInt(this.gender, gender);
    }

    public String getMobile() {
        return mUtils.getString(mobile, "");
    }

    public void setMobile(String mobile) {
        this.mUtils.putString(this.mobile, mobile);
    }

    public String getAvatar() {
        return mUtils.getString(avatar, "");
    }

    public void setAvatar(String avatar) {
        this.mUtils.putString(this.avatar, avatar);
    }

    public String getUserSignature() {
        return mUtils.getString(userSignature, "");
    }

    public void setUserSignature(String userSignature) {
        this.mUtils.putString(this.userSignature, userSignature);
    }

    public String getWxOpenId() {
        return mUtils.getString(wxOpenId, "");
    }

    public void setWxOpenId(String wxOpenId) {
        mUtils.putString(this.wxOpenId, wxOpenId);
    }

    public String getQqOpenId() {
        return mUtils.getString(qqOpenId, "");
    }

    public void setQqOpenId(String qqOpenId) {
        mUtils.putString(this.qqOpenId, qqOpenId);
    }

    public int geId() {
        return mUtils.getInt(id, 0);
    }

    public void setId(int userId) {
        mUtils.putInt(this.id, userId);
    }


    public String getWeiBoOpenId() {
        return mUtils.getString(weiBoOpenId, "");
    }

    public void setWeiBoOpenId(String weiBoOpenId) {
        mUtils.putString(this.weiBoOpenId, weiBoOpenId);
    }

    public int geloginStatus() {
        return mUtils.getInt(loginStatus, 1);
    }

    public void setLoginStatus(int loginStatus) {
        mUtils.putInt(this.loginStatus, loginStatus);
    }

    public void saveAll(User data) {
        setUserNickName(data.getNickname());
        setGender(data.getGender());
        setAvatar(data.getAvatar());
    }

    public void clearAll() {
        this.mUtils.clear();
    }

    public String getDateTime() {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(now);
    }
}
