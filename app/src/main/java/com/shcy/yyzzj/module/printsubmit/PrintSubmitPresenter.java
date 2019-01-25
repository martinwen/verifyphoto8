package com.shcy.yyzzj.module.printsubmit;

import android.text.TextUtils;

import com.shcy.yyzzj.bean.AlertBean;
import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.bean.express.ExpressListBean;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.PrintOrderPrice;
import com.shcy.yyzzj.module.addresslist.AddressModel;
import com.shcy.yyzzj.module.pay.PayModel;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.utils.ToastUtil;

/**
 * Created by licong on 2018/10/12.
 */

public class PrintSubmitPresenter implements PrintSubmitContract.Presenter {

    private PrintSubmitContract.View view;
    private PrintSubmitModel model;

    public PrintSubmitPresenter(PrintSubmitContract.View view) {
        this.view = view;
        model = new PrintSubmitModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void printSubmit(int type, String photoId, String addressId, String expressType, String printCount) {
        if (TextUtils.equals("0", addressId)) {
            ToastUtil.showToast("请添加收货信息");
            return;
        }
        view.loading();
        if (type == 1) {
            model.printSubmitByID(photoId, addressId, expressType, printCount, new PrintSubmitModel.PrintSubmitModelCallBack() {
                @Override
                public void onSuccess(HttpResult result) {
                    view.submitSuccess((Order) result.getData());
                    view.loadingEnd();
                }

                @Override
                public void onFailed() {
                    view.loadingEnd();
                    view.submitFailed();
                }
            });
        } else {
            model.printSubmitByNumber(photoId, addressId, expressType, printCount, new PrintSubmitModel.PrintSubmitModelCallBack() {
                @Override
                public void onSuccess(HttpResult result) {
                    view.loadingEnd();
                    view.submitSuccess((Order) result.getData());
                }

                @Override
                public void onFailed() {
                    view.loadingEnd();
                    view.submitFailed();
                }
            });
        }

    }

    @Override
    public void getExpressList() {
        model.getExpressList(new PrintSubmitModel.PrintSubmitModelCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                view.showExpressList((ExpressListBean) result.getData());
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void getAddressList() {
        view.loading();
        new AddressModel().getAddressList(new AddressModel.AddressCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.loadingEnd();
                view.showAddressList((AddressListBean) result.getData());
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }

    @Override
    public void getOrderPrice(int type, String photoId, int expressType, int printCount) {
        view.loading();
        if (type == 1) {
            model.getOrderPriceById(photoId, expressType, printCount, new PrintSubmitModel.PrintSubmitModelCallBack() {
                @Override
                public void onSuccess(HttpResult result) {
                    view.loadingEnd();
                    view.showOrderPrice((PrintOrderPrice) result.getData());
                }

                @Override
                public void onFailed() {
                    view.loadingEnd();
                }
            });
        } else {
            model.getOrderPriceByNumber(photoId, expressType, printCount, new PrintSubmitModel.PrintSubmitModelCallBack() {
                @Override
                public void onSuccess(HttpResult result) {
                    view.loadingEnd();
                    view.showOrderPrice((PrintOrderPrice) result.getData());
                }

                @Override
                public void onFailed() {
                    view.loadingEnd();
                }
            });
        }

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
                view.prepayFailed();
            }
        });
    }

    @Override
    public void getOrderStatus(final int orderId, final String orderNumber, int paytype) {
        new PayModel().getPayStatus(orderId, orderNumber, paytype, new PayModel.OrderDetailCallback() {
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

    @Override
    public void getAlert() {
        model.getAlert(new PrintSubmitModel.PrintSubmitModelCallBack() {
            @Override
            public void onSuccess(HttpResult result) {
                view.showAlert((AlertBean) result.getData());
            }

            @Override
            public void onFailed() {

            }
        });
    }
}
