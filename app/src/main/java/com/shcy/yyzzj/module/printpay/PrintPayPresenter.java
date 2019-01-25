package com.shcy.yyzzj.module.printpay;

import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.module.pay.PayModel;

/**
 * Created by licong on 2018/12/11.
 */

public class PrintPayPresenter implements PrintPayConstract.Presenter {

    private PrintPayConstract.View view;
    private PayModel model;

    public PrintPayPresenter(PrintPayConstract.View view) {
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
                view.prepayFailed();
            }
        });
    }

    @Override
    public void getOrderStatus(final int orderId, final String orderNumber,int paytype) {
        model.getPayStatus(orderId, orderNumber, paytype,new PayModel.OrderDetailCallback() {
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
