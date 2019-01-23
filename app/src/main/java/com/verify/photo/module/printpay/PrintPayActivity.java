package com.verify.photo.module.printpay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.verify.photo.R;
import com.verify.photo.base.BaseActivity;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.bean.pay.WechatPayParameter;
import com.verify.photo.config.Constants;
import com.verify.photo.dialog.LodingDialog;
import com.verify.photo.module.orderdetail.OrderDetailActivity;
import com.verify.photo.utils.CountDownUtils;
import com.verify.photo.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.Map;

import static com.verify.photo.module.printsubmit.PrintSubmitActivity.ORDER;

/**
 * Created by licong on 2018/12/11.
 */

public class PrintPayActivity extends Activity implements View.OnClickListener, PrintPayConstract.View {

    private static final int SDK_PAY_FLAG = 3;

    private LinearLayout payLayout, paySuccessLayout;
    private ImageView alipayCheckImage, wechatCheckImage, back, viewOrder, toMainpage;
    private TextView time, payButton, paySuccessAmout, paySuccessOrderNum, paySuccessTime, amount;
    private LodingDialog lodingDialog;
    private MyReceiver receiver;
    private Handler handler;
    private int payType = 2;//1-微信支付 2-支付宝支付
    private Order order;
    private PrintPayConstract.Presenter presenter;
    private boolean paySuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printpay);
        handler = new Myhandler(this);
        new PrintPayPresenter(this);
        initView();
        initData();
    }

    private void initData() {
        order = (Order) getIntent().getSerializableExtra(ORDER);
        if (order != null) {
            payButton.setOnClickListener(this);
            new CountDownUtils((order.getExpireUtc() - System.currentTimeMillis()) / 1000, handler);
            amount.setText(order.getAmount());
        }
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));
        setPayType(2);
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        alipayCheckImage = findViewById(R.id.printpay_alipay_choice);
        wechatCheckImage = findViewById(R.id.printpay_wechat_choice);
        time = findViewById(R.id.printpay_time_text);
        payButton = findViewById(R.id.printpay_pay_button);
        payLayout = findViewById(R.id.printpay_pay_layout);
        paySuccessLayout = findViewById(R.id.printpay_paysuccess_layout);
        paySuccessAmout = findViewById(R.id.printpay_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.printpay_pay_success_order_num);
        paySuccessTime = findViewById(R.id.printpay_pay_success_order_paytime);
        amount = findViewById(R.id.printpay_amount);
        back = findViewById(R.id.printpay_back);
        toMainpage = findViewById(R.id.printpay_pay_success_tomianpage);
        viewOrder = findViewById(R.id.printpay_pay_success_vieworder);
        alipayCheckImage.setOnClickListener(this);
        wechatCheckImage.setOnClickListener(this);
        payButton.setOnClickListener(this);
        back.setOnClickListener(this);
        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.printpay_back:
                finishToOrderDetail();
                break;
            case R.id.printpay_alipay_choice:
                setPayType(2);
                break;
            case R.id.printpay_wechat_choice:
                setPayType(1);
                break;
            case R.id.printpay_pay_button:
                presenter.prepay(order.getOrderNumber(), payType + "");
                break;
            case R.id.printpay_pay_success_tomianpage:
                finish();
                break;
            case R.id.printpay_pay_success_vieworder:
                finish();
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(ORDER, order);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setPresenter(PrintPayConstract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void prepaySuccess(PrePayInfoBean prePayInfoBean) {
        if (payType == 1) {
            WechatPayParameter wechatPayParameter = prePayInfoBean.getWeixinParameter();
            final IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, wechatPayParameter.getAppid());
            iwxapi.registerApp(wechatPayParameter.getAppid());
            PayReq request = new PayReq();
            request.appId = wechatPayParameter.getAppid();
            request.partnerId = wechatPayParameter.getPartnerid();
            request.prepayId = wechatPayParameter.getPrepayid();
            request.packageValue = wechatPayParameter.getPackage1();
            request.nonceStr = wechatPayParameter.getNoncestr();
            request.timeStamp = wechatPayParameter.getTimestamp();
            request.sign = wechatPayParameter.getSign();
            iwxapi.sendReq(request);
        } else {
            payV2(prePayInfoBean.getAlipayParameter());
        }
    }

    @Override
    public void prepayFailed() {

    }

    @Override
    public void getOrderStuatusSuccess(Order order) {
        this.order = order;
        if (order.getStatus() == 20) {//支付成功
            paySuccess = true;
            paySuccessLayout.setVisibility(View.VISIBLE);
            payLayout.setVisibility(View.GONE);
            paySuccessAmout.setText("实付：" + order.getAmount() + "元");
            paySuccessOrderNum.setText("订单编号：" + order.getOrderNumber());
            paySuccessTime.setText("支付时间：" + order.getPayTime());
            MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
        } else {
            ToastUtil.showToast("支付失败", true);
        }
    }

    @Override
    public void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum) {

    }

    @Override
    public void loading() {
        if (lodingDialog != null && !lodingDialog.isShowing()) {
            lodingDialog.show();
        }
    }

    @Override
    public void loadingEnd() {
        if (lodingDialog != null && lodingDialog.isShowing()) {
            lodingDialog.dismiss();
        }
    }

    public void payV2(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PrintPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            handler.sendMessage(msg);
        }
    }

    private static class Myhandler extends Handler {
        WeakReference<PrintPayActivity> weakReference;

        Myhandler(PrintPayActivity printPayActivity) {
            weakReference = new WeakReference<PrintPayActivity>(printPayActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final PrintPayActivity printPayActivity = weakReference.get();
            if (printPayActivity != null) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        this.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                printPayActivity.presenter.getOrderStatus(printPayActivity.order.getId(), printPayActivity.order.getOrderNumber(),printPayActivity.payType);
                            }
                        }, 3000);
                        break;
                    case 0://倒计时结束
                        printPayActivity.time.setText("");
                        break;
                    case 1:
                        printPayActivity.time.setText((String) msg.obj);
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        Intent intent = new Intent();
        intent.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
        sendBroadcast(intent);
    }

    private void setPayType(int type) {
        payType = type;
        if (payType == 2) {
            alipayCheckImage.setImageResource(R.mipmap.submit_paychoice);
            wechatCheckImage.setImageResource(R.mipmap.submit_pay_unchoice);
        } else {
            alipayCheckImage.setImageResource(R.mipmap.submit_pay_unchoice);
            wechatCheckImage.setImageResource(R.mipmap.submit_paychoice);
        }
    }

    /**
     * 监听按下事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finishToOrderDetail();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回处理
     */
    private void finishToOrderDetail() {
        if (!paySuccess) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(ORDER, order);
            startActivity(intent);
        }
        finish();
    }
}
