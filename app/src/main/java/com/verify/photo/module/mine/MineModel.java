package com.verify.photo.module.mine;

import com.verify.photo.bean.customserver.ServerMessageBean;
import com.verify.photo.bean.share.ShareAppBean;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/10/19.
 */

public class MineModel {

    interface MineCallback{
        void onSuccess(HttpResult data);

        void onFailed();
    }

    public void getServerMsg(final MineCallback callback){
        PhotoHttpManger.getPhotoApi().getServerMsg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ServerMessageBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ServerMessageBean> data) {
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

    public void getShareAppConfig(final MineCallback callback){
        PhotoHttpManger.getPhotoApi().getShareAppConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ShareAppBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ShareAppBean> data) {
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
