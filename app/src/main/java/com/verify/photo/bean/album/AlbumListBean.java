package com.verify.photo.bean.album;

import com.verify.photo.bean.Base_Bean;

import java.util.List;

/**
 * Created by licong on 2018/9/29.
 */

public class AlbumListBean extends Base_Bean {
    List<ALbumBean> data;

    public List<ALbumBean> getData() {
        return data;
    }

    public void setData(List<ALbumBean> data) {
        this.data = data;
    }
}
