package com.shcy.yyzzj.module.login;

/**
 * Created by licong on 2018/10/11.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private LoginModel model;
    public LoginPresenter(LoginContract.View view){
        this.view = view;
        model = new LoginModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getVerifyCode(String mobile) {
        model.getVerifyCode(mobile, new LoginModel.LoginCallBack() {
            @Override
            public void onSuccess() {
                view.getVerifySuccess();
            }

            @Override
            public void onFailed() {
                view.getVerifyFailed();
            }
        });
    }

    @Override
    public void login(String mobile, String verifyCode) {
        model.login(mobile, verifyCode, new LoginModel.LoginCallBack() {
            @Override
            public void onSuccess() {
                view.loginSuccess();
            }

            @Override
            public void onFailed() {
                view.loginFailed();
            }
        });
    }
}
