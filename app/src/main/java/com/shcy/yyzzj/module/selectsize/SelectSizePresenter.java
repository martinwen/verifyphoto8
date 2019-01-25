package com.shcy.yyzzj.module.selectsize;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
import com.shcy.yyzzj.retrofit.callback.HttpResult;

/**
 * Created by licong on 2018/10/9.
 */

public class SelectSizePresenter implements SelectSizeContract.Presenter {

    SelectSizeContract.View view;
    SelectSizeModel model;

    public SelectSizePresenter(SelectSizeContract.View view) {
        this.view = view;
        model = new SelectSizeModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getSizeList() {
        model.getSpecList(new SelectSizeModel.SelectSizeCallback() {
            @Override
            public void onSuccess(SelectSizeListBean bean) {
                if (bean.getSpecList() != null && bean.getSpecList().size() != 0) {
                    view.showSizeList(bean.getSpecList());
                }
            }
        });
    }

    @Override
    public void getPreviewPhoto(String file, String specId) {
        view.loading();
        model.getPreviewPhoto(file, specId, new SelectSizeModel.Callback() {
            @Override
            public void onResult(HttpResult<PreviewPhotoListBean> result) {
                view.loadingEnd();
                if (result.isSucess()) {
                    view.showPreviewPhoto(result.getData());
                } else {
                    view.getPreViewPhotoError(result.getMessage());
                }
            }

            @Override
            public void onFailed() {
                view.loadingEnd();
            }
        });
    }
}
