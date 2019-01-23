package com.verify.photo.module.album;

import com.verify.photo.base.BasePresenter;
import com.verify.photo.base.BaseView;
import com.verify.photo.bean.album.AlbumListBean;

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
