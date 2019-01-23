package com.verify.photo.module.camera;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.verify.photo.R;
import com.verify.photo.activity.CameraUtil;
import com.verify.photo.base.BaseActivity;
import com.verify.photo.config.Constants;
import com.verify.photo.log.L;
import com.verify.photo.module.imagepicker.ImagePicker;
import com.verify.photo.module.selectsize.SelectSizeActivity;
import com.verify.photo.module.soundplayer.SoundPlayer;
import com.verify.photo.utils.DpPxUtils;
import com.verify.photo.utils.PermissionUtil;
import com.verify.photo.utils.SetUtils;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.utils.fresco.FrescoUtils;
import com.zhihu.matisse.Matisse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.verify.photo.module.imagepicker.ImagePicker.REQUEST_CODE_CHOOSE;
import static com.verify.photo.module.selectsize.SelectSizeActivity.IMAGEPATH;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "拍摄图片";

    private final int REQUEST_PERMISSION_CODE = 015;
    private final int SET_PERMISSION_CODE = REQUEST_PERMISSION_CODE + 1;
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;
    private int specId;
    public static final String PREVIEWPHOTOLIST = "previewphotolist";
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    private TextView tips, delayedNum, gonglue;
    private ImageView camera_frontback, camera_close, img_camera, flash_light, daojishi;
    private SimpleDraweeView albumImage;
    private LinearLayout topview, guideLayout;
    private CameraLunboManager cameraLunboManager;
    private View popupView;
    private PopupWindow pop;
    private String imapath = "";
    private long clicktime;
    private Handler handler;
    private int tipsNum = 1;
    private int delayedType;//0无延时，1延时3S, 2延时7S
    private Timer timer = new Timer();
    private int time;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        specId = getIntent().getIntExtra(SelectSizeActivity.SPECID, 0);
        handler = new MyHandler(this);
        PermissionUtil.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_CODE, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {
                PermissionUtil.showExsit(CameraActivity.this, getString(R.string.need_permission_splash), new PermissionUtil.OnSetListener() {
                    @Override
                    public void onSueccess() {

                    }

                    @Override
                    public void onFailed() {

                    }
                }, SET_PERMISSION_CODE);
            }
        });
        setContentView(R.layout.activity_camera);
        context = this;
        initView();
        initData();
        SoundPlayer.init(this);
    }

    private void initView() {
        topview = findViewById(R.id.camera_top_layout);
        surfaceView = findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);
        tips = findViewById(R.id.camera_tips);
        albumImage = findViewById(R.id.camera_album_image);
        albumImage.setOnClickListener(this);
        camera_close = findViewById(R.id.camera_close);
        camera_close.setOnClickListener(this);
        camera_frontback = findViewById(R.id.camera_frontback);
        camera_frontback.setOnClickListener(this);
        flash_light = findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);
        daojishi = findViewById(R.id.cameraactivity_daojishi);
        daojishi.setOnClickListener(this);
        initPopupWindow();
        delayedNum = findViewById(R.id.camera_delayed_num);
        delayedNum.setOnClickListener(this);
        gonglue = findViewById(R.id.cameraactivity_strategy);
        gonglue.setOnClickListener(this);
        guideLayout = findViewById(R.id.camera_guidelayout);
        guideLayout.setOnClickListener(this);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
        params.width = DpPxUtils.getScreenWidth(this);
        params.height = params.width / 9 * 16;
        surfaceView.setLayoutParams(params);

        final IOrientationEventListener orientationEventListener = new IOrientationEventListener(this);

        surfaceView.getHolder().setKeepScreenOn(true);//屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //当SurfaceHolder被创建的时候回调
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                orientationEventListener.enable();
            }

            //当SurfaceHolder的尺寸发生变化的时候被回调
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            //当SurfaceHolder被销毁的时候回调
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                orientationEventListener.disable();
            }
        });
        setTips();

        if (!SetUtils.getInstance().getCameraLunboHasShow()) {
            showGuide();
        }
    }

    private void initData() {
        getImages();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_album_image:
                PermissionUtil.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE, new PermissionUtil.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.getImagesPath(CameraActivity.this, 1, false);
                    }

                    @Override
                    public void onPermissionDenied() {
                        PermissionUtil.showExsit(CameraActivity.this, getString(R.string.need_permission_camera), new PermissionUtil.OnSetListener() {
                            @Override
                            public void onSueccess() {
                            }

                            @Override
                            public void onFailed() {

                            }
                        }, SET_PERMISSION_CODE);
                    }
                });
                break;
            case R.id.img_camera:
                if (isQuickClick()) {
                    return;
                }
                takepictrueDelayed();
                break;

            //前后置摄像头拍照
            case R.id.camera_frontback:
                switchCamera();
                break;

            //退出相机界面 释放资源
            case R.id.camera_close:
                finish();
                break;

            //闪光灯
            case R.id.flash_light:
                if (mCameraId == 1) {
                    //前置
                    ToastUtil.showToast("请切换为后置摄像头开启闪光灯");
                    return;
                }
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        flash_light.setImageResource(R.mipmap.flash_open);
                        break;
                    case 1:
                        //关闭
                        light_num = 0;
                        flash_light.setImageResource(R.mipmap.flash_close);
                        break;
                }
                setFlash(mCamera);
                break;
            case R.id.cameraactivity_daojishi:
                showPopupWindow();
                break;
            case R.id.popupwindow_text1:
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 1;
                daojishi.setImageResource(R.mipmap.daojishi3_icon);
                break;
            case R.id.popupwindow_text2:
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 2;
                daojishi.setImageResource(R.mipmap.daojishi7_icon);
                break;
            case R.id.popupwindow_text3:
                if (pop != null && pop.isShowing()) {
                    pop.dismiss();
                }
                delayedType = 0;
                daojishi.setImageResource(R.mipmap.daojishi_icon);
                break;
            case R.id.cameraactivity_strategy:
                showGuide();
                break;

        }
    }

    private void setFlash(Camera camera) {
        if (mCameraId == 1) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        if (light_num == 0) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);//开启
            camera.setParameters(parameters);
        }
    }

    private void takepictrueDelayed() {
        switch (delayedType) {
            case 0:
                captrue();
                return;
            case 1:
                time = 3;
                break;
            case 2:
                time = 7;
                break;
        }
        delayedNum.setVisibility(View.VISIBLE);
        timer.schedule(new MyTask(), 0, 1000);
        delayedNum.setText(time + "");
    }

    private void initPopupWindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.popupwindow_delayed, null);
        TextView text1 = popupView.findViewById(R.id.popupwindow_text1);
        TextView text2 = popupView.findViewById(R.id.popupwindow_text2);
        TextView text3 = popupView.findViewById(R.id.popupwindow_text3);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        pop = new PopupWindow(popupView, DpPxUtils.getScreenWidth(this), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showPopupWindow() {
        if (pop.isShowing()) {
            pop.dismiss();
        } else {
            int[] location = new int[2];
            topview.getLocationOnScreen(location);
            pop.showAtLocation(topview, Gravity.NO_GRAVITY, location[0], location[1] + topview.getHeight());
        }
    }

    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_CAMERA_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        setFlash(camera);
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        if (camera == null) return;
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        SoundPlayer.playSound(1);
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                ToastUtil.showToast("拍照成功！", false);
                BufferedOutputStream bos = null;
                FileOutputStream fos = null;
                File file = null;
                try {
                    file = getOutputMediaFile();
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                            getToConfirmPictrue(file.getPath());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
//                mCamera.stopPreview();
            }
        });
    }

    private File getOutputMediaFile() {
        File tempFile1 = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath().endsWith("/") ? Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "photo" : Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/photo");
        String timeStamp = new SimpleDateFormat("yyyy-MMdd-HHmmss").format(new Date());
        File tempFile = new File(tempFile1, "verify_photo_" + timeStamp + ".jpg");
        if (!tempFile.exists()) {
            tempFile1.mkdirs();
            try {
                tempFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tempFile;
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Camera.Size optionSize = getOptimalPreviewSize(sizeList, surfaceView.getHeight(), surfaceView.getWidth());//获取一个最为适配的camera.size
            parameters.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPictureSize(optionSize.width, optionSize.height);
            camera.setParameters(parameters);
        } catch (Exception e) {
            L.e("CameraActivity", "set parameters fail");
        }

    }

    /**
     * 解决预览变形问题
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int h, int w) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
        }
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.onActivityResult(requestCode, CameraActivity.this);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0 && !TextUtils.isEmpty(paths.get(0))) {
                getToConfirmPictrue(paths.get(0));
            }
        }
    }

    public void getToConfirmPictrue(String path) {
        Intent intent = new Intent(this, PictrueConfirmActivity.class);
        intent.putExtra(IMAGEPATH, path);
        intent.putExtra(SelectSizeActivity.SPECID, specId + "");
        startActivity(intent);
    }

    public class IOrientationEventListener extends OrientationEventListener {

        public IOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (ORIENTATION_UNKNOWN == orientation) {
                return;
            }
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {
                rotation = (info.orientation + orientation) % 360;
            }
            if (null != mCamera) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setRotation(rotation);
                mCamera.setParameters(parameters);
            }
        }
    }


    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        MineThread mThread = new MineThread();
        mThread.start();
    }

    class MineThread extends Thread {

        @Override
        public void run() {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = CameraActivity.this
                    .getContentResolver();

            // 只查询jpeg和png的图片
            Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png", "image/bmp"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC");

            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));

                //预先验证图片的有效性
                final File file = new File(path);
                if (!file.exists()) {
//                    throw new IllegalArgumentException("Uri 文件不存在");
                    //如果此处抛出异常 说明预见检查到无效的图片 此时 开发者把 下面 continue 放开  再把 抛出异常的代码注释即可
                    continue;
                }
                // 拿到第一张图片的路径
                if (TextUtils.isEmpty(imapath)) {
                    imapath = path;
                    if (albumImage != null) {
                        Message message = new Message();
                        message.what = 101;
                        message.obj = imapath;
                        handler.sendMessage(message);
                    }
                    break;
                }
            }
        }
    }

    private boolean isQuickClick() {
        if (System.currentTimeMillis() - clicktime < 1000) {
            return true;
        }
        clicktime = System.currentTimeMillis();
        return false;
    }

    private void setTips() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (tipsNum % 4) {
                    case 1:
                        tips.setText(R.string.takepictrue_tips1);
                        break;
                    case 2:
                        tips.setText(R.string.takepictrue_tips2);
                        break;
                    case 3:
                        tips.setText(R.string.takepictrue_tips3);
                        break;
                    case 0:
                        tips.setText(R.string.takepictrue_tips4);
                        break;
                }
                tipsNum++;
                setTips();
            }
        }, 3000);
    }

    private static class MyHandler extends Handler {
        WeakReference<CameraActivity> mWeakRefrence;

        public MyHandler(CameraActivity activity) {
            mWeakRefrence = new WeakReference<CameraActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CameraActivity activity = mWeakRefrence.get();
            if (activity != null) {
                switch (msg.what) {
                    case 101:
                        if (activity.albumImage != null) {
                            String loacalpath = (String) msg.obj;
                            FrescoUtils.getInstance().showImageFile(activity.albumImage, loacalpath);
                        }
                }
            }
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if (!isFinishing()) {
                if (this == null) {
                    return;
                }
                CameraActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time == 0) {
                            cancel();
                            captrue();
                            delayedNum.setVisibility(View.GONE);
                        } else {
                            delayedNum.setText(time + "");
                            time--;
                        }
                    }
                });
            } else {
                if (this == null) {
                    return;
                }
                CameraActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (this == null) {
                            cancel();
                            return;
                        }
                        if (time <= 0) {
                            cancel();
                            captrue();
                            delayedNum.setVisibility(View.GONE);
                        } else {
                            CameraActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    delayedNum.setText(time + "");
                                }
                            });
                        }
                    }
                });

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void showGuide() {
        if (guideLayout == null || isFinishing()) {
            return;
        }
        guideLayout.removeAllViews();
        cameraLunboManager = new CameraLunboManager(this);
        guideLayout.addView(cameraLunboManager.getLunboView());
        guideLayout.setVisibility(View.VISIBLE);
        SetUtils.getInstance().setCameraLunboHasShow(true);
    }

    public void closeGuide() {
        if (guideLayout != null) {
            guideLayout.setVisibility(View.GONE);
        }
    }
}
