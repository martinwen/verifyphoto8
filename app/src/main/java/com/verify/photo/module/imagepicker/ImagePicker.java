package com.verify.photo.module.imagepicker;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;

import com.verify.photo.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

/**
 * Created by licong on 2018/9/18.
 */

public class ImagePicker {

    public static final int REQUEST_CODE_CHOOSE = 23;
    public static void getImagesPath(Activity activity,int limit,boolean takepictrue){
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                .theme(R.style.image_picker)
                .countable(true)
                .capture(takepictrue)
                .captureStrategy(
                        new CaptureStrategy(true, "com.verify.photo.fileProvider"))
                .maxSelectable(limit)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public static void getImagesPath(Fragment fragment, int limit,boolean takepictrue){
        Matisse.from(fragment)
                .choose(MimeType.ofImage())
                .theme(R.style.image_picker)
                .countable(true)
                .capture(takepictrue)
                .captureStrategy(
                        new CaptureStrategy(true, "com.verify.photo.fileProvider"))
                .maxSelectable(limit)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        fragment.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .forResult(REQUEST_CODE_CHOOSE);
    }
}
