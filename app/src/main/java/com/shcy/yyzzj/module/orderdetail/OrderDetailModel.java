package com.shcy.yyzzj.module.orderdetail;

import com.shcy.yyzzj.bean.login.ResultBean;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;
import com.shcy.yyzzj.utils.ToastUtil;

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
