package com.shcy.yyzzj.module.about;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.utils.WebViewUtil;
import com.shcy.yyzzj.view.view.X5WebView;

/**
 * Created by licong on 2018/10/15.
 */

public class H5Activity extends Activity {

    public static final String URL = "h5_activity_url";
    public static final String TITLE = "h5_activity_title";
    private static final String TAG = "隐私政策";

    private X5WebView webView;
    public WebViewUtil mWebViewUtil;
    private TextView titleText;
    private ImageView back;
    private String url;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agrennment);
        initView();
        initData();
    }

    private void initData() {
        url = getIntent().getStringExtra(URL);
        title = getIntent().getStringExtra(TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleText.setText(title);
        }
        if (!TextUtils.isEmpty(url)) {
            mWebViewUtil.loadUrl(url);
        } else {
            mWebViewUtil.loadUrl(Constants.AGREEMENT_URL);
        }
    }

    private void initView() {
        webView = findViewById(R.id.agreement_webview);
        titleText = findViewById(R.id.h5_actitivy_title);
        back = findViewById(R.id.agreement_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebViewUtil = WebViewUtil.create(this, webView);
        mWebViewUtil.setmWebViewClient(getWebViewClient());
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private com.tencent.smtt.sdk.WebViewClient getWebViewClient() {
        return new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView arg0, int arg1, String arg2,
                                        String arg3) {
                webView.setVisibility(View.GONE);
                super.onReceivedError(arg0, arg1, arg2, arg3);
                ToastUtil.showTextToast("网络不给力", false);
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed(); // 接受所有网站的证书
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView var1, com.tencent.smtt.export.external.interfaces.WebResourceRequest var2, WebResourceError var3) {
//                ToastUtil.showTextToast("网络跟着你的bug私奔了", false);
                ToastUtil.showToast("网络跟着你的bug私奔了");
                webView.setVisibility(View.GONE);
                super.onReceivedError(var1, var2, var3);

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.webView.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.webView.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        if (webView != null)
            this.webView.destroy();
        super.onDestroy();
    }
}