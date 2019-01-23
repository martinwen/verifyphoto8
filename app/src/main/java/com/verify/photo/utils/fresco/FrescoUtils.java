package com.verify.photo.utils.fresco;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.verify.photo.R;
import com.verify.photo.log.L;
import com.verify.photo.utils.CommontUtil;
import com.verify.photo.utils.DpPxUtils;
import com.verify.photo.utils.fresco.zoomable.ZoomableDraweeView;


/**
 * Facebook's Fresco 图片加载工具类
 * <p>
 * Note:虽然SimpleDraweeView继承于ImageView,但是最好不要使用ImageView的任何属性.
 * <p>
 * 附常用XML属性配置：
 * fresco:actualImageScaleType="" //图片缩放类型
 * fresco:placeholderImage=""     //默认占位图
 * fresco:placeholderImageScaleType="" //默认占位图缩放类型
 * fresco:roundAsCircle="true"    //显示圆形
 * fresco:roundedCornerRadius=""  //显示圆角
 * <p>
 * 更多属性请详见网址：http://fresco-cn.org/docs/using-drawees-xml.html#_
 * <p>
 * Created by Mr.Wang on 2018/3/29
 */

public class FrescoUtils {

    private static final FrescoUtils FRESCO_INSTANCE = new FrescoUtils();
    private ResizeOptions resizeOptions;

    public static FrescoUtils getInstance() {
        return FRESCO_INSTANCE;
    }

    /**
     * 初始化设置Fresco,设置渐变式加载以及缓存配置
     */
    public ImagePipelineConfig getFrescoConfig(Context context) {

        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setMaxCacheSize(50 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(20 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(8 * ByteConstants.MB)
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(pjpegConfig)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setDownsampleEnabled(true)
                .build();
        return config;
    }


    /**
     * 显示网络图片，无占位图
     *
     * @param draweeView
     * @param url
     */
    public void showImage(SimpleDraweeView draweeView, String url) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, 0);
        }
    }

    /**
     * 显示网络图片，带占位图、错误图
     *
     * @param draweeView
     * @param url
     * @param defaultResourceId 占位图图片资源id
     */
    public void showImage(SimpleDraweeView draweeView, String url, int defaultResourceId) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, defaultResourceId);
        } else {
            showImageResource(draweeView, defaultResourceId);
        }
    }

    /**
     * 显示网络图片，带占位图，错误图以及Fresco默认图片加载监听器
     *
     * @param draweeView
     * @param url
     * @param defaultResourceId
     * @param controllerListener Fresco默认图片加载监听器
     */
    public void showImage(SimpleDraweeView draweeView, String url, int defaultResourceId, BaseControllerListener<ImageInfo> controllerListener) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, defaultResourceId, controllerListener);
        } else {
            showImageResource(draweeView, defaultResourceId);
        }
    }

    /**
     * 显示网络图片，带占位图，错误图以及自定义图片加载监听器（可获得缓存的bitmap对象）
     *
     * @param draweeView
     * @param url
     * @param defaultResourceId
     * @param listener          自定义图片加载监听器
     */
    public void showImage(SimpleDraweeView draweeView, String url, int defaultResourceId, ImageRequestListener listener) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, defaultResourceId, listener);
        } else {
            showImageResource(draweeView, defaultResourceId);
        }
    }

    /**
     * 显示网络图片，不带占位图，带自定义图片加载监听器（可获得缓存的bitmap对象）
     *
     * @param draweeView
     * @param url
     * @param listener   自定义图片加载监听器
     */
    public void showImage(SimpleDraweeView draweeView, String url, ImageRequestListener listener) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, 0, listener);
        }
    }

    /**
     * 显示网络图片，不带占位图，带自定义图片加载监听器（可获得缓存的bitmap对象）
     *
     * @param draweeView
     * @param url
     */
    public void showBlurImage(SimpleDraweeView draweeView, String url) {
        if (!TextUtils.isEmpty(url)) {
            configBlurImage(draweeView, url);
        }
    }


    /**
     * 显示网络图片，带占位图、错误图、圆角。（显示圆角也可在在xml文件中配置）
     *
     * @param draweeView
     * @param url
     * @param defaultResourceId
     * @param cornerRadius      圆角半径，dp单位
     */
    public void showImage(SimpleDraweeView draweeView, String url, int defaultResourceId, int cornerRadius) {
        if (!TextUtils.isEmpty(url)) {
            configImage(draweeView, url, defaultResourceId, cornerRadius);
        } else {
            showImageResource(draweeView, defaultResourceId, cornerRadius);
        }
    }


    /**
     * 显示网络图片，设置无缓存, 带占位图
     *
     * @param draweeView
     * @param url
     * @param defaultResourceId
     */
    public void showImageNoCache(SimpleDraweeView draweeView, String url, int defaultResourceId) {
        if (!TextUtils.isEmpty(url)) {
            configImageNoCache(draweeView, url, defaultResourceId);
        } else {
            showImageResource(draweeView, defaultResourceId);
        }
    }

    /**
     * 显示网络图片，设置无缓存，不带占位图
     *
     * @param draweeView
     * @param url
     */
    public void showImageNoCache(SimpleDraweeView draweeView, String url) {
        if (!TextUtils.isEmpty(url)) {
            configImageNoCache(draweeView, url, 0);
        }
    }


    /**
     * 显示本地资源
     *
     * @param draweeView
     * @param resourceId 资源id
     */
    public void showImageResource(SimpleDraweeView draweeView, int resourceId) {
        draweeView.setImageURI(Uri.parse("res://" + CommontUtil.getGlobeContext().getPackageName() + "/" + resourceId));
    }

    /**
     * 显示本地GIF图片资源
     *
     * @param draweeView
     * @param resourceId GIF图
     */
    public void showImageGIFResource(SimpleDraweeView draweeView, int resourceId, String path) {

        Uri uri;
        if (TextUtils.isEmpty(path)) {
            uri = Uri.parse("res://" + CommontUtil.getGlobeContext().getPackageName() + "/" + resourceId);
        } else {
            uri = Uri.parse("file:///" + path);
        }


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    /**
     * 显示本地GIF图片资源（有占位图）
     *
     * @param draweeView
     * @param defaultResourceId  占位图
     * @param gifResourceId      GIF图
     * @param controllerListener
     */
    public void showImageGIFResource(SimpleDraweeView draweeView, int defaultResourceId, int gifResourceId, BaseControllerListener<ImageInfo> controllerListener) {
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res://" + CommontUtil.getGlobeContext().getPackageName() + "/" + gifResourceId))
                .setOldController(draweeView.getController())
                .setControllerListener(controllerListener)
                .build();
        draweeView.setController(controller);
    }

    /**
     * 显示本地文件 （默认不对图片裁剪，适用于显示单张或少量图片）。
     *
     * @param draweeView
     * @param localPath
     */
    public void showImageFile(SimpleDraweeView draweeView, String localPath) {
        configImageFile(draweeView, localPath, false);
    }

    /**
     * 显示本地文件
     *
     * @param draweeView
     * @param localPath
     * @param isClip     是否裁剪本地文件图片,适用于显示大量图片，防止OOM。
     */
    public void showImageFile(SimpleDraweeView draweeView, String localPath, boolean isClip) {
        configImageFile(draweeView, localPath, isClip);
    }


    /**
     * 显示本地资源，带圆角（显示圆角也可在在xml文件中配置）
     *
     * @param draweeView
     * @param resourceId   资源id
     * @param cornerRadius 圆角半径，dp单位
     */
    public void showImageResource(SimpleDraweeView draweeView, int resourceId, int cornerRadius) {
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams == null) {
            roundingParams = new RoundingParams();
        }
        roundingParams.setCornersRadius(DpPxUtils.dp2px(CommontUtil.getGlobeContext(), cornerRadius));
        hierarchy.setRoundingParams(roundingParams);
        showImageResource(draweeView, resourceId);
    }

    private void configImageNoCache(SimpleDraweeView draweeView, String url, int defaultResourceId) {
        if (defaultResourceId > 0) {
            GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
            hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .disableDiskCache()
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    private void configImage(SimpleDraweeView draweeView, String url, int defaultResourceId) {
        if (defaultResourceId > 0) {
            GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
            hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)  //图片加载完成后，若是gif图自动播放
                .build();
        draweeView.setController(controller);
    }

    private void configBlurImage(SimpleDraweeView draweeView, String url) {
//        ImageRequest request =
//                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                        .setPostprocessor(new IterativeBoxBlurPostProcessor(1, 1))
//                        .build();
//
//        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setOldController(draweeView.getController())
//                .setImageRequest(request)
//                .build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(80, 40)).build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
    }


    private void configImage(SimpleDraweeView draweeView, String url, int defaultResourceId, int cornerRadius) {
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams == null) {
            roundingParams = new RoundingParams();
        }
        roundingParams.setCornersRadius(DpPxUtils.dp2px(CommontUtil.getGlobeContext(), cornerRadius));
        hierarchy.setRoundingParams(roundingParams);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    private void configImage(SimpleDraweeView draweeView, String url, int defaultResourceId, BaseControllerListener<ImageInfo> controllerListener) {
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)
                .setControllerListener(controllerListener)
                .build();
        draweeView.setController(controller);
    }

    private void configImage(final SimpleDraweeView draweeView, String url, int defaultResourceId, final ImageRequestListener listener) {
        if (defaultResourceId > 0) {
            GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
            hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        }
        final ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        super.onSubmit(id, callerContext);
                        if (listener != null) {
                            listener.onLoadingStarted(draweeView, id, callerContext);
                        }
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (listener != null) {
                            listener.onLoadingComplete(draweeView, id, imageInfo, animatable);
                            setDataSubscriber(draweeView, request, listener);
                        }
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        if (listener != null) {
                            listener.onLoadingFailed(draweeView, id, throwable);
                        }
                    }

                    @Override
                    public void onRelease(String id) {
                        super.onRelease(id);
                    }
                })
                .build();
        draweeView.setController(controller);
    }

    public void setDataSubscriber(final SimpleDraweeView draweeView, ImageRequest request, final ImageRequestListener listener) {
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            if (listener != null) {
                                listener.onLoadingBitmapComplete(draweeView, bitmap);
                            }
                        } else {
                            L.e("onNewResultImpl方法获取到的bitmap==null或释放掉了");
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                // handle failure
                if (listener != null) {
                    listener.onLoadingBitmapFailed(draweeView, throwable);
                }
            }
        };
        getBitmap(request, dataSubscriber);
    }

    private void configImageFile(SimpleDraweeView draweeView, String localPath, boolean isClip) {
        if (!localPath.startsWith("file://")) {
            localPath = "file://" + localPath;
        }

        if (isClip) {
            if (resizeOptions == null) {
                resizeOptions = new ResizeOptions(200, 200);
            }
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(localPath))
                    .setResizeOptions(resizeOptions)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .build();
            draweeView.setController(controller);
        } else {
            try {
                draweeView.setImageURI(Uri.parse(localPath));
            } catch (java.lang.IllegalStateException e) {
                showImageResource(draweeView, R.mipmap.album_err);
            }
        }
    }

    private void getBitmap(ImageRequest request, DataSubscriber dataSubscriber) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(request, CommontUtil.getGlobeContext());
        dataSource.subscribe(dataSubscriber, CallerThreadExecutor.getInstance());
    }

    private Drawable getDrawable(int defaultResourceId) {
        return CommontUtil.getGlobeContext().getResources().getDrawable(defaultResourceId);
    }

    /**
     * 加载GIF
     *
     * @param draweeView
     * @param url
     * @param isAuto     是否自动播放
     */
    public void DraweeViewGif(SimpleDraweeView draweeView, String url, int defaultResourceId, boolean isAuto) {
        if (defaultResourceId > 0) {
            GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
            hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
        }

//        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(uri)//设置GIF网址
                .setImageRequest(request)
                .setAutoPlayAnimations(isAuto)//是否自动播放动画,false为不播放
                .setOldController(draweeView.getController())//内存优化
                .build();
        draweeView.setController(controller);

    }

    public void DraweeViewGifResouce(SimpleDraweeView draweeView, int resourceId, boolean isAuto) {
        DraweeViewGifResouce(draweeView, resourceId, isAuto, 0);
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri(Uri.parse("res://"+CommontUtil.getGlobeContext().getPackageName()+"/" + resourceId))
//                .setOldController(draweeView.getController())
//                .setAutoPlayAnimations(isAuto)
//                .build();
//        draweeView.setController(controller);
    }

    public void DraweeViewGifResouce(SimpleDraweeView draweeView, int resourceId, boolean isAuto, int cornerRadius) {
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        hierarchy.setPlaceholderImage(getDrawable(resourceId), ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setFailureImage(getDrawable(resourceId), ScalingUtils.ScaleType.CENTER_CROP);
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams == null) {
            roundingParams = new RoundingParams();
        }
        roundingParams.setCornersRadius(DpPxUtils.dp2px(CommontUtil.getGlobeContext(), cornerRadius));
        hierarchy.setRoundingParams(roundingParams);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res://" + CommontUtil.getGlobeContext().getPackageName() + "/" + resourceId))
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(isAuto)
                .build();
        draweeView.setController(controller);
    }

    //点击暂停播放
    public void stop(SimpleDraweeView san) {
        // 动画停止
        //拿到动画对象
        Animatable animatableStop = san.getController().getAnimatable();
        //进行非空及是否动画在播放判断
        if (animatableStop != null && animatableStop.isRunning()) {
            //动画在播放,停止动画播放
            animatableStop.stop();
        }
    }

    //点击开始播放
    public void start(SimpleDraweeView san) {
        // 动画开始
        //拿到动画对象
        if (san.getController() == null) return;
        Animatable animatableStart = san.getController().getAnimatable();
        //进行非空及是否动画在播放判断
        if (animatableStart != null && !animatableStart.isRunning()) {
            //动画停止播放,播放动画
            animatableStart.start();
        }
    }

    public void configImage(ZoomableDraweeView draweeView, String url, int defaultResourceId, String thumeuri) {
//        if (defaultResourceId > 0) {
//            GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
//            hierarchy.setPlaceholderImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
//            hierarchy.setFailureImage(getDrawable(defaultResourceId), ScalingUtils.ScaleType.CENTER_CROP);
//        }

//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setLowResImageRequest(ImageRequest.fromUri(thumeuri))
//                .setOldController(draweeView.getController())
//                .build();
//        draweeView.setController(controller);

//---------------------------------------------------------------------

//        DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
//                Uri.parse( url)).setTapToRetryEnabled(true).build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).setProgressiveRenderingEnabled(true).build();
        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setLowResImageRequest(ImageRequest.fromUri(thumeuri))
                .setOldController(draweeView.getController())
                .build();

        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(CommontUtil.getGlobeContext().getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build();

        draweeView.setController(ctrl);
        draweeView.setHierarchy(hierarchy);


    }
}
