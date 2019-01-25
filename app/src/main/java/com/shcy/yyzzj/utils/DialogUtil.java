package com.shcy.yyzzj.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.dialog.PhotoSystemDialog;
import com.shcy.yyzzj.module.update.UpdateAppService;

public class DialogUtil {

    public static void showPermissionDialog(Context context, CharSequence content,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setTitleName("提示")
                .setMessage(content)
                .setConfirmBtn("去设置")
                .setCancelBtn("取  消")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    public static void showConfirmRreceiptDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("是否确认收货？")
                .setConfirmBtn("确定")
                .setCancelBtn("取消")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     *  支付失败提示
     * @param context
     * @param listener
     */
    public static void showPayFailedDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("支付失败，请查看订单")
                .setConfirmBtn("确定")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     *  支付失败提示
     * @param context
     * @param listener
     */
    public static void showCheckFaceDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("未检测出人脸，请重新拍摄")
                .setConfirmBtn("确定")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     *  提示弹窗
     * @param context
     * @param listener
     */
    public static void showNoticeDialog(Context context,String msg,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage(msg)
                .setConfirmBtn("确定")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     * 删除照片提示
     * @param context
     * @param listener
     */
    public static void showDeletePhotoDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("是否确定删除当前照片？")
                .setConfirmBtn("确定")
                .setCancelBtn("取消")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     * 删除照片提示
     * @param context
     * @param listener
     */
    public static void showDeleteAddressDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("是否确定删除当前地址？")
                .setConfirmBtn("确定")
                .setCancelBtn("取消")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }

    /**
     * 删除照片提示
     * @param context
     * @param listener
     */
    public static void showLogoutDialog(Context context,PhotoDialog.OnDialogClickListener listener) {
        new PhotoDialog.Builder(context)
                .setMessage("确认退出登录吗？")
                .setConfirmBtn("确定")
                .setCancelBtn("取消")
                .setOnDialogClickListener(listener)
                .createSetBtnTextColor(false).show();
    }
    /**
     * 提示是否更新APP
     *
     * @param context
     * @param url     下载URL
     * @param content
     */
    public static void showDialogForDownloadApp(final Context context, final String url, final String content) {
        PhotoSystemDialog dialog =new PhotoSystemDialog.Builder(context)
                .setTitleName("系统更新")
                .setConfirmBtn("立即更新")
                .setCancelBtn("1".equals(SetUtils.getInstance().getForce())?"":"稍后更新")
//                .setMessage(Html.fromHtml(TextUtils.isEmpty(content)?"":content))
                .setMessage(TextUtils.isEmpty(content)?"":content)
                .setOnDialogClickListener(new PhotoSystemDialog.OnDialogClickListener() {

                    @Override
                    public void confirm() {
                        FileUtil fileUtil = new FileUtil();
                        //下载完成 并且目录中存在该apk
                        if ("2".equals(SetUtils.getInstance().getdownload())&&fileUtil.FileIsExists(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk")) {
                            //说明已下载最新APK 那么直接调用安装apk
                            context.startActivity(PublicUtil.getInstallAPKIntent());
                            android.os.Process.killProcess(android.os.Process.myPid());
                        } else {
                            Intent updateIntent = new Intent(context, UpdateAppService.class);
                            updateIntent.putExtra("downloadUrl", url);
                            context.startService(updateIntent);
                        }
                    }

                    @Override
                    public void cancel() {
                    }

                })
                .create(false);
        if("1".equals(SetUtils.getInstance().getForce())){
            dialog.setCancelable(false);
        }
        dialog.show();
    }

}
