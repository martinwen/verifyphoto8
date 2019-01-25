package com.shcy.yyzzj.module.help;

import com.shcy.yyzzj.bean.help.HelpListBean;

/**
 * Created by licong on 2018/12/17.
 */

public class HelpPresenter implements HelpContract.Presenter {

    private HelpContract.View view;
    private HelpModel model;

    public HelpPresenter(HelpContract.View view) {
        this.view = view;
        model = new HelpModel();
        view.setPresenter(this);
    }

    @Override
    public void getHelpData() {
        model.getHelopData(new HelpModel.GetHelpListCallback() {
            @Override
            public void onSuccess(HelpListBean helpListBean) {
                view.showHelpData(helpListBean);
            }
        });
    }

    @Override
    public void start() {

    }
}
