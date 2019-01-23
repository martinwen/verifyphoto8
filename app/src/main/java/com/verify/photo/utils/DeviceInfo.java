package com.verify.photo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.verify.photo.config.Constants;
import com.verify.photo.log.L;


/**
 */
public class DeviceInfo {
    private static DeviceInfo mInstance;
    private static Context mContext;

    public DeviceInfo(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static DeviceInfo getInstance(Context context) {
        if (mInstance == null){
            mInstance = new DeviceInfo(context);
        }
        return mInstance;
    }

    public TelephonyManager getTm() {
        return (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

//    public String getImei() {
//        if (getTm().getDeviceId() == null) return "";
//        return getTm().getDeviceId();
//    }
//
//    public String getImsi() {
//        if (getTm().getSubscriberId() == null) return "";
//        return getTm().getSubscriberId();
//    }

    public String getVersionName() {
        try {
            if (mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName != null) {
                return mContext.getPackageManager().getPackageInfo(
                        mContext.getPackageName(), 0).versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getVersionCode() {
        try {
            if (String.valueOf(mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode) != null) {
                return String.valueOf(mContext.getPackageManager().getPackageInfo(
                        mContext.getPackageName(), 0).versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getChannelName() {
        ApplicationInfo appInfo;
        try {
            appInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return appInfo.metaData.getString("UMENG_CHANNEL");
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String getMacAddress() {
        String mac = "-";
        WifiManager wifiMng = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfor = wifiMng.getConnectionInfo();
        mac = wifiInfor.getMacAddress();
        if (mac == null || mac.equals("") ){
            mac = "-";
        }
        return mac;
    }

    /**
     * 获得状态栏高度
     */
    public static int getStateBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获得底部NavigationBar高度(虚拟键盘)
     */
    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        try {
            Resources resources = context.getResources();
            int resourcesId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            height = resources.getDimensionPixelSize(resourcesId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获得手机型号(ex:MI 2S)
     * @return
     */
    public String getModel() {
        return Build.MODEL;
    }

    /**
     * 获得手机厂商(ex:Xiaomi)
     * @return
     */
    public String getManufacturer() {
        return Build.MANUFACTURER;
    }


    public void saveDisplayWhAndImei(Activity activity){
        saveDisplayWH(activity);
        saveImei();
    }

    /**
     * 保存分辨率
     */
    public void saveDisplayWH(Activity activity){
        SharePreUtils pre = new SharePreUtils(Constants.SHAREPRE_DISPLAY);
        if (pre.getString(Constants.SHAREPRE_DISPLAY,"").equals("")) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            pre.putString(Constants.SHAREPRE_DISPLAY,mDisplayMetrics.widthPixels + "*" + mDisplayMetrics.heightPixels);
        }
    }

    /**
     * 获取屏幕分辨率
     * @return  W*H
     */
    public String getDisplayWH(){
        SharePreUtils pre = new SharePreUtils(Constants.SHAREPRE_DISPLAY);
        return pre.getString(Constants.SHAREPRE_DISPLAY,"");
    }

    /**
     * 保存imei
     */
    public  void saveImei(){
        SharePreUtils preUtils = new SharePreUtils(Constants.SHAREPRE_IMEI);
        if (preUtils.getString(Constants.SHAREPRE_IMEI,"").equals("")) {
//            preUtils.putString(Constants.SHAREPRE_IMEI,getImei());
        }
    }

    /**
     * 获取本地imei
     * @return  imei
     */
    public String getSharePreImei(){
        SharePreUtils pre = new SharePreUtils(Constants.SHAREPRE_IMEI);
        return pre.getString(Constants.SHAREPRE_IMEI,"-");
    }


    public String GetNetworkType()
    {
        String strNetworkType = "";
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "WIFI";
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String _strSubTypeName = networkInfo.getSubtypeName();

                    Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                    // TD-SCDMA   networkType is 17
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                            strNetworkType = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                        case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                        case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                            strNetworkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                            strNetworkType = "4G";
                            break;
                        default:
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                strNetworkType = "3G";
                            }else if (_strSubTypeName.contains("LTE")){
                                strNetworkType = "4G";
                            }else {
                                strNetworkType = _strSubTypeName;
                            }

                            break;
                    }

                    L.e("cocos2d-x，Network getSubtype : " + Integer.valueOf(networkType).toString());
                }
            }

            L.e("cocos2d-x，Network Type : " + strNetworkType);

            return strNetworkType;
        }catch (Exception e){
            return "-";
        }
    }

    /**
     * APP是否安装
     * @param packageName 包名  ex: (支付宝com.eg.android.AlipayGphone)
     * @return
     */
    public boolean isAppInstall(String packageName) {
        boolean isInstall;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo == null) {
                isInstall = false;
            } else {
                isInstall = true;
            }
        } catch (Exception e) {
            isInstall = false;
        }

        return isInstall;
    }

}
