package com.verify.photo.retrofit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by licong on 2018/9/25.
 */

public class RxManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }
    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
    }
    /**
     *
     * @param observable 要发送的请求
     * @param subscriber 请求 回来的处理
     */
    public void start(Observable observable, Subscriber subscriber) {
        mCompositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
