package com.verify.photo.module.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.verify.photo.R;
import com.verify.photo.activity.MainActivity;
import com.verify.photo.config.Constants;
import com.verify.photo.module.login.LoginModel;
import com.verify.photo.module.pay.PayActivity;
import com.verify.photo.utils.SetUtils;

import static com.verify.photo.receiver.MyReceiver.INTENT_TARGET_PAYACTIVITY;
import static com.verify.photo.receiver.MyReceiver.KEY_INTENT_TARGET;

/**
 * Created by licong on 2018/10/11.
 */

public class SplashActivity extends Activity {

    private static final String TAG = "启动页";

    private LinearLayout splashLayout;
    private SplashLunboManager splashLunboManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Constants.TOKEN = SetUtils.getInstance().getToken();
        LoginModel model = new LoginModel();
        if (SetUtils.getInstance().getIsLogin()) {
            model.loadUserData();
        } else {
            model.login();
        }
        splashLayout = findViewById(R.id.splash_guide);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SetUtils.getInstance().getFirstApp()) {
                    initGuide();
                } else {
                    intoMain();
                }
            }

        }, 2000);
    }

    private void initGuide() {
        if (splashLayout == null || isFinishing()) {
            return;
        }
        splashLunboManager = new SplashLunboManager(this);
        splashLayout.addView(splashLunboManager.getLunboView());
        splashLayout.setVisibility(View.VISIBLE);
    }


    private void intoMain() {
        SetUtils.getInstance().setFirstApp(false);
        if (TextUtils.equals(getIntent().getStringExtra(KEY_INTENT_TARGET), INTENT_TARGET_PAYACTIVITY)) {//来自通知，需要跳往订单详情页
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(KEY_INTENT_TARGET,INTENT_TARGET_PAYACTIVITY);
            intent.putExtra(PayActivity.ORDER, getIntent().getSerializableExtra(PayActivity.ORDER));
            startActivity(intent);
        }else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 关闭启动引导页
     */
    public void closeGuide() {
        intoMain();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashLunboManager != null) {
            splashLunboManager.destory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
