package com.verify.photo.module.orderlist;

import android.support.annotation.Nullable;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.order.OrderListBean;
import com.verify.photo.bean.pay.PrePayInfoBean;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListContract {

    interface View extends BaseView<Presenter>{
        void showOrderList(OrderListBean orderListBean);

        void prepaySuccess(PrePayInfoBean bean);

        void prepayFailed();

        void getOrderStuatusSuccess(Order order);

        void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum);

        void loading();

        void loadingEnd();

        void showOrder(Order order);
    }

    interface Presenter extends BasePresenter{
        void getOrderList(int pageNo);

        void prepay(String orderNumber,String payType);

        void getOrderStatus(int orderId,String orderNumber,int type);

        void PrintOrderConfirm(Order order);

        void getOrderDetail(@Nullable int orderId, @Nullable String orderNumber);
    }
}
