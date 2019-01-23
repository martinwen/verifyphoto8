package com.verify.photo.module.printpay;

import android.support.annotation.Nullable;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;

/**
 * Created by licong on 2018/12/11.
 */

public class PrintPayConstract {

    interface View extends BaseView<Presenter>{
        void prepaySuccess(PrePayInfoBean bean);

        void prepayFailed();

        void getOrderStuatusSuccess(Order order);

        void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum);

        void loading();

        void loadingEnd();
    }

    interface Presenter extends BasePresenter{
        void prepay(String orderNumber,String payType);

        void getOrderStatus(int orderId,String orderNumber,int type);
    }
}
