package com.shcy.yyzzj.module.pay;

import android.annotation.SuppressLint;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.WechatPayParameter;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.module.orderdetail.OrderDetailActivity;
import com.shcy.yyzzj.module.orderlist.OrderListActivity;
import com.shcy.yyzzj.utils.CountDownUtils;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by licong on 2018/10/10.
 */

public class PayActivity extends Activity implements View.OnClickListener, PayContract.View {

    private static final String TAG = "支付";

    private static final int SDK_PAY_FLAG = 3;
    public static final String ORDER = "order";
    public static final String ORDER_STATUS = "order_status";
    private Order order;
    private TextView orderNum, amount, time, paySuccessAmout, paySuccessOrderNum, paySuccessTime;
    private ImageView back, payBtn, alipayCheckImage, wechatCheckImage, toMainpage, viewOrder;
    private LinearLayout orderLayout, paysuccessLayout, alipayLayout, wechatLayout, timeLayout;
    private SimpleDraweeView photo;

    private int payType = 2;//1-微信支付 2-支付宝支付

    private PayContract.Presenter presenter;
    private MyReceiver receiver;
    private LodingDialog lodingDialog;
    private boolean isPaySuccess = false;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        new PayPresenter(this);
        handler = new MyHandler(this);
        initView();
        initData();
    }

    @SuppressLint("HandlerLeak")
    private void initData() {
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));

        order = (Order) getIntent().getSerializableExtra(ORDER);
        FrescoUtils.getInstance().showImage(photo, order.getPhoto().getImage());
        setPayType(2);
        orderNum.setText("订单编号：" + order.getOrderNumber());
        amount.setText(order.getAmount());
        new CountDownUtils((order.getExpireUtc() - System.currentTimeMillis()) / 1000, handler);
        alipayLayout.setOnClickListener(this);
        wechatLayout.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        orderNum = findViewById(R.id.submit_order_num);
        back = findViewById(R.id.submit_back);
        amount = findViewById(R.id.submit_amount);
        time = findViewById(R.id.submit_time);
        payBtn = findViewById(R.id.submit_pay);
        alipayCheckImage = findViewById(R.id.submit_alipay_choice);
        wechatCheckImage = findViewById(R.id.submit_wechat_choice);
        orderLayout = findViewById(R.id.submit_order_layout);
        alipayLayout = findViewById(R.id.submit_alipay_layout);
        wechatLayout = findViewById(R.id.submit_wechat_layout);
        timeLayout = findViewById(R.id.submit_time_layout);
        photo = findViewById(R.id.printsubmit_photo);

        paysuccessLayout = findViewById(R.id.submit_paysuccess_layout);
        paySuccessAmout = findViewById(R.id.submit_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.submit_pay_success_order_num);
        paySuccessTime = findViewById(R.id.submit_pay_success_order_paytime);
        toMainpage = findViewById(R.id.submit_pay_success_tomianpage);
        viewOrder = findViewById(R.id.submit_pay_success_vieworder);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_back:
                if (!isPaySuccess) {
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra(ORDER, order);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.submit_alipay_layout:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PAY_ALIPAY);
                setPayType(2);
                break;
            case R.id.submit_wechat_layout:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PAY_WX);
                setPayType(1);
                break;
            case R.id.submit_pay:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PAY);
                presenter.prepay(order.getOrderNumber(), payType + "");
                break;
            case R.id.submit_pay_success_tomianpage:
                finish();
                break;
            case R.id.submit_pay_success_vieworder:
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(ORDER, order);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(PayContract.Presenter presenter) {
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
    public void getOrderStuatusSuccess(final Order order) {
        this.order = order;
        if (order.getStatus() == 20) {//支付成功
            isPaySuccess = true;
            paysuccessLayout.setVisibility(View.VISIBLE);
            orderLayout.setVisibility(View.GONE);
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
        DialogUtil.showPayFailedDialog(this, new PhotoDialog.OnDialogClickListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(PayActivity.this, OrderDetailActivity.class);
                intent.putExtra(ORDER, order);
                startActivity(intent);
                finish();
            }

            @Override
            public void cancel() {

            }
        });
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

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            handler.sendMessage(msg);
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<PayActivity> weakReference;

        public MyHandler(PayActivity payActivity) {
            weakReference = new WeakReference<PayActivity>(payActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final PayActivity payActivity = weakReference.get();
            if (payActivity != null) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        this.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                payActivity.presenter.getOrderStatus(payActivity.order.getId(), payActivity.order.getOrderNumber(), payActivity.payType);
                            }
                        }, 3000);
                        break;
                    case 0://倒计时结束
                        payActivity.timeLayout.setVisibility(View.GONE);
                        payActivity.time.setText("");
                        Intent intent = new Intent();
                        intent.setAction(OrderListActivity.PAYRECEIVER_ACTION);
                        intent.putExtra(PayActivity.ORDER_STATUS, 0);
                        payActivity.sendBroadcast(intent);
                        break;

                    case 1:
                        payActivity.timeLayout.setVisibility(View.VISIBLE);
                        payActivity.time.setText((String) msg.obj);
                        break;
                }
            }
        }
    }

    public void payV2(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
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

    /**
     * 监听按下事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!isPaySuccess) {
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(ORDER, order);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
        sendBroadcast(intent);
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_PAY_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
