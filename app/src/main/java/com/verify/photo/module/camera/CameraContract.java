package com.verify.photo.module.camera;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.preview.PreviewPhotoListBean;

/**
 * Created by licong on 2018/10/9.
 */

public class CameraContract {

    interface View extends BaseView<Presenter>{
        void showPreviewPhoto(PreviewPhotoListBean listBean);

        void getPreViewPhotoError(String message);

        void gotPreviewStatus();

        void loading();

        void loadingEnd();
    }

    public interface Presenter extends BasePresenter{
        void getPreviewPhoto(String file,String specId);

        void getPreviewStatus();
    }
}
