package com.shcy.yyzzj.module.addressadd;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.address.ProvinceBean;
import com.shcy.yyzzj.bean.login.ResultBean;

import java.util.List;

/**
 * Created by licong on 2018/12/8.
 */

public class AddAddressContract {

    interface View extends BaseView<Presenter>{
        void loading();

        void loadingEnd();

        void saveAddressSuccess(ResultBean bean);

        void showAreaData(List<ProvinceBean> list);
    }



    interface Presenter extends BasePresenter{
        void addAddress(int areaId, String detailedAddress, String recipientsName, String recipientsMobile);

        void changeAddress(int addressId, int areaId, String detailedAddress, String recipientsName, String recipientsMobile);

        void getAreaData();

        void deleteAddress(int addressId);
    }
}
