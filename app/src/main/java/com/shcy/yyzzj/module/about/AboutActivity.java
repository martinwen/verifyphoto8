package com.shcy.yyzzj.module.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.activity.MyApplication;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.utils.CommontUtil;
import com.shcy.yyzzj.utils.LogUtils;
import com.shcy.yyzzj.utils.PublicUtil;

import static com.shcy.yyzzj.module.about.H5Activity.TITLE;
import static com.shcy.yyzzj.module.about.H5Activity.URL;

public class AboutActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "关于我们";
    private ImageView back;
    private TextView agreenment, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initview();
    }

    private void initview() {
        back = findViewById(R.id.about_back);
        agreenment = findViewById(R.id.about_agreement);
        version = findViewById(R.id.about_version);

        back.setOnClickListener(this);
        agreenment.setOnClickListener(this);

        String contnet = "版本号:" + CommontUtil.getAppPackageInfo(this).versionName + "_";
        String schannel = PublicUtil.getChannelMap().get(PublicUtil.getChannelName(MyApplication.getContext()));
        contnet = contnet + (TextUtils.isEmpty(schannel) ? "verifyphoto" : schannel);
        if (LogUtils.APP_DBG) {
            contnet += "_debug";
        } else {
            contnet += "_release";
        }
        version.setText(contnet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back:
                finish();
                break;
            case R.id.about_agreement:
                Intent intent = new Intent(this, H5Activity.class);
                intent.putExtra(URL, Constants.AGREEMENT_URL);
                intent.putExtra(TITLE,"关于我们");
                startActivity(intent);
                break;
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
