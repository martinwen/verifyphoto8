package com.verify.photo.module.album;

import com.verify.photo.bean.album.AlbumListBean;
import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.LoadDataPostJsonObject;
import com.verify.photo.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/9/29.
 */

public class AlbumModel {
    interface Callback {
        void onSuccess(HttpResult data);

        void onFailed();
    }

    public void getPhotoList(int pageNO,final Callback callback) {
        PhotoHttpManger.getPhotoApi().getPhotoList(pageNO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<AlbumListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<AlbumListBean> data) {
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                    }
                });
    }

    public void deletePhoto(String photoId){
        PhotoHttpManger.getPhotoApi().deletePhoto(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("photoId"),photoId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {

                    }

                    @Override
                    public void onFilad(NetException e) {

                    }
                });
    }
}
