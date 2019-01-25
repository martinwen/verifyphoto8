package com.shcy.yyzzj.module.album;

import com.shcy.yyzzj.bean.album.AlbumListBean;
import com.shcy.yyzzj.bean.login.ResultBean;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;

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
