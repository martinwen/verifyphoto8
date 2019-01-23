package com.verify.photo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

public class CommontUtil {
	public static final HashMap<String, Object> GLOBEL_VALUE = new HashMap<String, Object>();
	public static final String GLOBEL_CONTEXT_KEY = "ctx";
	public static final String GLOBEL_NOMAL_BEAN_KEY = "nomal_bean";

	private static Context mInnerContext;

	private static ConnectivityManager mConnectivityManager;
	private static int SCREEN_WIDTH = 0;
	private static int SCREEN_HEIHGT = 0;

	public static void init(Context context) {
		GLOBEL_VALUE.put(GLOBEL_CONTEXT_KEY, context);
		mInnerContext = context;

		DisplayMetrics dm =new DisplayMetrics();
		WindowManager manager = (WindowManager) mInnerContext.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		SCREEN_WIDTH=dm.widthPixels;
		SCREEN_HEIHGT = dm.heightPixels;
		if (SCREEN_WIDTH > SCREEN_HEIHGT){
			SCREEN_WIDTH = dm.heightPixels;
			SCREEN_HEIHGT = dm.widthPixels;
		}
	}

	public static Context getGlobeContext() {
		if (mInnerContext == null)
			mInnerContext = (Context) CommontUtil.GLOBEL_VALUE
					.get(CommontUtil.GLOBEL_CONTEXT_KEY);
		return mInnerContext;
	}


	public static PackageManager getGlobePackageManager() {
		return getGlobeContext().getPackageManager();
	}

	/**
	 * 判断当前是不是wifi
	 * @return
	 */
	public static boolean isWifi() {
		try {
			if (mConnectivityManager == null) {
				mConnectivityManager = (ConnectivityManager) getGlobeContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			if (mConnectivityManager == null)
				return false;

			NetworkInfo wifiInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiInfo != null && wifiInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 是否是3G模式
	 * @return
	 */
	public static boolean is3G() {
		try {
			if (mConnectivityManager == null) {
				mConnectivityManager = (ConnectivityManager) getGlobeContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			if (mConnectivityManager == null)
				return false;

			NetworkInfo mobileInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileInfo != null && mobileInfo.isConnected()) {
				return false;
			}
		} catch (Exception e) {
		}
		return true;
	}

	public static boolean isNeworkEnable() {
		try {
			if (mConnectivityManager == null) {
				mConnectivityManager = (ConnectivityManager) getGlobeContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
			}
			if (mConnectivityManager == null)
				return false;

			NetworkInfo wifiInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiInfo != null && wifiInfo.isConnected()) {
				return true;
			}
			NetworkInfo mobileInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobileInfo != null && mobileInfo.isConnected()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public static int getSreenHeight() {
		return SCREEN_HEIHGT;
	}

	private static boolean isPaseDisplayImg = false;

	public static void setPaseDispalyImg(boolean pause) {
		isPaseDisplayImg = pause;
	}

	public static boolean isPaseDisplayImg() {
		return isPaseDisplayImg;
	}

	/**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight() {
		int result = 0;
		int resourceId = mInnerContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mInnerContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 判断当前View是否还在屏幕内
	 * @param context
	 * @param view
	 * @return
	 */
	public static Boolean checkIsVisible(Context context, View view) {
		int screenWidth = getScreenMetrics(context).x;
		int screenHeight = getScreenMetrics(context).y;
		Rect rect = new Rect(0, 0, screenWidth, screenHeight);
		int[] location = new int[2];
		view.getLocationInWindow(location);
		if (view.getLocalVisibleRect(rect)) {
			return true;
		} else {
			//view已不在屏幕可见区域;
			return false;
		}
	}
	/**
	 * 获取屏幕宽度和高度，单位为px
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context){
		DisplayMetrics dm =context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		return new Point(w_screen, h_screen);
	}

	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static PackageInfo getAppPackageInfo(Context context) {
		PackageInfo info = null;
		PackageManager manager = context.getPackageManager();
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}
}
