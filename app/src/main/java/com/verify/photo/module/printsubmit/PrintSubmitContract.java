package com.verify.photo.module.printsubmit;

import android.support.annotation.Nullable;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.AlertBean;
import com.verify.photo.bean.address.AddressListBean;
import com.verify.photo.bean.express.ExpressListBean;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.bean.pay.PrintOrderPrice;

/**
 * Created by licong on 2018/10/12.
 */

public class PrintSubmitContract {

    interface View extends BaseView<Presenter>{
        void submitSuccess(Order order);

        void submitFailed();

        void loading();

        void loadingEnd();

        void showExpressList(ExpressListBean expressListBean);

        void showAddressList(AddressListBean addressListBean);

        void showOrderPrice(PrintOrderPrice printOrderPrice);

        void prepaySuccess(PrePayInfoBean bean);

        void prepayFailed();

        void getOrderStuatusSuccess(Order order);

        void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum);

        void showAlert(AlertBean alertBean);
    }

    interface Presenter extends BasePresenter{
        void printSubmit(int type,String photoid,String addressId, String expressType, String printCount);

        void getExpressList();

        void getAddressList();

        void getOrderPrice(int type, String photoId, int expressType, int printCount);

        void prepay(String orderNumber, String payType);

        void getOrderStatus(int orderId, String orderNumber, int type);

        void getAlert();
    }
}
