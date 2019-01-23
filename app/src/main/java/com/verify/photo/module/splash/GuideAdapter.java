package com.verify.photo.module.splash;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.verify.photo.R;
import com.verify.photo.utils.fresco.FrescoUtils;

/**
 * 内容页图集适配器
 */

public class GuideAdapter extends PagerAdapter {
    private Context context;
    private int[] data;
    private View.OnClickListener mListener;

    public GuideAdapter(Context context, int[] datas, View.OnClickListener listener) {
        this.context = context;
        this.data = datas;
        this.mListener = listener;

    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        SimpleDraweeView iv_img = new SimpleDraweeView(container.getContext());

        iv_img.setTag(R.id.lunbo_postion, position);
        FrescoUtils.getInstance().showImageResource(iv_img, data[position]);

        if (mListener != null)
            iv_img.setOnClickListener(mListener);

        container.addView(iv_img);
        return iv_img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
