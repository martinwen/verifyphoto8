package com.shcy.yyzzj.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.view.view.X5WebView;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WebViewUtil {

    private Activity activity;
    private X5WebView mWebView;
    private Fragment mFragment;

    private ProgressBar progressBar;

    private webTitleListener titlelistener;
    public WebSettings webSetting;


    public interface webTitleListener {
        void webTitleReceived(String webTitle);
    }

    public void setmWebViewClient(WebViewClient mWebViewClient) {
        this.mWebView.setWebViewClient(mWebViewClient);
    }

    public static WebViewUtil create(Activity activity, X5WebView mWebView, webTitleListener listener) {
        WebViewUtil object = new WebViewUtil();
        object.mWebView = mWebView;
        object.activity = activity;
        object.titlelistener = listener;
        object.init();
        return object;
    }

    public static WebViewUtil create(Activity activity, X5WebView mWebView) {
        WebViewUtil object = new WebViewUtil();
        object.mWebView = mWebView;
        object.activity = activity;
        object.init();
        return object;
    }

    public static WebViewUtil create(Activity activity, X5WebView mWebView, Fragment fragment) {
        WebViewUtil object = new WebViewUtil();
        object.mWebView = mWebView;
        object.activity = activity;
        object.mFragment = fragment;
        object.init();
        return object;
    }

    public void loadUrl(String url) {
        this.mWebView.loadUrl(url);
    }

    public void loadUrl(String url, Map<String, String> Header) {
        this.mWebView.loadUrl(url, Header);
    }

    public void loadData(String html) {
        final String mimeType = "text/html";
        final String encoding = "utf-8";
        this.mWebView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

    private void init() {
        this.initWebView();
        this.initWebSetting();
    }

    private void initWebView() {
        this.mWebView.setWebChromeClient(this.defaultWebChromeClient());
    }

    private void initWebSetting() {
        // 各种设置
        webSetting = this.mWebView.getSettings();
        //是否设置缓存策略
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//允许弹框
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setUserAgent(webSetting.getUserAgentString() + " " + Constants.getH5Header2String());
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.activity.getDir("appcache", 0)
                .getPath());
        webSetting.setDatabasePath(this.activity.getDir("databases", 0)
                .getPath());
        webSetting.setGeolocationDatabasePath(this.activity.getDir(
                "geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    //支持下载
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }

    }

    public void syncCookie(String url) {
        CookieSyncManager.createInstance(this.activity);
        CookieManager cookieManager = CookieManager.getInstance();
        com.tencent.smtt.sdk.CookieManager tencentCookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
        SharePreUtils preUtils = new SharePreUtils(Constants.SHAREPRE_PHOTO_COKIES);
        Set<String> cookies = preUtils.getStringSet(Constants.SHAREPRE_PHOTO_COKIES, new HashSet<String>());
        String[] datas = cookies.toArray(new String[cookies.size()]);
        if (datas != null && datas.length > 0 && cookieManager != null && url != null) {
            for (int i = 0; i < cookies.size(); i++) {
                cookieManager.setCookie(url, datas[i]);
                tencentCookieManager.setCookie(url, datas[i]);
            }
        }

        CookieSyncManager.getInstance().sync();
    }

    /**
     * cookie 是否过期
     *
     * @return
     */
    public boolean isCookieExpires() {
        SharePreUtils preUtils = new SharePreUtils(Constants.SHAREPRE_PHOTO_COKIES);
        Set<String> cookies = preUtils.getStringSet(Constants.SHAREPRE_PHOTO_COKIES, new HashSet<String>());
        String[] datas = cookies.toArray(new String[cookies.size()]);
        if (datas != null && datas.length > 0) {
            for (int i = 0; i < cookies.size(); i++) {
                String cgroup[] = datas[i].split("Expires=");
                if (cgroup.length > 1) {
                    String cookie = cgroup[1];
                    String expires = cookie.split(";")[0];
                    if (DateUtil.compareTwoDateBefore(new Date(System.currentTimeMillis()), new Date(expires))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private WebChromeClient defaultWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, com.tencent.smtt.sdk.ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (openFileListener != null) {
                    openFileListener.onShowFile(view, callback, params);
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar != null) {
                    if (newProgress < 100) {
                        if (progressBar.getVisibility() == View.GONE)
                            progressBar.setVisibility(View.VISIBLE);

                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar.setProgress(100);

                        // 运行动画 animation

                        progressBar
                                .startAnimation(AnimationUtils.loadAnimation(
                                        activity, R.anim.progressbar_h5));

                        // 将 spinner 的可见性设置为不可见状态

                        progressBar.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (titlelistener != null)
                    titlelistener.webTitleReceived(title);
            }


            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }
        };

    }


    public void scrollTo(int x, int y) {
        this.mWebView.scrollTo(x, y);
    }

    public int getScrollY() {
        return this.mWebView.getWebScrollY();
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public interface OnOpenFileListener {
        void onShowFile(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);
    }

    OnOpenFileListener openFileListener;

    public void setOpenFileListener(OnOpenFileListener f) {
        openFileListener = f;
    }
}
