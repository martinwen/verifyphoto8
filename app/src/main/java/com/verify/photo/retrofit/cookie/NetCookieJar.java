package com.verify.photo.retrofit.cookie;


import com.verify.photo.config.Constants;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.SharePreUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by licong on 2018/9/25.
 */

public class NetCookieJar implements CookieJar {
    private static NetCookieJar mCookieManager;

    private NetCookieJar() {
    }

    public static NetCookieJar create(){
        if (mCookieManager == null){
            mCookieManager = new NetCookieJar();
        }
        return mCookieManager;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cook) {
        if (url.toString().contains(Constants.PHOTO_URL)) {
            Set<String> cookies = new HashSet<String>();
            String cookie = "";
            if (cook != null && cook.size() > 0) {
                for (Cookie item : cook) {
                    if (item.name().contains("accessToken")) {
                        cookie = item.toString();
                    }
                }
            }
            if (!cookie.equals("")) {
                cookies.add(cookie);
            SharePreUtils preUtils = new SharePreUtils(Constants.SHAREPRE_PHOTO_COKIES);
            preUtils.putSet(Constants.SHAREPRE_PHOTO_COKIES, cookies);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        if (url.toString().contains(Constants.PHOTO_URL)){
            //将保存的cookies添加到url上
            Iterator<String> it =  PublicUtil.getCookies().iterator();
            while (it.hasNext()) {
                Cookie cookie = Cookie.parse(url,it.next());
                cookies.add(cookie);
            }
        }
        return cookies;
    }
}