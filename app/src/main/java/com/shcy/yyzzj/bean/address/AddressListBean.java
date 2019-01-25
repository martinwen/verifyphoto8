package com.shcy.yyzzj.bean.address;

import com.shcy.yyzzj.bean.Base_Bean;

import java.util.List;

/**
 * Created by licong on 2018/11/22.
 */

public class AddressListBean extends Base_Bean {
    private List<AddressBean> data;

    public List<AddressBean> getData() {
        return data;
    }

    public void setData(List<AddressBean> data) {
        this.data = data;
    }
}
