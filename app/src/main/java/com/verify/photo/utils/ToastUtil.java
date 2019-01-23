
package com.verify.photo.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verify.photo.R;


public class ToastUtil {
    private static Toast mToast;
    private static TextView tv_toast;
    private static Toast toast;

    public static void showToast(String msg){
        if (toast == null){
            toast = Toast.makeText(CommontUtil.getGlobeContext(),"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
        }
        toast.setText(msg);
        toast.show();
    }

    public static void showToast(String msg, boolean iserror){
        if (toast == null){
            toast = Toast.makeText(CommontUtil.getGlobeContext(),"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
        }
        toast.setText(msg);
        toast.show();
    }

//    public static void showToast(String msg , boolean iserror) {
//        InitToast(iserror);
//        tv_toast.setText(msg);
//        mToast.show();
//    }

    public static void showToast(int resId,boolean iserror) {
        InitToast(iserror);
        tv_toast.setText(resId);
        mToast.show();
    }


    private static void InitToast(boolean iserror) {
        LayoutInflater inflater = LayoutInflater.from(CommontUtil.getGlobeContext());
        View toastView = inflater.inflate(R.layout.toastbg, null);

        tv_toast = toastView.findViewById(R.id.toast_tv);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_toast.getLayoutParams();

        params.width = CommontUtil.getScreenWidth();
        tv_toast.setLayoutParams(params);

        if(iserror){
            tv_toast.setBackgroundResource(R.color.toast_error_bg);
        }else{
            tv_toast.setBackgroundResource(R.color.toast_nomal_bg);
        }

        if (mToast == null) {
            mToast = new Toast(CommontUtil.getGlobeContext());
        }
        mToast.setView(toastView);
        mToast.setGravity(Gravity.TOP, 0, 0);
    }


    public static void showTextToast(String content, boolean b) {
        Toast toast = new Toast(CommontUtil.getGlobeContext());
        View view = View.inflate(CommontUtil.getGlobeContext(), R.layout.toast_text_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_text_toast);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_toast_bg);
        toast.setDuration(Toast.LENGTH_SHORT);
        tv.setText(content);
        if (b) {
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.GONE);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }
}

