package com.verify.photo.module.orderdetail;

import android.support.annotation.Nullable;

import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.module.pay.PayModel;
import com.verify.photo.retrofit.callback.HttpResult;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private OrderDetailContract.View view;
    private PayModel model;

    public OrderDetailPresenter(OrderDetailContract.View view) {
        this.view = view;
        model = new PayModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getOrderDetail(@Nullable int orderId, @Nullable String orderNumber) {
        view.loading();
        model.getOrderDetail(orderId, orderNumber, new PayModel.OrderDetailCallback() {
            @Override
            public void onSuccess(Order order) {
                view.showOrderDetail(order);
                view.loadingEnd();
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }

    @Override
    public void PrintOrderConfirm(String order) {
        new OrderDetailModel().printOrderConfirm(order, new OrderDetailModel.OrderDetailCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.confirmOrder((ResultBean) result.getData());
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void prepay(String orderNumber, String payType) {
        view.loading();
        model.prepay(orderNumber, payType, new PayModel.PayCallback() {
            @Override
            public void onSuccess(PrePayInfoBean bean) {
                view.prepaySuccess(bean);
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
                view.prepayFailed();
            }
        });
    }

    @Override
    public void getOrderStatus(int orderId, String orderNumber, int type) {
        model.getPayStatus(orderId, orderNumber,type, new PayModel.OrderDetailCallback() {
            @Override
            public void onSuccess(Order order) {
                view.loadingEnd();
                view.showPayStatus(order);
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }
}
