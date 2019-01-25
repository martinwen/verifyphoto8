package com.shcy.yyzzj.module.album;

import com.shcy.yyzzj.base.BasePresenter;
import com.shcy.yyzzj.base.BaseView;
import com.shcy.yyzzj.bean.album.AlbumListBean;

/**
 * Created by licong on 2018/9/29.
 */

public class AlbumContract {

    interface View extends BaseView<Presenter>{
        void showAlbumData(AlbumListBean listBean);
    }

    interface Presenter extends BasePresenter{
        void getAlbumList(int pageNO);

        void deletePhoto(String photoId);
    }
}
