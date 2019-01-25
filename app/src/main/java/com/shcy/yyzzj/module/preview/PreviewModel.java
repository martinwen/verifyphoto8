package com.shcy.yyzzj.module.preview;


import com.shcy.yyzzj.bean.order.Order;
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
