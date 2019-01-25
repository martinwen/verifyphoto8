package com.shcy.yyzzj.module.splash;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.shcy.yyzzj.R;

import java.lang.ref.WeakReference;


/**
 * Created by hy on 2015/8/17.
 */
public class SplashLunboManager implements ViewPager.OnPageChangeListener, View.OnClickListener, View.OnTouchListener {
    private int[] listLunbo = new int[]{R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three};
    private View point1, point2, point3;
    private LinearLayout pointLayout;
    private final int DURATION_TIME = 4000;
    private Context mContext;
    private ViewPager vp;
    private GuideAdapter adpter;
    /**
     * 是否正在触摸状态，用以防止触摸滑动和自动轮播冲突
     */
    private boolean mIsTouching = false;
    private Handler handler;

    public SplashLunboManager(Context context) {
        this.mContext = context;
        handler = new MyHandler(this);
    }

    public View getLunboView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guide_lunbo, null);
        vp = view.findViewById(R.id.vp_guide);
        point1 = view.findViewById(R.id.guide_point1);
        point2 = view.findViewById(R.id.guide_point2);
        point3 = view.findViewById(R.id.guide_point3);
        pointLayout = view.findViewById(R.id.guide_point_layout);
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
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back_select));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                point1.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back));
                point2.setBackground(mContext.getResources().getDrawable(R.drawable.guide_point_back_select));
                pointLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                pointLayout.setVisibility(View.GONE);
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
        vp.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = (Integer) v.getTag(R.id.lunbo_postion);
        if (i == listLunbo.length - 1) {
            //轮播到最后一页
//            Intent intent = new Intent(mContext, ForkActivity.class);
//            mContext.startActivity(intent);
            ((SplashActivity) mContext).closeGuide();


        }
    }

    public void destory() {
        if (vp != null) {
            handler.removeMessages(1001);
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
        WeakReference<SplashLunboManager> weakReference;

        public MyHandler(SplashLunboManager splashLunboManager) {
            weakReference = new WeakReference<SplashLunboManager>(splashLunboManager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final SplashLunboManager splashLunboManager = weakReference.get();
            if (splashLunboManager != null) {
                if (splashLunboManager.vp != null && !splashLunboManager.mIsTouching) {
                    if (splashLunboManager.listLunbo.length > (int) splashLunboManager.vp.getTag(R.id.viewpage_postion) + 1) {
                        splashLunboManager.vp.setCurrentItem(splashLunboManager.vp.getCurrentItem() + 1);
                        this.sendEmptyMessageDelayed(1001, DURATION_TIME);
                    } else {
                        splashLunboManager.handler.removeMessages(1001);
                    }
                }
            }
        }
    }
}
