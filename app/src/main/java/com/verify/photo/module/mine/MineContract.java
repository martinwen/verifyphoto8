package com.verify.photo.module.mine;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.share.ShareAppBean;

/**
 * Created by licong on 2018/10/19.
 */

public class MineContract {

    interface View extends BaseView<Presenter>{

        void loading();

        void loadingEnd();

        void showServerMessage(String msg);

        void shareApp(ShareAppBean shareAppBean);
    }

    interface Presenter extends BasePresenter{
        void getCustomServer();

        void getShareAppConfig();
    }
}
