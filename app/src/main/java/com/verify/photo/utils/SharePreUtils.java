package com.verify.photo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.verify.photo.config.Constants;

import java.util.Set;

/**
 * Created by Licong on 2018/9/25.
 */

public class SharePreUtils {
        SharedPreferences manager;
        public Context mGlobeContext;
        private final static int MODE = Context.MODE_PRIVATE;

        public SharePreUtils(String name)
        {
            mGlobeContext = CommontUtil.getGlobeContext();
            manager = mGlobeContext.getSharedPreferences(name, MODE);
        }

        public boolean putString(String key, String value)
        {
            return manager.edit().putString(key, value).commit();
        }

        public boolean putBoolean(String key, boolean value)
        {
            return manager.edit().putBoolean(key, value).commit();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public boolean putSet(String key, Set<String> value)
        {
            return manager.edit().putStringSet(key, value).commit();
        }

        public boolean putInt(String key, int defValue)
        {
            return manager.edit().putInt(key, defValue).commit();
        }

        public String getString(String key, String defValue)
        {
            return manager.getString(key, defValue);
        }

        public boolean getBoolean(String key, boolean defValue)
        {
            return manager.getBoolean(key, defValue);
        }

        public Set<String> getStringSet(String key, Set<String> value)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return manager.getStringSet(key, value);
            }else{
                return value;
            }
        }

        public int getInt(String key, int defValue)
        {
            return manager.getInt(key, defValue);
        }

        public void clearKey(String key)
        {
            manager.edit().remove(key).commit();
        }

        public void clear()
        {
            manager.edit().clear().commit();
        }

    //取本地存入的Cookies
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getCookies(){
//        SharePreUtils preUtils = new SharePreUtils(Constants.SHAREPRE_PHOTO_COKIES);
        return manager.getStringSet(Constants.SHAREPRE_PHOTO_COKIES,null);
    }
}
