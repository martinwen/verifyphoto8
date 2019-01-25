package com.shcy.yyzzj.module.help;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.help.HelpListBean;

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
