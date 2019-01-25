package com.shcy.yyzzj.module.pay;

import android.support.annotation.Nullable;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;

/**
 * Created by licong on 2018/10/12.
 */

public class PayContract {

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
