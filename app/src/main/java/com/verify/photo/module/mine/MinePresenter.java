package com.verify.photo.module.mine;

import android.text.TextUtils;

import com.verify.photo.bean.customserver.ServerMessageBean;
import com.verify.photo.bean.share.ShareAppBean;
import com.verify.photo.retrofit.callback.HttpResult;

/**
 * Created by licong on 2018/10/19.
 */

public class MinePresenter implements MineContract.Presenter {


    private MineContract.View view;
    private MineModel model;

    public MinePresenter(MineContract.View view) {
        this.view = view;
        this.model = new MineModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getCustomServer() {
        view.loading();
        model.getServerMsg(new MineModel.MineCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.loadingEnd();
                ServerMessageBean bean = (ServerMessageBean) result.getData();
                if (bean != null && !TextUtils.isEmpty(bean.getMsg()))
                    view.showServerMessage(bean.getMsg());
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }

    @Override
    public void getShareAppConfig() {
        view.loading();
        model.getShareAppConfig(new MineModel.MineCallback() {
            @Override
            public void onSuccess(HttpResult data) {
                view.loadingEnd();
                ShareAppBean bean = (ShareAppBean) data.getData();
                if (bean != null) {
                    view.shareApp(bean);
                }
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }
}
