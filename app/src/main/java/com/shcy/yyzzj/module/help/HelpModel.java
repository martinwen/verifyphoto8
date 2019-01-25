package com.shcy.yyzzj.module.help;

import com.shcy.yyzzj.bean.help.HelpListBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/12/17.
 */

public class HelpModel {

    interface GetHelpListCallback {
        void onSuccess(HelpListBean helpListBean);
    }

    public void getHelopData(final GetHelpListCallback callback) {
        PhotoHttpManger.getPhotoApi().getHelpData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<HelpListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<HelpListBean> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data.getData());
                        } else {
                            ToastUtil.showToast(Constants.NETERROR);
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }
}
