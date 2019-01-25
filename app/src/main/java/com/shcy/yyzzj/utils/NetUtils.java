package com.shcy.yyzzj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Zmm on 2017/4/24.
 */

public class NetUtils {
    private static NetUtils utils;
    public static NetUtils getInstanse(){
         if(utils==null){
             utils=new NetUtils();
         }
        return utils;
    }

    /**
     * 判断wifi连接
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断移动网络连接
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm != null && info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断有无网络连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
