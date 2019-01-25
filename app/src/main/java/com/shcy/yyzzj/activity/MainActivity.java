package com.shcy.yyzzj.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.log.L;
import com.shcy.yyzzj.module.album.AlbumActivity;
import com.shcy.yyzzj.module.mine.MineActivity;
import com.shcy.yyzzj.module.orderdetail.OrderDetailActivity;
import com.shcy.yyzzj.module.pay.PayActivity;
import com.shcy.yyzzj.module.selectsize.SelectSizeActivity;
import com.shcy.yyzzj.module.splash.SplashActivity;
import com.shcy.yyzzj.module.update.AppDownLoad;
import com.shcy.yyzzj.utils.DateUtil;
import com.shcy.yyzzj.utils.PermissionUtil;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.SetUtils;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shcy.yyzzj.receiver.MyReceiver.INTENT_TARGET_PAYACTIVITY;
import static com.shcy.yyzzj.receiver.MyReceiver.KEY_INTENT_TARGET;

/**
 * Created by licong on 2018/9/25.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "主页";

    private RelativeLayout takepictrueLayout, albumLayout, mineLayout;
    private final int REQUEST_PERMISSION_CODE = 015;
    private final int SET_PERMISSION_CODE = REQUEST_PERMISSION_CODE + 1;
    private final int REQUEST_LOGIN_CODE = SET_PERMISSION_CODE + 1;

    private long exitTime = 0L;
    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.mainActivity = this;
        takepictrueLayout = findViewById(R.id.takepictrue_layout);
        albumLayout = findViewById(R.id.album_layout);
        mineLayout = findViewById(R.id.mine_layout);
        takepictrueLayout.setOnClickListener(this);
        mineLayout.setOnClickListener(this);
        albumLayout.setOnClickListener(this);
        if (savedInstanceState != null) {
            this.returnInstanceState(savedInstanceState);
        }
        if (TextUtils.equals(getIntent().getStringExtra(KEY_INTENT_TARGET), INTENT_TARGET_PAYACTIVITY)) {//来自通知，需要跳往订单详情页
            Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
            intent.putExtra(KEY_INTENT_TARGET, INTENT_TARGET_PAYACTIVITY);
            intent.putExtra(PayActivity.ORDER, getIntent().getSerializableExtra(PayActivity.ORDER));
            startActivity(intent);
        }


        PermissionUtil.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_CODE, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {
                PermissionUtil.showExsit(MainActivity.this, getString(R.string.need_permission_splash), new PermissionUtil.OnSetListener() {
                    @Override
                    public void onSueccess() {
                    }

                    @Override
                    public void onFailed() {
                        finish();
                    }
                }, SET_PERMISSION_CODE);
            }
        });

        //app版本檢查，下載APP
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(d);
        if (!isCheck && !DateUtil.compareTwoDateForSameDay(DateUtil.getDate(time, DateUtil.DATAFORMAT_STR), DateUtil.getDate(SetUtils.getInstance().getTimeUpdate(), DateUtil.DATAFORMAT_STR))) {
            isCheck = true;
            SetUtils.getInstance().setTimeUpdate(time);
            AppDownLoad.create(this).checkVersion();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.album_layout:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_MAIN_ALBUM);
                Intent intent;
                intent = new Intent(MainActivity.this, AlbumActivity.class);
                startActivity(intent);
                break;
            case R.id.takepictrue_layout:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_MAIN_TAKEPICTRUE);
                Intent intent1 = new Intent(this, SelectSizeActivity.class);
                intent1.putExtra(SelectSizeActivity.TYPE, 0);
                startActivity(intent1);
                break;
            case R.id.mine_layout:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_MAIN_MINE);
                Intent in3 = new Intent(this, MineActivity.class);
                startActivity(in3);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.onActivityResult(requestCode, MainActivity.this);
        if (requestCode == REQUEST_LOGIN_CODE) {
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isCheck", isCheck);
        outState.putStringArrayList(Constants.PERMISSION, PublicUtil.getPermission(this));
        super.onSaveInstanceState(outState);
    }

    protected void returnInstanceState(Bundle savedInstanceState) {
        this.isCheck = savedInstanceState.getBoolean("isCheck");

        //处理6.0手动关闭权限崩溃问题
        if (savedInstanceState != null && PublicUtil.checkPermissionHasChanged(PublicUtil.getPermission(this), savedInstanceState.getStringArrayList(Constants.PERMISSION))) {
            L.e("某些权限被拒绝");
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.mainActivity = null;
    }

    /**
     * 监听按下事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 连续2次点击退出
            if ((System.currentTimeMillis() - this.exitTime) < 2000) {
                UserUtil.getInstance().clearAll();
                //删除本地非有效期的视频
                this.finish();
            } else {
                this.exitTime = System.currentTimeMillis();
                ToastUtil.showToast("再按一次退出证件照相机");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页");
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_MAINPAGE_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
