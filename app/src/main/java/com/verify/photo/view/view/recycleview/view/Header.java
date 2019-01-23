package com.verify.photo.view.view.recycleview.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verify.photo.R;
import com.verify.photo.view.view.recycleview.swipetoloadlayout.SwipeRefreshTrigger;
import com.verify.photo.view.view.recycleview.swipetoloadlayout.SwipeTrigger;


/**
 * Created by Administrator on 2016/7/27.
 */

public class Header extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    int mHeaderHeight;
    TextView info;
    TextView time;
    ImageView arrow;
    RelativeLayout rl_bg;
    Context context;
    boolean rotated;
    private boolean isRecommend = false;

    public Header(Context context) {
        this(context, null);
    }

    public Header(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        String lastTime = new PreferenceUtils("Pull_List").getString(
//                "last_refresh_time_"+lab, "");
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.pulldown_headerview_height);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        info = (TextView) findViewById(R.id.info);
        time = (TextView) findViewById(R.id.time);
        arrow = (ImageView) findViewById(R.id.arrow);
        rl_bg = findViewById(R.id.rl_bg);
    }

    /**
     * 刷新的时候
     */
    @Override
    public void onRefresh() {
        if (isRecommend) {
            info.setText("推荐中...");
        } else {
            info.setText("努力加载中..");
        }

        arrow.post(new Runnable()
        {
            @Override
            public void run()
            {
                ((AnimationDrawable) arrow.getBackground()).start();
            }
        });


    }

    @Override
    public void onPrepare() {

    }

    /**
     * 拖拽时候
     *
     * @param y          偏移量
     * @param isComplete
     * @param automatic
     */
    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            arrow.setVisibility(VISIBLE);
            ((AnimationDrawable) arrow.getBackground()).start();
            if (y >= mHeaderHeight) {
                info.setText("努力加载中..");
//                info.setText("松开后加载");
                if (!rotated) {
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    rotated = false;
                }

                info.setText("努力加载中..");
//                info.setText("下拉后加载...");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    /**
     * 加载完成
     */
    @Override
    public void onComplete() {
        rotated = false;
        ((AnimationDrawable) arrow.getBackground()).stop();
        info.setText("加载完成");
    }

    @Override
    public void onReset() {
        ((AnimationDrawable) arrow.getBackground()).stop();
        info.setVisibility(View.VISIBLE);
    }

    /**
     * 设置是否推荐
     *
     * @param isRecommend
     */
    public void setRecommend(boolean isRecommend) {
        this.isRecommend = isRecommend;
    }
}
