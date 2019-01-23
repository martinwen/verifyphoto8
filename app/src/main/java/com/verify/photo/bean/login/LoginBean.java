package com.verify.photo.bean.login;

/**
 * Created by licong on 2018/10/11.
 */

public class LoginBean {
    private User user;
    private int loginStatus;//0-临时 1-正式登录
    private String accessToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
