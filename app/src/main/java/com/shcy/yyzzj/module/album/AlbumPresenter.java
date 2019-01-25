package com.shcy.yyzzj.module.album;

import com.shcy.yyzzj.bean.album.AlbumListBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.utils.ToastUtil;

/**
 * Created by licong on 2018/9/29.
 */

public class AlbumPresenter implements AlbumContract.Presenter {

    private AlbumContract.View view;
    private AlbumModel model;
    public AlbumPresenter(AlbumContract.View view){
        this.view = view;
        view.setPresenter(this);
        this.model = new AlbumModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void getAlbumList(int pageNO) {
        model.getPhotoList(pageNO,new AlbumModel.Callback() {
            @Override
            public void onSuccess(HttpResult data) {
                if (data.isSucess()){
                    view.showAlbumData((AlbumListBean) data.getData());
                }else {
                    ToastUtil.showToast(data.getMessage(),true);
                }
            }

            @Override
            public void onFailed() {
                ToastUtil.showToast(Constants.NETERROR,true);
            }
        });
    }

    @Override
    public void deletePhoto(String photoId) {
        model.deletePhoto(photoId);
    }
}
