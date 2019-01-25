package com.shcy.yyzzj.module.search;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.utils.ToastUtil;

/**
 * Created by licong on 2018/12/13.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private SearchModel model;

    public SearchPresenter(SearchContract.View view){
        this.view = view;
        model = new SearchModel();
        view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void getSearchData(String keyword, int pageNo) {
        model.getSearchData(keyword, pageNo, new SearchModel.SearchCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.showSearchData((SelectSizeListBean) result.getData());
            }

            @Override
            public void onFailed(String s) {
                ToastUtil.showToast(s);
            }
        });
    }

    @Override
    public void getPreviewPhoto(String file, String specId) {
        view.loading();
        model.getPreviewPhoto(file, specId, new SearchModel.SearchCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                view.loadingEnd();
                if (result.isSucess()) {
                    view.showPreviewPhoto((PreviewPhotoListBean) result.getData());
                } else {
                    view.getPreViewPhotoError(result.getMessage());
                }
            }

            @Override
            public void onFailed(String s) {
                view.loadingEnd();
            }
        });
    }
}
