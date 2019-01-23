package com.verify.photo.module.addresslist;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.address.AddressListBean;

/**
 * Created by licong on 2018/11/13.
 */

public class AddressContract {

    interface View extends BaseView<Presenter>{
        void showAddressList(AddressListBean addressListBean);
    }


    interface Presenter extends BasePresenter{
        void getAddressList();
    }
}
