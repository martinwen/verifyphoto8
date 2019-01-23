package com.verify.photo.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.smtt.sdk.WebView;
import com.verify.photo.log.L;


public class X5WebView extends WebView implements View.OnTouchListener {
    public ScrollInterface mScrollInterface;

    public X5WebView(Context context) {
        super(context);
        // setOnTouchListener(this);

        // =================设置长按监听方法，两种方法==================

        // 方法一：对所有版本都适用（暂时建议使用此方法）
        // this.setOnLongClickListener(mLongClickListener);

        // 方法二：只对5.3及以上版本的x5内核适用；5.2及以下版本，这种方法会覆盖内核的长按监听，导致长按后，内核无相应。
        // this.getView().setOnLongClickListener(mLongClickListener);

        // =================设置长按监听方法 end=======================
        init();
    }

    public X5WebView(Context context, AttributeSet arg1) {
        super(context, arg1);
        init();
    }

    private void init() {
    }

    // 自定义长按监听
    OnLongClickListener mLongClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            /*
			 * 长按事件处理。。。
			 */
            L.e("DemoWebView", "DemoWebView-onLongClick()");

            return false; // 返回false，则会继续传递长按事件到内核处理。
            // return true; //返回true，则停止传递。
        }
    };

    // @Override
    // protected boolean drawChild(Canvas canvas, View child, long drawingTime)
    // {
    // boolean ret = super.drawChild(canvas, child, drawingTime);
    // canvas.save();
    // Paint paint = new Paint();
    // paint.setColor(0x7fff0000);
    // paint.setTextSize(24.f);
    // paint.setAntiAlias(true);
    // if (getX5WebViewExtension() != null)
    // canvas.drawText("X5 Core:" + WebView.getQQBrowserVersion(), 10, 50,
    // paint);
    // else
    // canvas.drawText("Sys Core", 10, 50, paint);
    // canvas.restore();
    // return ret;
    // }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        L.e("DemoWebView", "onTouch " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return false;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if(mScrollInterface!=null){
            mScrollInterface.onSChanged(l, t, oldl, oldt);
        }
    }

    public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {
            this.mScrollInterface = scrollInterface;
    }

    public interface ScrollInterface {

        public void onSChanged(int l, int t, int oldl, int oldt);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
