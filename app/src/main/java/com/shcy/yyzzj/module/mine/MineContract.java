package com.shcy.yyzzj.module.mine;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.share.ShareAppBean;

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
