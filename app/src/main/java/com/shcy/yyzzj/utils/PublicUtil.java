package com.shcy.yyzzj.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.ViewConfiguration;

import com.shcy.yyzzj.activity.MyApplication;
import com.shcy.yyzzj.config.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by licong on 2018/9/25.
 */

public class PublicUtil {

    private static Map<String, String> map = new HashMap<String, String>();

    public static PackageInfo getAppPackageInfo() {
        PackageInfo info = null;
        PackageManager manager = CommontUtil.getGlobeContext()
                .getPackageManager();
        try {
            info = manager.getPackageInfo(CommontUtil.getGlobeContext()
                    .getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String getChannelName(Context ctx) {
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public static String getVersionCode(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion + "";
    }

    public static String getVersionName(Context ctx) {
        String localVersion = "V1.0.0";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "v" + localVersion;
    }

    public static boolean isDebug(Context ctx) {
        try {
            ApplicationInfo info = ctx.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static String getBuildTpe(Context ctx) {
        if (isDebug(ctx)) {
            return "debug";
        }
        return "release";
    }

    public static String getCommentNumText(int num) {
        if (num == 0) {
            return "";
        }
        return "评论(" + num + ")";
    }

    public static boolean IsForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 验证邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean vertifyEmail(String email) {
        Pattern patternMail = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcherMail = patternMail.matcher(email.trim());
        return matcherMail.find();
    }

    /**
     * 验证密码是否合法
     *
     * @param pass 密码
     * @return
     */
    public static boolean vertifyPassword(String pass) {
        Pattern pattern = Pattern.compile("^.{6,16}$");
        Matcher matcher = pattern.matcher(pass.trim());
        return matcher.find();
    }

    /**
     * 验证手机号合法
     *
     * @param phone
     * @return
     */
    public static boolean vertifyPhone(String phone) {
        Pattern pattern = Pattern.compile("^[0-9]{11}$");
        Matcher matcher = pattern.matcher(phone.trim());
        return matcher.find();

    }

    /**
     * 验证标签和别名的合法性
     *
     * @param s
     * @return
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 获取16:9图片高度
     **/
    public static int getHeightImage16_9(int mImageWidth) {
        return (mImageWidth * 9) / 16;
    }

    /**
     * 获取4:3图片高度
     **/
    public static int getWidthImage3_4(int mImageHeigh) {
        return (mImageHeigh * 4) / 3;
    }

    /**
     * 获取3:2图片高度
     **/
    public static int getHeighImage3_2(int mImageWidth) {
        return (mImageWidth * 2) / 3;
    }

    /**
     * 判断网络是否正常
     *
     * @return
     */
    public static boolean isNetworkConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CommontUtil.getGlobeContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if (activeInfo != null) {
            return activeInfo.isAvailable();
        }
        return false;
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 检查拥有的权限
     */
    public static ArrayList<String> getPermission(Activity activity) {
        ArrayList<String> list = new ArrayList<String>();
        if (Build.VERSION.SDK_INT < 23) return list;

        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
        };
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                list.add(permissions[i]);
            }
        }
        return list;
    }

    /**
     * 用于检查权限是否被禁用过
     */
    public static boolean checkPermissionHasChanged(ArrayList<String> news, ArrayList<String> old) {
        for (String permision : old) {
            if (!news.contains(permision)) {
                return true;
            }
        }
        return false;
    }

    private static void initMap() {
        map.put("channelverifyphoto", "3000");
        map.put("channel360", "1000");
        map.put("channeltx", "1001");
        map.put("channelbaidu", "1002");
        map.put("channelalibaba", "1003");
        map.put("channelanzhi", "1004");
        map.put("channelxiaomi", "1005");
        map.put("channeloppo", "1006");
        map.put("channelvivo", "1007");
        map.put("channelhuawei", "1008");
        map.put("channelmeizu", "1009");
        map.put("channellianxiang", "1010");
        map.put("channelleshi", "1011");
        map.put("channelsougou", "1012");
        map.put("channelyiyonghui", "1013");
        map.put("channelchuizi", "1014");
    }

    public static Set<String> getCookies() {
        SharePreUtils sharePreUtils = new SharePreUtils(Constants.SHAREPRE_PHOTO_COKIES);
//		Set<String> cookies = sharePreUtils.getCookies();

        return sharePreUtils.getCookies();
    }

    public static Map<String, String> getChannelMap() {
        if (map == null || map.size() == 0) {
            initMap();
        }
        return map;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    public static String getImageString(String path) {
        String imagefile = path;
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagefile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        if (bm == null) return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static void downloadImage(final String imageUrl, final String photoId, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpConnection = null;
                InputStream is = null;
                FileOutputStream fos = null;
                //系统相册目录
                String galleryPath = Environment.getExternalStorageDirectory()
                        + File.separator + Environment.DIRECTORY_DCIM
                        + File.separator + "Camera" + File.separator;
                File file = new File(galleryPath, "verifyphoto" + photoId + ".jpg");
                try {
                    URL url = new URL(imageUrl);
                    httpConnection = (HttpURLConnection) url.openConnection();
                    httpConnection.setConnectTimeout(10000);
                    httpConnection.setReadTimeout(20000);
                    if (httpConnection.getResponseCode() == 404) {
                        throw new Exception("fail!");
                    }
                    is = httpConnection.getInputStream();
                    fos = new FileOutputStream(
//                            FileUtil.getFile(Constants.SDCARD_PATH + "verifyphoto" + photoId + ".png"),
                            file,
                            false);
                    byte buffer[] = new byte[4096];
                    int readsize = 0;
                    while ((readsize = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, readsize);
                    }
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpConnection != null) {
                        httpConnection.disconnect();
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }, "Avatar Download").start();
    }

    public static Intent getInstallAPKIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MyApplication.getContext(), "com.shcy.yyzzj.fileProvider", new File(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk"));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Constants.SDCARD_PATH + "verifyphoto_" + SetUtils.getInstance().getBuildNo() + ".apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }
}
