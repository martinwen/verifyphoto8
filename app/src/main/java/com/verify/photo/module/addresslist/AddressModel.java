package com.verify.photo.module.addresslist;

import com.verify.photo.bean.address.AddressListBean;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/11/13.
 */

public class AddressModel {

    public interface AddressCallback{
        void onSuccess(HttpResult result);

        void onFailed();
    }

    public void getAddressList(final AddressCallback callback){
        PhotoHttpManger.getPhotoApi().getAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<AddressListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<AddressListBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data);
                        }else {
                            callback.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                    }
                });
    }
}
