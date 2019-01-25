package com.shcy.yyzzj.utils;

import android.content.Context;

/**
 Created by Mr.Wang on 2018/3/28.
 */
public class DpPxUtils {
    private static  float scale;
    /**
     * dp转成px
     * @param dpValue
     * @return
     */
    public static int dp2px(Context mContext, float dpValue) {
        scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转成dp
     * @param pxValue
     * @return
     */
    public static int px2dp(Context mContext, float pxValue) {
        scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转px
     * @param mContext
     * @param i
     * @return
     */
    public static int dip2px(Context mContext, int i) {
        return (int) (0.5D + (double) (getDensity(mContext) * (float) i));
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕高度
     * @param mContext
     * @return
     */
    public static int getScreenHeight(Context mContext) {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }
}
