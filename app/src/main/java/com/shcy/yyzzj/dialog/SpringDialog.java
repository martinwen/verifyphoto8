package com.shcy.yyzzj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by licong on 2018/4/24.
 */

public class SpringDialog extends Dialog {

    private static boolean Debug = true;
    private boolean init = true;
    protected boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = false;

    WindowManager wm;
    Window win;
    View rightOf;
    View bottomOf;

    int contenerHeight = 0;
    int contenerWidth = 0;

    public SpringDialog(Context context, int theme) {
        super(context, theme);
    }

    public SpringDialog(Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setLayout(WindowManager wm, Window win, View rightOf,
                          View bottomOf) {
        this.wm = wm;
        this.win = win;
        this.rightOf = rightOf;
        this.bottomOf = bottomOf;
    }

    @Override
    public void show() {
        // 初始化参数
        setParams(wm, win, rightOf, bottomOf);
        super.show();
    }

    @Override
    public void setCancelable(boolean flag) {
        mCancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !mCancelable) {
            mCancelable = true;
        }

        mCanceledOnTouchOutside = cancel;
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCancelable && mCanceledOnTouchOutside
                && event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(event)) {
            cancel();
            return true;
        }

        return false;
    }

    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(getContext())
                .getScaledWindowTouchSlop();
        if (Debug) {
            System.out.println("SpringDialog.isOutOfBounds()-->"
                    + getWindow().getWindowManager());
            System.out.println("SpringDialog.isOutOfBounds()");
        }
        return (x < -slop) || (y < -slop) || (x > (contenerWidth + slop))
                || (y > (contenerHeight + slop));
    }

    /**
     * 设置位置
     *
     * @param
     * @param rightOf
     * @param bottomOf
     */
    private void setParams(WindowManager wm, Window win, View rightOf,
                           View bottomOf) {
        if (!init) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = win.getDecorView();
        view.getWindowVisibleDisplayFrame(rect);

        WindowManager.LayoutParams lay = this.getWindow().getAttributes();

        // WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW...
        contenerHeight = -1;
        contenerWidth = -1;
        // WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW...

        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;

        if (rightOf != null) {
            lay.width = dm.widthPixels - 2 * rightOf.getRight();
        }
        if (bottomOf != null) {
            lay.height = dm.heightPixels - rect.top - 2 * bottomOf.getBottom();
        }

        init = false;
    }

}
