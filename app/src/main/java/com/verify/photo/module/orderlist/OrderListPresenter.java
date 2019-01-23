package com.verify.photo.module.orderlist;

import android.support.annotation.Nullable;

import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.order.OrderListBean;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.module.orderdetail.OrderDetailModel;
import com.verify.photo.module.orderdetail.OrderDetailPresenter;
import com.verify.photo.module.pay.PayModel;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.utils.ToastUtil;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListPresenter implements OrderListContract.Presenter {

    private OrderListContract.View view;
    private OrderListModel model;

    public OrderListPresenter(OrderListContract.View view) {
        this.view = view;
        model = new OrderListModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getOrderList(int pageNo) {
        model.getOrderList(pageNo, new OrderListModel.OrderListCallback() {
            @Override
            public void getOrderListSuccess(OrderListBean orderListBean) {
                view.showOrderList(orderListBean);
            }
        });
    }

    @Override
    public void prepay(String orderNumber, String payType) {
        view.loading();
        new PayModel().prepay(orderNumber, payType, new PayModel.PayCallback() {
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
    public void getOrderStatus(final int orderId, final String orderNumber, int type) {
        new PayModel().getPayStatus(orderId, orderNumber, type, new PayModel.OrderDetailCallback() {
            @Override
            public void onSuccess(Order order) {
                view.loadingEnd();
                view.getOrderStuatusSuccess(order);
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
                view.getOrderStuatusFailed(orderId, orderNumber);
            }
        });
    }

    @Override
    public void PrintOrderConfirm(final Order order) {
        view.loading();
        new OrderDetailModel().printOrderConfirm(order.getOrderNumber(), new OrderDetailModel.OrderDetailCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.loadingEnd();
                ToastUtil.showToast("确认收货成功",false);
                getOrderDetail(order.getId(), order.getOrderNumber());
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }

    @Override
    public void getOrderDetail(@Nullable int orderId, @Nullable String orderNumber) {
        view.loading();
        new PayModel().getOrderDetail(orderId, orderNumber, new PayModel.OrderDetailCallback() {
            @Override
            public void onSuccess(Order order) {
                view.showOrder(order);
                view.loadingEnd();
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }
}
