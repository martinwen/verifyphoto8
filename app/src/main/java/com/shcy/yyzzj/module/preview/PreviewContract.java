package com.shcy.yyzzj.module.preview;

import android.support.annotation.Nullable;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;

/**
 * Created by licong on 2018/10/10.
 */

public class PreviewContract {

    interface View extends BaseView<Presenter>{
        void submitSuccess(Order order);

        void loading();

        void loadingEnd();

        void prepaySuccess(PrePayInfoBean bean);

        void prepayFailed();

        void getOrderStuatusSuccess(Order order);

        void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum);
    }

    interface Presenter extends BasePresenter{
        void submit(String photoNumber);

        void prepay(String orderNumber,String payType);

        void getOrderStatus(int orderId,String orderNumber,int paytype);
    }
}
