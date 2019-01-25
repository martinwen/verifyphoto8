package com.shcy.yyzzj.module.camera;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;

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
