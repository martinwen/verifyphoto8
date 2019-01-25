package com.shcy.yyzzj.module.selectsize;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
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
