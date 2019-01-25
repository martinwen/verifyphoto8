package com.shcy.yyzzj.module.camera;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;
import com.shcy.yyzzj.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/10/9.
 */

public class CameraModel {

    interface PhotographCallback{
        void onResult(HttpResult<PreviewPhotoListBean> result);

        void onFailed();
    }

    interface GetPreviewStatusCallback {
        void onSuccess();
    }

    public void getPreviewPhoto(String file, String specId, final PhotographCallback callback){
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
                        callback.onFailed();
                        ToastUtil.showToast(Constants.NETERROR,true);
                    }
                });
    }

    public void getPreviewStatus(final GetPreviewStatusCallback callback) {
        PhotoHttpManger.getPhotoApi().getPreviewStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Object>() {

                    @Override
                    public void onSuccsess(HttpResult data) {
                        if (data.isSucess()) {
                            callback.onSuccess();
                        } else {
                            ToastUtil.showToast(data.getError().getMsg());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast("系统繁忙，请稍后重试");
                    }
                });
    }
}
