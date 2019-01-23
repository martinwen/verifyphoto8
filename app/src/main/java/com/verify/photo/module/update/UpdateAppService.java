package com.verify.photo.module.update;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;


import com.verify.photo.R;
import com.verify.photo.activity.MainActivity;
import com.verify.photo.activity.MyApplication;
import com.verify.photo.config.Constants;
import com.verify.photo.utils.FileUtil;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.SetUtils;
import com.verify.photo.utils.ToastUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * App自动更新之通知栏下载
 *
 * @author hy
 */
public class UpdateAppService extends Service {

    private final int DOWNLOAD_COMPLETE = 1;
    private final int DOWNLOAD_FAIL = 2;
    public static final int NOTIFY_ID = 100;

    private Context context;
    private NotificationManager nManager;
    private NotificationCompat.Builder CompatBuilder;
    private Notification.Builder nBuilder;
    private boolean isDownLoading = false;
    private boolean isFKload = false;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Notification notification = (Notification) msg.obj;
                    UpdateAppService.this.notify(notification);
                    super.handleMessage(msg);
                    SetUtils.getInstance().setdownload("1");
                    break;
                case DOWNLOAD_COMPLETE:
                    // 下载完成
                    UpdateAppService.this.notify(getDownloadCompleteNotification("下载完成,点击安装。"));
                    SetUtils.getInstance().setdownload("2");
                    stopSelf();
//                    install(Constants.SDCARD_PATH_DOWNLOAD_APK);
                    break;
                case DOWNLOAD_FAIL:
                    // 下载失败
                    UpdateAppService.this.notify(getDownloadCompleteNotification("下载失败！"));
                    SetUtils.getInstance().setdownload("0");
                    stopSelf();
                    break;
                default:
                    stopSelf();
            }

        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isDownLoading && intent != null) {
            String downloadUrl = intent.getStringExtra("downloadUrl");
            isFKload = intent.getBooleanExtra("isFKLoad", false);
            if (!TextUtils.isEmpty(downloadUrl)) {
                if (!isFKload) {
                    mHandler.obtainMessage(0, this.getNotification("正在下载..."));
                }
                new Thread(new UpdateRunnable(downloadUrl), "APK Download").start();// 开始下载
            }

        } else {
            ToastUtil.showToast("下载中，请稍后...");
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("~~updateAppService stop~~");
        super.onDestroy();
    }

    /**
     * 发送更新通知
     *
     * @param notification
     */
    private void notify(Notification notification) {
        // 用NotificationManager的notify方法通知用户生成标题栏消息通知
        if (nManager == null) {
            nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        nManager.notify(NOTIFY_ID, notification);// id是应用中通知的唯一标识
        // 如果拥有相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
    }

    /**
     * 创建通知  Builder api 小于26
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Notification.Builder createNotificationBuilder() {

        return new Notification.Builder(context)
                .setSound(null)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(getString(R.string.app_name) + "下载进度:")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setContentIntent(
                        PendingIntent.getActivity(context, 0, new Intent(
                                context, MainActivity.class), 0));
    }

    /**
     * 创建通知  Builder api 26及以上
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    private NotificationCompat.Builder createNotificationBuilder_O() {
        NotificationChannel channel = new NotificationChannel(NOTIFY_ID + "", getString(R.string.app_name), NotificationManager.IMPORTANCE_MIN);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        return new NotificationCompat.Builder(this, "chat")
                .setContentTitle(getString(R.string.app_name) + "下载进度:")
                .setContentText("0%")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo))
                .setAutoCancel(true).setContentIntent(
                        PendingIntent.getActivity(context, 0, new Intent(
                                context, MainActivity.class), 0));
    }


    /**
     * 下载中的通知
     *
     * @param content
     * @return
     */
    private Notification getNotification(String content) {
        //8.0以上系统 适配 通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (CompatBuilder == null) {
                CompatBuilder = createNotificationBuilder_O();
            }
            CompatBuilder.setChannelId("100");
            CompatBuilder.setContentText(content);
            return CompatBuilder.build();
        } else {
            if (nBuilder == null) {
                nBuilder = createNotificationBuilder();
            }
            nBuilder.setContentText(content);
            return nBuilder.getNotification();
        }
    }

    /**
     * 下载完成后的通知
     *
     * @return
     */
    private Notification getDownloadCompleteNotification(String content) {
        //8.0以上系统 适配 通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (this.CompatBuilder == null) {
                this.CompatBuilder = createNotificationBuilder_O();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    PublicUtil.getInstallAPKIntent(), 0);
            CompatBuilder.setChannelId("100");
            CompatBuilder.setContentText(content)
                    .setContentIntent(pendingIntent);
            return CompatBuilder.build();
        } else {
            if (this.nBuilder == null) {
                this.nBuilder = createNotificationBuilder();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    PublicUtil.getInstallAPKIntent(), 0);
            nBuilder.setContentText(content)
                    .setContentIntent(pendingIntent);
            return nBuilder.getNotification();

        }
    }

    class UpdateRunnable implements Runnable {

        private String downloadUrl = null;

        public UpdateRunnable(String url) {
            downloadUrl = url;
        }

        @Override
        public void run() {
            isDownLoading = true;
            try {
                long downloadSize = downloadUpdateFile(downloadUrl);
                if (downloadSize > 0) {
                    // 下载成功
                    if (!isFKload) {
                        mHandler.obtainMessage(DOWNLOAD_COMPLETE).sendToTarget();
                        if (MyApplication.mainActivity.getPackageManager().queryIntentActivities(PublicUtil.getInstallAPKIntent(), 0).size() > 0) {
                            MyApplication.mainActivity.startActivity(PublicUtil.getInstallAPKIntent());
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 下载失敗
                mHandler.obtainMessage(DOWNLOAD_FAIL).sendToTarget();
            }
            isDownLoading = false;
        }

        public long downloadUpdateFile(String downloadUrl) throws Exception {

            int downloadCount = 0;
            long totalSize = 0;
            int updateTotalSize = 0;

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;

            try {
                URL url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(10000);
                httpConnection.setReadTimeout(20000);
                updateTotalSize = httpConnection.getContentLength();
                if (httpConnection.getResponseCode() == 404) {
                    throw new Exception("fail!");
                }
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(
                        FileUtil.getFile(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk"),
                        false);
                byte buffer[] = new byte[4096];
                int readsize = 0;
                while ((readsize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readsize);
                    totalSize += readsize;
                    if (!isFKload) {
                        int per = (int) (totalSize * 100 / updateTotalSize);
                        // 为了防止频繁的通知导致应用吃紧，百分比增加2才通知一次
                        if ((downloadCount == 0) || per - 1 > downloadCount) {
                            downloadCount += 2;
                            mHandler.obtainMessage(0, getNotification(per + "%"))
                                    .sendToTarget();
                        }
                    }
                }
                // System.out.print("");
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
            return totalSize;
        }
    }

}