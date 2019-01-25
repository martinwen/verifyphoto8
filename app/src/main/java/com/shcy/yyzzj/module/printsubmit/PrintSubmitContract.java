package com.shcy.yyzzj.module.printsubmit;

import android.support.annotation.Nullable;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.AlertBean;
import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.bean.express.ExpressListBean;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.PrintOrderPrice;

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
