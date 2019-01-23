package com.verify.photo.module.camera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.verify.photo.R;
import com.verify.photo.utils.DpPxUtils;

import java.lang.ref.WeakReference;


/**
 * Created by hy on 2015/8/17.
 */
public class CameraLunboManager implements ViewPager.OnPageChangeListener, View.OnClickListener, View.OnTouchListener {
    private int[] listLunbo = new int[]{R.mipmap.camera_guide1, R.mipmap.camera_guide2, R.mipmap.camera_guide3, R.mipmap.camera_guide4, R.mipmap.camera_guide5};
    private View point1, point2, point3, point4, point5;
    private LinearLayout pointLayout;
    private final int DURATION_TIME = 4000;
    private Context mContext;
    private ViewPager vp;
    private ImageView close;
    private GuideAdapter adpter;
    /**
     * 是否正在触摸状态，用以防止触摸滑动和自动轮播冲突
     */
    private boolean mIsTouching = false;
    private Handler handler;

    public CameraLunboManager(Context context) {
        this.mContext = context;
        handler = new MyHandler(this);
    }

    public View getLunboView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.camera_guide, null);
        vp = view.findViewById(R.id.vp_guide);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vp.getLayoutParams();
        params.width = DpPxUtils.getScreenWidth(mContext) - DpPxUtils.dp2px(mContext, 40);
        params.height = params.width * 888 / 624;
        vp.setLayoutParams(params);
        point1 = view.findViewById(R.id.guide_point1);
        point2 = view.findViewById(R.id.guide_point2);
        point3 = view.findViewById(R.id.guide_point3);
        point4 = view.findViewById(R.id.guide_point4);
        point5 = view.findViewById(R.id.guide_point5);
        pointLayout = view.findViewById(R.id.guide_point_layout);
        close = view.findViewById(R.id.cameraguide_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) mContext).closeGuide();
                destory();
            }
        });
        setData();
        return view;
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        vp.setTag(R.id.viewpage_postion, i);
        switch (i) {
            case 0:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.camera_lunbopoint_back_select));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point3.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point4.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point5.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.camera_lunbopoint_back_select));
                point3.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point4.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point5.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point3.setBackground(mContext.getResources().getDrawable(R.drawable.camera_lunbopoint_back_select));
                point4.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point5.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point3.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point4.setBackground(mContext.getResources().getDrawable(R.drawable.camera_lunbopoint_back_select));
                point5.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point3.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point4.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point5.setBackground(mContext.getResources().getDrawable(R.drawable.camera_lunbopoint_back_select));
                pointLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void setData() {
        adpter = new GuideAdapter(mContext, listLunbo, this);
        vp.setAdapter(adpter);
        vp.setOnPageChangeListener(this);
        vp.setCurrentItem(0);
        vp.setTag(R.id.viewpage_postion, 0);
        handler.sendEmptyMessageDelayed(1001, DURATION_TIME);
    }


    @Override
    public void onClick(View v) {
        int i = (Integer) v.getTag(R.id.lunbo_postion);
        if (i == listLunbo.length - 1) {
            ((CameraActivity) mContext).closeGuide();
            destory();
        }
    }

    public void destory() {
        if (vp != null) {
            handler.removeMessages(1001);
            vp.setCurrentItem(0);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIsTouching = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsTouching = false;
                break;
        }
        return false;
    }

    private class MyHandler extends Handler {
        WeakReference<CameraLunboManager> weakReference;

        public MyHandler(CameraLunboManager cameraLunboManager) {
            weakReference = new WeakReference<CameraLunboManager>(cameraLunboManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final CameraLunboManager cameraLunboManager = weakReference.get();
            if (cameraLunboManager != null) {
                if (cameraLunboManager.vp != null && !cameraLunboManager.mIsTouching) {
                    if (cameraLunboManager.listLunbo.length > (int) cameraLunboManager.vp.getTag(R.id.viewpage_postion) + 1) {
                        cameraLunboManager.vp.setCurrentItem(cameraLunboManager.vp.getCurrentItem() + 1);
                        this.sendEmptyMessageDelayed(1001, DURATION_TIME);
                    } else {
                        cameraLunboManager.handler.removeMessages(1001);
                    }
                }
            }
        }
    }

    private class GuideAdapter extends PagerAdapter {
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

            ImageView iv_img = new SimpleDraweeView(container.getContext());
            iv_img.setTag(R.id.lunbo_postion, position);
            iv_img.setImageResource(data[position]);
            if (mListener != null)
                iv_img.setOnClickListener(mListener);

            container.addView(iv_img);
            ViewGroup.LayoutParams params = iv_img.getLayoutParams();
            params.width = DpPxUtils.getScreenWidth(context) - DpPxUtils.dp2px(context, 40);
            params.height = params.width * 888 / 624;
            iv_img.setLayoutParams(params);
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
}
