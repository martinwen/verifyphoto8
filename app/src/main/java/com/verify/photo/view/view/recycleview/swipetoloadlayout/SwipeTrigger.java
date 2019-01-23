package com.verify.photo.view.view.recycleview.swipetoloadlayout;

/**
 * Created by Administrator on 2016/7/27.
 */

public interface SwipeTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}
