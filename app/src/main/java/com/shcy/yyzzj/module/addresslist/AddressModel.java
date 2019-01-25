package com.shcy.yyzzj.module.addresslist;

import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;

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
