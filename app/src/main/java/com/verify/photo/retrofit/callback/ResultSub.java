package com.verify.photo.retrofit.callback;




import com.verify.photo.retrofit.NetCode;
import com.verify.photo.retrofit.exception.NetException;

import rx.Subscriber;

/**
 * Created by licong on 2018/9/25.
 */

public abstract class ResultSub<T> extends Subscriber<HttpResult<T>> {


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onFilad(NetCode.parseException(e));
    }

    @Override
    public void onNext(HttpResult<T> tHttpResult) {
        onSuccsess(tHttpResult);

    }

    public abstract void onSuccsess(HttpResult<T> data);

    public abstract void onFilad(NetException e);

}
