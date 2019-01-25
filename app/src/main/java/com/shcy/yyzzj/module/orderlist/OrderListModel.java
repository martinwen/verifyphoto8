package com.shcy.yyzzj.module.orderlist;

import com.shcy.yyzzj.bean.order.OrderListBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.ToastUtil;

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
