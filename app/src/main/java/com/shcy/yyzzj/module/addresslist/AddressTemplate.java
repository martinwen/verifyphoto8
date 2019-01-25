package com.shcy.yyzzj.module.addresslist;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.address.AddressBean;
import com.shcy.yyzzj.view.view.BaseItemTempalte;
import com.shcy.yyzzj.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/11/13.
 */

public class AddressTemplate extends BaseItemTempalte {

    private View.OnClickListener onClickListener;

    public AddressTemplate(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getViewId() {
        return R.layout.template_address;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        AddressBean addressBean = (AddressBean) list.get(position);
        if (addressBean == null) {
            return;
        }
        TextView name, mobile, address;
        ImageView edit;
        RelativeLayout layout;
        name = holder.getView(R.id.template_address_name);
        mobile = holder.getView(R.id.template_address_mobile);
        address = holder.getView(R.id.template_address_address);
        edit = holder.getView(R.id.template_address_edit);
        layout = holder.getView(R.id.template_address_layout);
        if (!TextUtils.isEmpty(addressBean.getRecipientsName())) {
            name.setText(addressBean.getRecipientsName());
        }
        if (!TextUtils.isEmpty(addressBean.getRecipientsMobile())) {
            mobile.setText(addressBean.getRecipientsMobile());
        }
        if (!TextUtils.isEmpty(addressBean.getDetailedAddress())) {
            address.setText(addressBean.getProvince() + addressBean.getCity() + addressBean.getDistrict() + addressBean.getDetailedAddress());
        }
        if (onClickListener != null) {
            edit.setTag(position);
            edit.setOnClickListener(onClickListener);
            layout.setTag(position);
            layout.setOnClickListener(onClickListener);
        }
    }
}
