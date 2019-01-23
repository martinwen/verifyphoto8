package com.verify.photo.utils;

import android.view.View;

/**
 * 批量设置view的显示与隐藏
 * Created by wangshoubo on 2016/4/26
 */
public class ViewUtil {

    public static void setViewVisiblity(int visiblity, View... views) {
        if (views == null || views.length == 0) return;
        for (View acitonView : views) {
            if (acitonView.getVisibility() == visiblity) continue;

            acitonView.setVisibility(visiblity);
        }
    }
}
