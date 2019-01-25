package com.shcy.yyzzj.module.search;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;
import com.shcy.yyzzj.utils.ToastUtil;

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
