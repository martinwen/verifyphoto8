package com.shcy.yyzzj.module.pay;

import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;

/**
 * Created by licong on 2018/10/12.
 */

public class PayPresenter implements PayContract.Presenter {

    private PayContract.View view;
    private PayModel model;

    public PayPresenter(PayContract.View view) {
        this.view = view;
        model = new PayModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

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
    public void getOrderStatus(final int orderId, final String orderNumber,int type) {
        model.getPayStatus(orderId, orderNumber,type, new PayModel.OrderDetailCallback() {
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
}
