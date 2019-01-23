package com.verify.photo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.verify.photo.R;


public class PayDialogPreview extends Dialog implements View.OnClickListener {

    private TextView expireTime, payButton, tip;
    private ImageView alipay, wxpay, close;
    private LinearLayout alipaylayout, wxpaylayout, timelayout;
    private PayLisener payLisener;

    private int paytype;

    public PayDialogPreview(Activity context, PayLisener payLisener) {
        super(context, R.style.fn_fullsreen_dialog_tra);
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
        close = findViewById(R.id.paydialog_close);
        tip = findViewById(R.id.deadline_tip);
        tip.setVisibility(View.GONE);

        alipaylayout.setOnClickListener(this);
        wxpaylayout.setOnClickListener(this);
        payButton.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    private void initData() {
        setPayType(2);
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
                    payLisener.pay(paytype);
                }
                cancel();
                break;
            case R.id.paydialog_close:
                dismiss();
                break;
        }
    }

    public interface PayLisener {
        void pay(int paytype);
    }
}
