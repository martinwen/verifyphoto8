package com.verify.photo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

public class PreferenceUtils {
    SharedPreferences manager;
    public Context mGlobeContext;
    private final static int MODE = Context.MODE_PRIVATE;

    public PreferenceUtils(String name) {
        mGlobeContext = CommontUtil.getGlobeContext();
        manager = mGlobeContext.getSharedPreferences(name, MODE);
    }

    public boolean putJsonObject(String key, String json) {
        return manager.edit().putString(key, json).commit();
    }

    public boolean putString(String key, String value) {
        return manager.edit().putString(key, value).commit();
    }

    public boolean putBoolean(String key, boolean value) {
        return manager.edit().putBoolean(key, value).commit();
    }

    public boolean putInt(String key, int defValue) {
        return manager.edit().putInt(key, defValue).commit();
    }

    public String getString(String key, String defValue) {
        return manager.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return manager.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return manager.getInt(key, defValue);
    }

    public void clearKey(String key) {
        manager.edit().remove(key).commit();
    }

    public void clear() {
        manager.edit().clear().commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putSet(String key, Set<String> set) {
        manager.edit().putStringSet(key, set).commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getSet(String key, Set<String> def) {
        return manager.getStringSet(key, def);
    }

    public Map<String, ?> getAll() {
        return manager.getAll();
    }

}
