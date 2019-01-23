package com.verify.photo.module.preview;

import android.support.annotation.Nullable;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.bean.preview.PreviewPrintPhotoBean;

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
