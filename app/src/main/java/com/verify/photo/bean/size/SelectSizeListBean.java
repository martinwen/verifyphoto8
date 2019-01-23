package com.verify.photo.bean.size;


import com.verify.photo.bean.Base_Bean;

import java.util.List;

/**
 * Created by licong on 2018/10/8.
 */

public class SelectSizeListBean extends Base_Bean{
    List<SelectSizeBean> specList;

    List<SelectSizeBean> data;

    public List<SelectSizeBean> getSpecList() {
        return specList;
    }

    public void setSpecList(List<SelectSizeBean> specList) {
        this.specList = specList;
    }

    public List<SelectSizeBean> getData() {
        return data;
    }

    public void setData(List<SelectSizeBean> data) {
        this.data = data;
    }
}
