package com.verify.photo.module.orderdetail;

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
 * Created by licong on 2018/12/7.
 */

public class OrderDetailModel {
    public interface OrderDetailCallback{
        void onSuccess(HttpResult result);

        void onFailed();
    }

    public void printOrderConfirm(String orderNumber, final OrderDetailCallback callback){
        PhotoHttpManger.getPhotoApi().PrintOrderConfirm(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("orderNumber"),orderNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data);
                        }else {
                            callback.onFailed();
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(e.getMessage());
                        callback.onFailed();
                    }
                });
    }
}
