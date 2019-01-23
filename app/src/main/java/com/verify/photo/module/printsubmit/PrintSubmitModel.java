package com.verify.photo.module.printsubmit;

import com.verify.photo.bean.AlertBean;
import com.verify.photo.bean.express.ExpressListBean;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrintOrderPrice;
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

public class PrintSubmitModel {

    public interface PrintSubmitModelCallBack {
        void onSuccess(HttpResult result);

        void onFailed();
    }

    public void printSubmitByID(String photoId, String addressId, String expressType, String printCount, final PrintSubmitModelCallBack callback) {
        PhotoHttpManger.getPhotoApi().printSubmit(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("photoId", "addressId", "expressType", "printCount"), photoId, addressId, expressType, printCount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Order>() {
                    @Override
                    public void onSuccsess(HttpResult<Order> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        } else {
                            callback.onFailed();
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }

    public void printSubmitByNumber(String photoNumber, String addressId, String expressType, String printCount, final PrintSubmitModelCallBack callback) {
        PhotoHttpManger.getPhotoApi().printSubmit(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("photoNumber", "addressId", "expressType", "printCount"), photoNumber, addressId, expressType, printCount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<Order>() {
                    @Override
                    public void onSuccsess(HttpResult<Order> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        } else {
                            callback.onFailed();
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }

    public void getExpressList(final PrintSubmitModelCallBack callback) {
        PhotoHttpManger.getPhotoApi().getExpressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ExpressListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ExpressListBean> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {

                    }
                });
    }

    public void getOrderPriceById(String photoId, int expressType, int printCount, final PrintSubmitModelCallBack callBack) {
        PhotoHttpManger.getPhotoApi().getPrintOrderPriceById(photoId, expressType, printCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PrintOrderPrice>() {
                    @Override
                    public void onSuccsess(HttpResult<PrintOrderPrice> data) {
                        if (data.isSucess()) {
                            callBack.onSuccess(data);
                        } else {
                            callBack.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callBack.onFailed();
                    }
                });
    }

    public void getOrderPriceByNumber(String photoNumber, int expressType, int printCount, final PrintSubmitModelCallBack callBack) {
        PhotoHttpManger.getPhotoApi().getPrintOrderPriceByNumber(photoNumber, expressType, printCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PrintOrderPrice>() {
                    @Override
                    public void onSuccsess(HttpResult<PrintOrderPrice> data) {
                        if (data.isSucess()) {
                            callBack.onSuccess(data);
                        } else {
                            callBack.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callBack.onFailed();
                    }
                });
    }

    public void getAlert(final PrintSubmitModelCallBack callBack) {
        PhotoHttpManger.getPhotoApi().getAlert()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<AlertBean>() {
                    @Override
                    public void onSuccsess(HttpResult data) {
                        if (data.isSucess()) {
                            callBack.onSuccess(data);
                        } else {
                            callBack.onFailed();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callBack.onFailed();
                    }
                });
    }
}
