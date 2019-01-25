package com.shcy.yyzzj.module.search;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;

/**
 * Created by licong on 2018/12/13.
 */

public class SearchContract {

    interface View extends BaseView<Presenter> {

        void showSearchData(SelectSizeListBean selectSizeListBean);

        void showPreviewPhoto(PreviewPhotoListBean listBean);

        void getPreViewPhotoError(String message);

        void getSearchDataFailed();

        void loading();

        void loadingEnd();
    }

    interface Presenter extends BasePresenter {

        void getSearchData(String keyword,int pageNo);



        void getPreviewPhoto(String file,String specId);
    }
}
