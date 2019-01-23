package com.verify.photo.module.search;

import com.verify.photo.bean.preview.PreviewPhotoListBean;
import com.verify.photo.bean.size.SelectSizeListBean;
import com.verify.photo.config.Constants;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.LoadDataPostJsonObject;
import com.verify.photo.utils.ToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/12/13.
 */

public class SearchModel {

    interface SearchCallback{
        void onSuccess(HttpResult result);

        void onFailed(String s);
    }

    public void getSearchData(String keyword, int pageNNo,final SearchCallback callback){
        PhotoHttpManger.getPhotoApi().getSearchData(keyword,pageNNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<SelectSizeListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<SelectSizeListBean> data) {
                        if (data.isSucess()){
                            callback.onSuccess(data);
                        }else {
                            callback.onFailed(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed(Constants.NETERROR);
                    }
                });
    }

    public void getPreviewPhoto(String file, String specId, final SearchCallback callback){
        PhotoHttpManger.getPhotoApi().getPreviewPhoto(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("file","specId"),file,specId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PreviewPhotoListBean>() {
                    @Override
                    public void onSuccsess(HttpResult<PreviewPhotoListBean> data) {
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }

}
