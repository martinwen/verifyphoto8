package com.shcy.yyzzj.utils;

import android.app.Activity;
import android.content.Context;

import com.shcy.yyzzj.dialog.PhotoInfoMessage;

/**
 * Created by licong on 2018/4/24.
 */

public class ProgressBarUtil {
    private PhotoInfoMessage dialog;
    private Context context;

    public ProgressBarUtil(Context context) {
        this.context = context;
    }

    public static ProgressBarUtil create(Context context){
        return new ProgressBarUtil(context);
    }

    public void show(String t) {
        if (dialog == null)
            dialog = new PhotoInfoMessage(context);
        else
            cancel();
        dialog.setTitle(t);
        dialog.show();
    }

    public void cancel() {
        if(context==null||((Activity)context).isFinishing()){
            return ;
        }
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
