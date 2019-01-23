package com.verify.photo.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.verify.photo.dialog.PhotoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licong on 2018/5/14.
 */

public class PermissionUtil {

    private static int mRequestPermissionCode = -1;

    private static int mSetPermissionCode = -1;

    private static OnPermissionListener mOnPermissionListener;

    private static OnSetListener onSetListener;

    public interface OnPermissionListener {

        void onPermissionGranted();

        void onPermissionDenied();
    }

    public interface OnSetListener{
        void onSueccess();

        void onFailed();
    }

    public static List<String> checkPermissions(Activity activity, String[] permissions) {
        List<String> needRequestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                needRequestPermissions.add(permission);
            }
        }
        return needRequestPermissions;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode, OnPermissionListener onPermissionListener) {
        mOnPermissionListener = onPermissionListener;
        List<String> list = checkPermissions(activity, permissions);
        if (list.size() > 0) {
            mRequestPermissionCode = requestCode;
            activity.requestPermissions(list.toArray(new String[list.size()]), requestCode);
        } else {
            if (mOnPermissionListener != null) {
                mOnPermissionListener.onPermissionGranted();
            }
        }
    }

    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != -1 && requestCode == mRequestPermissionCode) {
            if (mOnPermissionListener != null) {
                if (verifyPermissions(grantResults)) {
                    mOnPermissionListener.onPermissionGranted();
                } else {
                    mOnPermissionListener.onPermissionDenied();
                }
            }
        }
    }

    private static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 确认删除窗口
     */
    public static void showExsit(final Activity activity,String content ,OnSetListener Listener,final int setPermissionCode) {
        onSetListener = Listener;
        DialogUtil.showPermissionDialog(activity, content,
                new PhotoDialog.OnDialogClickListener() {

                    @Override
                    public void confirm() {
                        getAppDetailSettingIntent(activity,setPermissionCode);
                    }

                    @Override
                    public void cancel() {
                        onSetListener.onFailed();
                    }

                });
    }

    public static void onActivityResult(int requestCode, Activity activity){
        if (requestCode == mSetPermissionCode){
            if (checkPermissions(activity,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE}).size()>0){
                onSetListener.onFailed();
            }else {
                onSetListener.onSueccess();
            }
        }

    }

    /**
     * 跳转到权限设置界面
     */
    private static void getAppDetailSettingIntent(Activity activity,int setPermissionCode){
        mSetPermissionCode = setPermissionCode;
        Intent intent = new Intent();
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        }
        activity.startActivityForResult(intent,mSetPermissionCode);
    }

}
