package com.verify.photo.module.help;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.help.HelpListBean;

/**
 * Created by licong on 2018/12/17.
 */

public class HelpContract {

    interface View extends BaseView<Presenter> {
        void showHelpData(HelpListBean helpListBean);
    }

    interface Presenter extends BasePresenter {
        void getHelpData();
    }
}
