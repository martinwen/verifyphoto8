package com.shcy.yyzzj.view.view.recycleview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.SwipeTrigger;


/**
 * Created by Administrator on 2016/7/28.
 */

public class Footer extends RelativeLayout implements SwipeLoadMoreTrigger,SwipeTrigger {
    TextView info;
    ProgressBar pb;
    int mFooterHeight;
    public Footer(Context context) {
        this(context,null);
    }

    public Footer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Footer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       info= (TextView) findViewById(R.id.tv_loadmore);
       pb= (ProgressBar) findViewById(R.id.pb);
    }

    @Override
    public void onLoadMore() {
        info.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            pb.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            if (-y >= mFooterHeight) {
                info.setText("加载更多");
            } else {
                info.setText("加载更多");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {

    }
}
