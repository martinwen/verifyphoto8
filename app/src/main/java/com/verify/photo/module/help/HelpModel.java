package com.verify.photo.module.help;

import com.verify.photo.bean.help.HelpListBean;
import com.verify.photo.config.Constants;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.ToastUtil;

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
