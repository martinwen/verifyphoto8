package com.verify.photo.module.preview;


import com.verify.photo.bean.order.Order;
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
 * Created by licong on 2018/10/10.
 */

public class PreviewModel {

    interface SubmitCallback{
        void submitSuccess(Order order);

        void submitFailed(String meg);
    }
    public void submit(String photoNumber, final SubmitCallback callback){
        PhotoHttpManger.getPhotoApi().submit(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("photoNumber"),photoNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Order>() {
                    @Override
                    public void onSuccsess(HttpResult<Order> data) {
                        if (data.isSucess()){
                            callback.submitSuccess(data.getData());
                        }else {
                            callback.submitFailed(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }
}
