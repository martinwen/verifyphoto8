package com.shcy.yyzzj.module.camera;

import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.retrofit.callback.HttpResult;

/**
 * Created by licong on 2018/10/9.
 */

public class CameraPresenter implements CameraContract.Presenter {

    private CameraContract.View view;
    private CameraModel model;

    public CameraPresenter(CameraContract.View view) {
        this.view = view;
        this.model = new CameraModel();
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getPreviewPhoto(String file, String specId) {
        view.loading();
        model.getPreviewPhoto(file, specId, new CameraModel.PhotographCallback() {
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

    @Override
    public void getPreviewStatus() {
        model.getPreviewStatus(new CameraModel.GetPreviewStatusCallback() {
            @Override
            public void onSuccess() {
                view.gotPreviewStatus();
            }
        });
    }
}
