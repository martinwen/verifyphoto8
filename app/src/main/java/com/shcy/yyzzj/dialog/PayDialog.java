package com.shcy.yyzzj.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.utils.CountDownUtils;

import java.lang.ref.WeakReference;

/**
 * Created by licong on 2018/12/6.
 */

public class PayDialog extends Dialog implements View.OnClickListener {

    private TextView expireTime, payButton;
    private ImageView alipay, wxpay;
    private LinearLayout alipaylayout, wxpaylayout, timelayout;
    private View topview;
    private PayLisener payLisener;
    private CountDownUtils countDownUtils;

    private Order order;
    private int paytype;

    public PayDialog(Activity context, Order order, PayLisener payLisener) {
        super(context, R.style.fn_fullsreen_dialog_tra);
        this.order = order;
        this.payLisener = payLisener;
        setContentView(R.layout.dialog_pay);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        initView();
        initData();
        show();
    }

    private void initView() {
        expireTime = findViewById(R.id.paydialog_deadline_text);
        payButton = findViewById(R.id.paydialog_pay_button);
        alipay = findViewById(R.id.paydialog_alipay_choice);
        wxpay = findViewById(R.id.paydialog_wechat_choice);
        alipaylayout = findViewById(R.id.paydialog_alipay_layout);
        wxpaylayout = findViewById(R.id.paydialog_wechat_layout);
        timelayout = findViewById(R.id.paydialog_deadline_layout);
        topview = findViewById(R.id.paydialog_close);
        alipaylayout.setOnClickListener(this);
        wxpaylayout.setOnClickListener(this);
        payButton.setOnClickListener(this);
        topview.setOnClickListener(this);
    }

    private void initData() {
        setPayType(2);
        countDownUtils = new CountDownUtils((order.getExpireUtc() - System.currentTimeMillis()) / 1000, new Myhandler(this));
    }

    private static class Myhandler extends Handler {

        WeakReference<PayDialog> weakReference;

        public Myhandler(PayDialog payDialog) {
            weakReference = new WeakReference<PayDialog>(payDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PayDialog payDialog = weakReference.get();
            if (payDialog != null) {
                switch (msg.what) {
                    case 0://倒计时结束
                        payDialog.timelayout.setVisibility(View.GONE);
                        payDialog.expireTime.setText("");
                        break;

                    case 1:
                        payDialog.timelayout.setVisibility(View.VISIBLE);
                        payDialog.expireTime.setText((String) msg.obj);
                        break;
                }
            }
        }
    }

    private void setPayType(int type) {
        paytype = type;
        if (paytype == 2) {
            alipay.setImageResource(R.mipmap.submit_paychoice);
            wxpay.setImageResource(R.mipmap.submit_pay_unchoice);
        } else {
            alipay.setImageResource(R.mipmap.submit_pay_unchoice);
            wxpay.setImageResource(R.mipmap.submit_paychoice);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paydialog_alipay_layout:
                setPayType(2);
                break;
            case R.id.paydialog_wechat_layout:
                setPayType(1);
                break;
            case R.id.paydialog_pay_button:
                if (payLisener != null) {
                    payLisener.pay(order.getOrderNumber(), paytype);
                }
                cancel();
                break;
            case R.id.paydialog_close:
                dismiss();
                break;
        }
    }

    public interface PayLisener {
        void pay(String ordernum, int paytype);
    }
}
