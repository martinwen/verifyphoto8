package com.shcy.yyzzj.module.login;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;

/**
 * Created by licong on 2018/10/11.
 */

public class LoginContract {

    interface View extends BaseView<Presenter>{
        void getVerifySuccess();

        void getVerifyFailed();

        void loginSuccess();

        void loginFailed();
    }

    interface Presenter extends BasePresenter{
        void getVerifyCode(String mobile);

        void login(String mobile,String verifyCode);
    }
}
