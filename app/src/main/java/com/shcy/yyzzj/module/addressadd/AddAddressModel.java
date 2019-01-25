package com.shcy.yyzzj.module.addressadd;

import com.shcy.yyzzj.bean.address.ProvinceBean;
import com.shcy.yyzzj.bean.login.ResultBean;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/12/8.
 */

public class AddAddressModel {

    interface AddAddressCallback {
        void onSuccess(HttpResult bean);

        void onFailed(String msg);
    }

    public void addAddress(int areaId, String detailedAddress, String recipientsName, String recipientsMobile, final AddAddressCallback callback) {
        PhotoHttpManger.getPhotoApi().SaveAddress(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("areaId", "detailedAddress", "recipientsName", "recipientsMobile"), areaId + "", detailedAddress, recipientsName, recipientsMobile))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        } else {
                            callback.onFailed(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed("网络不给力");
                    }
                });
    }

    public void changeAddress(int addressId, int areaId, String detailedAddress, String recipientsName, String recipientsMobile, final AddAddressCallback callback) {
        PhotoHttpManger.getPhotoApi().SaveAddress(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("addressId", "areaId", "detailedAddress", "recipientsName", "recipientsMobile"), addressId + "", areaId + "", detailedAddress, recipientsName, recipientsMobile))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        } else {
                            callback.onFailed(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed("网络不给力");
                    }
                });
    }

    public void getAreaData(final AddAddressCallback callback) {
        PhotoHttpManger.getPhotoApi().getAreaData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<List<ProvinceBean>>() {
                    @Override
                    public void onSuccsess(HttpResult<List<ProvinceBean>> data) {
                        if (data.isSucess()) {
                            callback.onSuccess(data);
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {

                    }
                });
    }

    public void deleteAddress(int addressId, final AddAddressCallback callback){
        PhotoHttpManger.getPhotoApi().deleteAddress(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("addressId"),addressId+""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data);
                        }else {
                            callback.onFailed(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed("网络不给力");
                    }
                });
    }
}
