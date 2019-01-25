package com.shcy.yyzzj.module.addresslist;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.address.AddressListBean;

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
