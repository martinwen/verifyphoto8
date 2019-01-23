package com.verify.photo.retrofit.transformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Mzc
 * @time 2017-01-17 11:22
 */
public class SubsTransformer {
    public static Observable.Transformer MAINTHREAD = new  Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable)  observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}
