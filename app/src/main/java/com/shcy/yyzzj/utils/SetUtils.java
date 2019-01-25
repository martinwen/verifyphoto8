package com.shcy.yyzzj.utils;

import com.shcy.yyzzj.activity.MyApplication;

/**
 * @author 设置及状态
 */
public class SetUtils {
    private static SetUtils mInstance;
    PreferenceUtils mUtils;

    String key_is_night = "fork_is_night";//夜间模式
    String key_font = "fork_font";//字体大小
    String key_mail = "fork_phone";//手机号
    String key_token = "fork_token";//手机号token
    String key_is_login = "key_is_login";
    String key_push_tags_status = "key_push_tags_status";
    String key_lodding_avatar = "key_lodding_avatar";
    String key_first_app = "key_first_app";
    String key_hasshow_camera_lunbo = "key_hasshow_camera_lunbo";

    String key_isshow_guide = "key_isshow_guide";
    String key_version = "key_version";
    String key_forceUpdate = "key_forceUpdate";
    String key_isupdate = "key_isupdate";

    String key_download = "key_download";

    String key_qiniuDomain = "key_qiniuDomain";

    String key_wifi_play_new = "key_wifi_play_new"; //0 关闭 1.仅wifi 2.数据+wifi
    String key_update_time = "key_update_time"; //记录时间 判断更新用

    String key_province_download_time = "key_province_download_time";//省市区json下载事件

    private SetUtils() {
        mUtils = new PreferenceUtils("photo_globel_set");
    }

    public static SetUtils getInstance() {
        if (mInstance == null)
            mInstance = new SetUtils();
        return mInstance;
    }

    public void setBuildNo(String string) {
        mUtils.putString(key_version, string);
    }

    public String getBuildNo() {
        return mUtils.getString(key_version, Integer.parseInt(DeviceInfo.getInstance(MyApplication.getContext()).getVersionCode()) + "");
    }

    public String getEm() {
        return mUtils.getString(key_mail, "");
    }


    public void setEm(String em) {
        mUtils.putString(key_mail, em);
    }

    public void setToken(String token) {
        mUtils.putString(key_token, token);
    }

    public String getToken() {
        return mUtils.getString(key_token, "");
    }


    public void setNight(boolean is) {
        mUtils.putBoolean(key_is_night, is);
    }

    public boolean isNgiht() {
        return mUtils.getBoolean(key_is_night, false);
    }

    public void setIsLogin(boolean is) {
        mUtils.putBoolean(key_is_login, is);
    }

    public boolean getIsLogin() {
        return mUtils.getBoolean(key_is_login, false);
    }


    public void setFont(int string) {
        mUtils.putInt(key_font, string);
    }

    public int getFont() {
        return mUtils.getInt(key_font, 15);

    }

    public void setIsupdate(boolean boo) {
        mUtils.putBoolean(key_isupdate, boo);
    }

    public boolean getIsupdate() {
        return mUtils.getBoolean(key_isupdate, false);
    }

    public void setForce(String string) {
        mUtils.putString(key_forceUpdate, string);
    }

    public String getForce() {
        return mUtils.getString(key_forceUpdate, "0");
    }

    public void putIsEnter(boolean b) {
        mUtils.putBoolean("isEnter", b);
    }

    public boolean getIsEnter() {
        return mUtils.getBoolean("isEnter", true);
    }

    public void setLoddingAvatar(String avatar) {
        mUtils.putString(key_lodding_avatar, avatar);
    }

    public String getLoddingAvatar() {
        return mUtils.getString(key_lodding_avatar, "");
    }

    public void setFirstApp(boolean isfirst) {
        mUtils.putBoolean(key_first_app, isfirst);
    }

    public boolean getFirstApp() {
        return mUtils.getBoolean(key_first_app, true);
    }

    public void setIsShowGuide(boolean isShowGuide) {
        mUtils.putBoolean(key_isshow_guide, isShowGuide);
    }

    public boolean getIsShowGuide() {
        return mUtils.getBoolean(key_isshow_guide, true);
    }

    /**
     * 0 未下载 1 下载中 2下载完成
     *
     * @param s
     */
    public void setdownload(String s) {
        mUtils.putString(key_download, s);
    }

    public String getdownload() {
        return mUtils.getString(key_download, "0");
    }

    public void setIsWifiPlay(String s) {
        mUtils.putString(key_wifi_play_new, s);
    }

    public String getIsWifiPlay() {
        return mUtils.getString(key_wifi_play_new, "0");
    }


    public void setKeyQiniuDomain(String qiniuDomain) {
        mUtils.putString(key_qiniuDomain, qiniuDomain);
    }

    public String getKeyQiniuDomain() {
        return mUtils.getString(key_qiniuDomain, "");
    }

    public void setTimeUpdate(String qiniuDomain) {
        mUtils.putString(key_update_time, qiniuDomain);
    }

    public String getTimeUpdate() {
        return mUtils.getString(key_update_time, "");
    }


    public void setProvinceDownLoadTime(String time) {
        mUtils.putString(key_province_download_time, time);
    }

    public String getProvinceDownLoadTime() {
        return mUtils.getString(key_province_download_time, "0");
    }

    public void setCameraLunboHasShow(boolean hasshow) {
        mUtils.putBoolean(key_hasshow_camera_lunbo, hasshow);
    }

    public boolean getCameraLunboHasShow() {
        return mUtils.getBoolean(key_hasshow_camera_lunbo, false);
    }
}
