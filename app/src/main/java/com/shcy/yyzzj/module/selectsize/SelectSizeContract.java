package com.shcy.yyzzj.module.selectsize;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeBean;

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
