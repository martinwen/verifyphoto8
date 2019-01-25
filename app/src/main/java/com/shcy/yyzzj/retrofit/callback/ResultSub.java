package com.shcy.yyzzj.retrofit.callback;




import com.shcy.yyzzj.retrofit.NetCode;
import com.shcy.yyzzj.retrofit.exception.NetException;

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
