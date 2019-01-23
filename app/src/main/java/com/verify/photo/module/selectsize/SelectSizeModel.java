package com.verify.photo.module.selectsize;

import com.verify.photo.bean.preview.PreviewPhotoListBean;
import com.verify.photo.bean.size.SelectSizeListBean;
import com.verify.photo.config.Constants;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.LoadDataPostJsonObject;
import com.verify.photo.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/10/9.
 */

public class SelectSizeModel {

    interface SelectSizeCallback{
        void onSuccess(SelectSizeListBean bean);
    }
    interface Callback{
        void onResult(HttpResult<PreviewPhotoListBean> result);

        void onFailed();
    }


    public void getSpecList(final SelectSizeCallback callback){
        PhotoHttpManger.getPhotoApi().getSpecList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<SelectSizeListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<SelectSizeListBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data.getData());
                        }else {
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }

    public void getPreviewPhoto(String file, String specId, final Callback callback){
        PhotoHttpManger.getPhotoApi().getPreviewPhoto(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("file","specId"),file,specId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PreviewPhotoListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<PreviewPhotoListBean> data) {
                        callback.onResult(data);
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }
}
