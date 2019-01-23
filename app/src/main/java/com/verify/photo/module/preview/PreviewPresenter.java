package com.verify.photo.module.preview;

import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.module.pay.PayModel;
import com.verify.photo.utils.ToastUtil;

/**
 * Created by licong on 2018/10/10.
 */

public class PreviewPresenter implements PreviewContract.Presenter {

    private PreviewContract.View view;
    private PreviewModel model;

    public PreviewPresenter(PreviewContract.View view) {
        this.view = view;
        model = new PreviewModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void submit(String photoNumber) {
        view.loading();
        model.submit(photoNumber, new PreviewModel.SubmitCallback() {
            @Override
            public void submitSuccess(Order order) {
                view.loadingEnd();
                view.submitSuccess(order);
            }

            @Override
            public void submitFailed(String meg) {
                view.loadingEnd();
                ToastUtil.showToast(meg, true);
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
    public void getOrderStatus(final int orderId, final String orderNumber, int paytype) {
        new PayModel().getPayStatus(orderId, orderNumber,paytype,new PayModel.OrderDetailCallback() {
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
