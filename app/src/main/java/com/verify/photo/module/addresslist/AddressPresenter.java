package com.verify.photo.module.addresslist;

import com.verify.photo.bean.address.AddressListBean;
import com.verify.photo.retrofit.callback.HttpResult;

/**
 * Created by licong on 2018/11/13.
 */

public class AddressPresenter implements AddressContract.Presenter {

    private AddressContract.View view;
    private AddressModel model;

    public AddressPresenter(AddressContract.View view){
        this.view = view;
        model = new AddressModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getAddressList() {
        model.getAddressList(new AddressModel.AddressCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.showAddressList((AddressListBean) result.getData());
            }

            @Override
            public void onFailed() {

            }
        });
    }
}
