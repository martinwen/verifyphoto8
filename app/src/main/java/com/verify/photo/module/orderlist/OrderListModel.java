package com.verify.photo.module.orderlist;

import com.verify.photo.bean.order.OrderListBean;
import com.verify.photo.config.Constants;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListModel {

    interface OrderListCallback{
        void getOrderListSuccess(OrderListBean orderListBean);
    }

    public void getOrderList(int pageNo,final OrderListCallback callback){
        PhotoHttpManger.getPhotoApi().orderList(pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<OrderListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<OrderListBean> data) {
                        if (data.isSucess()){
                            callback.getOrderListSuccess(data.getData());
                        }else {

                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR,true);
                    }
                });
    }
}
