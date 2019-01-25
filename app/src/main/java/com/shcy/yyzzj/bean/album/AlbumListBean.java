package com.shcy.yyzzj.bean.album;

import com.shcy.yyzzj.bean.Base_Bean;

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
