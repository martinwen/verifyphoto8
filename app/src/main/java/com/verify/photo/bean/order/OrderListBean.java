package com.verify.photo.bean.order;

import com.verify.photo.bean.Base_Bean;

import java.util.List;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListBean extends Base_Bean{
    List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
