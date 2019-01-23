package com.verify.photo.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by licong on 2018/10/17.
 */

public class BaseActivity extends Activity {

    public static final String CLOSEACTIVITY_ACTION = "closeactivity_action";
    public static final String CLOSEACTIVITY_PREVIEW = "preview_closeactivity_action";
    private CloseActivityReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new CloseActivityReceiver();
        registerReceiver(receiver, new IntentFilter(CLOSEACTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class CloseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), CLOSEACTIVITY_ACTION)) {
                if (intent.getIntExtra(CLOSEACTIVITY_PREVIEW, 0) == 1) {
                    finishByReceiver();
                } else {
                    finish();
                }
            }
        }
    }

    protected void finishByReceiver() {
        finish();
    }
}
