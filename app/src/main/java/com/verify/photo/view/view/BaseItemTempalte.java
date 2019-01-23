package com.verify.photo.view.view;

import com.verify.photo.bean.Base_Bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */

public abstract class BaseItemTempalte<T extends Base_Bean> {
   public  abstract int getViewId();
    public abstract void convert(ViewHolder holder,int position,List<T> list);
    public void viewRecycled(){}
    private boolean isDelete = false;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
