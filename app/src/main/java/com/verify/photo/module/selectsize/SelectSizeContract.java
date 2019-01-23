package com.verify.photo.module.selectsize;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.preview.PreviewPhotoListBean;
import com.verify.photo.bean.size.SelectSizeBean;

import java.util.List;

/**
 * Created by licong on 2018/10/8.
 */

public class SelectSizeContract {

    interface View extends BaseView<Presenter>{
        void showSizeList(List<SelectSizeBean> list);

        void showPreviewPhoto(PreviewPhotoListBean listBean);

        void getPreViewPhotoError(String message);

        void loading();

        void loadingEnd();
    }

    interface Presenter extends BasePresenter{
        void getSizeList();

        void getPreviewPhoto(String file,String specId);
    }
}
