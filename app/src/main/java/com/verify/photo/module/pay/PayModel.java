package com.verify.photo.module.pay;

import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
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
 * Created by licong on 2018/10/12.
 */

public class PayModel {
    public interface PayCallback{
        void onSuccess(PrePayInfoBean bean);

        void onFailed();
    }
    public interface OrderDetailCallback{
        void onSuccess(Order order);

        void onFailed();
    }

    public void prepay(String orderNumber, String payType, final PayCallback callback){
        PhotoHttpManger.getPhotoApi().prepay(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("orderNumber","payType"),orderNumber,payType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PrePayInfoBean>() {
                    @Override
                    public void onSuccsess(HttpResult<PrePayInfoBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data.getData());
                        }else {
                            callback.onFailed();
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                        ToastUtil.showToast(Constants.NETERROR,true);
                    }
                });
    }

    public void getOrderDetail(int orderId, String orderNumber, final OrderDetailCallback callback){
        PhotoHttpManger.getPhotoApi().orderDetail(orderId,orderNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Order>() {
                    @Override
                    public void onSuccsess(HttpResult<Order> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data.getData());
                        }else {
                            callback.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR,true);
                        callback.onFailed();
                    }
                });
    }

    public void getPayStatus(int orderId, String orderNumber, int paytype, final OrderDetailCallback callback) {
        PhotoHttpManger.getPhotoApi().payStatus(orderId, orderNumber, paytype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Order>() {
                    @Override
                    public void onSuccsess(HttpResult<Order> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data.getData());
                        } else {
                            callback.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR, true);
                        callback.onFailed();
                    }
                });
    }
}
