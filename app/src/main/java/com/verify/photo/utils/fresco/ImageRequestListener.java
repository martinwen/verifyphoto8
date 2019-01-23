package com.verify.photo.utils.fresco;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.widget.ImageView;

import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Fresco 图片加载监听器
 * Created by Mr.Wang on 2018/3/29.
 */

public interface ImageRequestListener  {
    void onLoadingStarted(ImageView view, String id, Object callerContext);
    void onLoadingFailed(ImageView view, String id, Throwable throwable);
    void onLoadingComplete(ImageView view, String id, ImageInfo imageInfo, Animatable animatable);
    void onLoadingBitmapComplete(ImageView view, Bitmap bitmap);
    void onLoadingBitmapFailed(ImageView view, Throwable throwable);
}
