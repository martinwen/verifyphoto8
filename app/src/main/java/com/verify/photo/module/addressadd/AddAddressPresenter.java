package com.verify.photo.module.addressadd;

import com.verify.photo.bean.address.ProvinceBean;
import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.utils.ToastUtil;

import java.util.List;

/**
 * Created by licong on 2018/12/8.
 */

public class AddAddressPresenter implements AddAddressContract.Presenter {

    private AddAddressModel model;
    private AddAddressContract.View view;

    public AddAddressPresenter(AddAddressContract.View view) {
        this.view = view;
        this.model = new AddAddressModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void addAddress(int areaId, String detailedAddress, String recipientsName, String recipientsMobile) {
        view.loading();
        model.addAddress(areaId, detailedAddress, recipientsName, recipientsMobile, new AddAddressModel.AddAddressCallback() {
            @Override
            public void onSuccess(HttpResult bean) {
                view.loadingEnd();
                view.saveAddressSuccess((ResultBean) bean.getData());
            }

            @Override
            public void onFailed(String msg) {
                view.loadingEnd();
                ToastUtil.showToast(msg);
            }
        });
    }

    @Override
    public void changeAddress(int addressId, int areaId, String detailedAddress, String recipientsName, String recipientsMobile) {
        view.loading();
        model.changeAddress(addressId, areaId, detailedAddress, recipientsName, recipientsMobile, new AddAddressModel.AddAddressCallback() {
            @Override
            public void onSuccess(HttpResult bean) {
                view.loadingEnd();
                view.saveAddressSuccess((ResultBean) bean.getData());
            }

            @Override
            public void onFailed(String msg) {
                view.loadingEnd();
                ToastUtil.showToast(msg);
            }
        });
    }

    @Override
    public void getAreaData() {
        model.getAreaData(new AddAddressModel.AddAddressCallback() {
            @Override
            public void onSuccess(HttpResult bean) {
                if (bean.getData() != null) {
                    view.showAreaData((List<ProvinceBean>) bean.getData());
                }
            }

            @Override
            public void onFailed(String msg) {
            }
        });
    }

    @Override
    public void deleteAddress(int addressId) {
        view.loading();
        model.deleteAddress(addressId, new AddAddressModel.AddAddressCallback() {
            @Override
            public void onSuccess(HttpResult bean) {
                view.loadingEnd();
                view.saveAddressSuccess((ResultBean) bean.getData());
            }

            @Override
            public void onFailed(String msg) {
                view.loadingEnd();
                ToastUtil.showToast(msg);
            }
        });
    }
}
