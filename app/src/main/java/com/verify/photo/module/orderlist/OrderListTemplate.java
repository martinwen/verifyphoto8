package com.verify.photo.module.orderlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.verify.photo.R;
import com.verify.photo.bean.order.Order;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.utils.fresco.FrescoUtils;
import com.verify.photo.view.view.BaseItemTempalte;
import com.verify.photo.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListTemplate extends BaseItemTempalte {
    private Context context;
    private View.OnClickListener clickListener;

    public OrderListTemplate(Context context, View.OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public int getViewId() {
        return R.layout.template_order_list;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        final TextView orderNum, status, photoName, amount, orderType;
        SimpleDraweeView photo;
        final TextView button;
        final LinearLayout buttonLayout;
        orderNum = holder.getView(R.id.template_orderlist_ordernum);
        status = holder.getView(R.id.template_orderlist_status);
        photoName = holder.getView(R.id.template_orderlist_photoname);
        amount = holder.getView(R.id.template_orderlist_amount);
        photo = holder.getView(R.id.template_orderlist_photo);
        button = holder.getView(R.id.template_orderlist_button);
        buttonLayout = holder.getView(R.id.template_orderlist_pay_layout);
        orderType = holder.getView(R.id.template_orderlist_tag);

        final Order order = (Order) list.get(position);

        if (!TextUtils.isEmpty(order.getOrderNumber())) {
            orderNum.setText("订单编号：" + order.getOrderNumber());
        }

        if (order.getPhoto() != null && !TextUtils.isEmpty(order.getPhoto().getImage())) {
            FrescoUtils.getInstance().showImage(photo, order.getPhoto().getImage());
        }

        if (order.getSpec() != null && !TextUtils.isEmpty(order.getSpec().getName())) {
            photoName.setText(order.getSpec().getName());
        }

        if (order.getType() == 1) {
            orderType.setText("电子照订单");
        } else if (order.getType() == 2) {
            orderType.setText("冲印订单");
        }

        String amountStr = "¥" + order.getAmount();
        amount.setText(amountStr);

        switch (order.getStatus()) {////0—订单关闭 10-待支付 20-支付成功  21—待发货   22—已发货   24—已完成 30-已退款
            case 0://订单关闭
                buttonLayout.setVisibility(View.GONE);
                status.setText("已关闭");
                status.setTextColor(context.getResources().getColor(R.color.order_status_close));
                break;
            case 10://待支付
                buttonLayout.setVisibility(View.VISIBLE);
                status.setText("待支付");
                status.setTextColor(context.getResources().getColor(R.color.order_status));
                button.setText("去支付");
                break;
            case 20://支付成功
                status.setText("支付成功");
                if (order.getType() == 1){
                    buttonLayout.setVisibility(View.VISIBLE);
                    button.setText("下载照片");
                }else {
                    buttonLayout.setVisibility(View.GONE);
                }
                status.setTextColor(context.getResources().getColor(R.color.order_status));
                break;
            case 21://待发货
                buttonLayout.setVisibility(View.GONE);
                status.setText("待发货");
                status.setTextColor(context.getResources().getColor(R.color.order_status));
                break;
            case 22://已发货
                buttonLayout.setVisibility(View.GONE);
                status.setText("已发货");
                status.setTextColor(context.getResources().getColor(R.color.order_status));
                buttonLayout.setVisibility(View.VISIBLE);
                button.setText("确认收货");
                break;
            case 24://已完成
                status.setText("已完成");
                status.setTextColor(context.getResources().getColor(R.color.order_status));
                if (order.getType() == 1){
                    buttonLayout.setVisibility(View.VISIBLE);
                }else {
                    buttonLayout.setVisibility(View.GONE);
                }
                break;
            case 30://已退款
                buttonLayout.setVisibility(View.GONE);
                status.setText("已退款");
                status.setTextColor(context.getResources().getColor(R.color.order_status_close));
                break;
        }

        if (order.getStatus() == 20) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicUtil.downloadImage(order.getPhoto().getImage(), order.getId() + "", context);
                    ToastUtil.showToast("下载成功，请前往系统相册查看", false);
                }
            });
        } else {
            button.setTag(position);
            button.setOnClickListener(clickListener);
        }
    }
}
