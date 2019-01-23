package com.verify.photo.module.preview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.verify.photo.R;
import com.verify.photo.base.BaseActivity;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.pay.PayResult;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.bean.pay.PrintPayBean;
import com.verify.photo.bean.pay.WechatPayParameter;
import com.verify.photo.bean.preview.PreviewPhotoBean;
import com.verify.photo.bean.preview.PreviewPrintPhotoBean;
import com.verify.photo.config.Constants;
import com.verify.photo.dialog.LodingDialog;
import com.verify.photo.dialog.PayDialogPreview;
import com.verify.photo.dialog.PhotoDialog;
import com.verify.photo.module.editphoto.EditPhotoActivity;
import com.verify.photo.module.orderdetail.OrderDetailActivity;
import com.verify.photo.module.pay.PayActivity;
import com.verify.photo.module.printsubmit.PrintSubmitActivity;
import com.verify.photo.utils.DialogUtil;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.utils.fresco.FrescoUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import static com.verify.photo.module.pay.PayActivity.ORDER;


/**
 * Created by licong on 2018/10/10.
 */

public class PreviewActivity extends BaseActivity implements View.OnClickListener, PreviewContract.View {

    private static final String TAG = "预览图片";

    private static final int SDK_PAY_FLAG = 1;
    private PreviewPhotoBean bean;
    private Order order;
    private PreviewPrintPhotoBean printBean;
    private SimpleDraweeView photoa, printPhoto;
    private ImageView back;
    private LinearLayout preview_pay, preview_print, paysuccessLayout, previewLayout;
    private TextView amount1, amount2, paySuccessAmout, paySuccessOrderNum, paySuccessTime;
    private ImageView toMainpage, viewOrder;

    private PreviewContract.Presenter presenter;
    private MyReceiver receiver;
    private LodingDialog lodingDialog;
    private Handler handler;
    private int payType = 2;
    private boolean isPaySuccess = false;
    private boolean isSubmitSuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        handler = new MyHandler(this);
        new PreviewPresenter(this);
        initView();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        photoa = findViewById(R.id.preview_photo);
        printPhoto = findViewById(R.id.preview_photo_print);
        back = findViewById(R.id.preview_back);
        preview_pay = findViewById(R.id.preview_pay);
        preview_print = findViewById(R.id.preview_button_print);
        amount1 = findViewById(R.id.preview_amount1);
        amount2 = findViewById(R.id.preview_amount2);

        bean = (PreviewPhotoBean) getIntent().getSerializableExtra(EditPhotoActivity.PREVIEWPHOTOBEAN);
        printBean = (PreviewPrintPhotoBean) getIntent().getSerializableExtra(EditPhotoActivity.PREVIEW_PRINT_PHOTO_BEAM);

        if (printBean != null) {
            FrescoUtils.getInstance().showImage(printPhoto, printBean.getPrintPhotoUrl());
            amount2.setText("￥" + printBean.getPrintPrice());
        }
        if (bean != null) {
            FrescoUtils.getInstance().showImage(photoa, bean.getPhotoUrl());
            amount1.setText("￥" + bean.getAmount());
        }

        back.setOnClickListener(this);
        preview_pay.setOnClickListener(this);
        preview_print.setOnClickListener(this);

        previewLayout = findViewById(R.id.preview_layout);

        paysuccessLayout = findViewById(R.id.submit_paysuccess_layout);
        paySuccessAmout = findViewById(R.id.submit_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.submit_pay_success_order_num);
        paySuccessTime = findViewById(R.id.submit_pay_success_order_paytime);
        toMainpage = findViewById(R.id.submit_pay_success_tomianpage);
        viewOrder = findViewById(R.id.submit_pay_success_vieworder);

        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);

        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_back:
                submitPayfailedFinish();
                break;
            case R.id.preview_pay:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PREVIEW_PAY);
                new PayDialogPreview(PreviewActivity.this, new PayDialogPreview.PayLisener() {
                    @Override
                    public void pay(int paytype) {
                        PreviewActivity.this.payType = paytype;
                        presenter.submit(bean.getPhotoNumber());
                    }
                });
                break;
            case R.id.preview_button_print:
                if (printBean != null) {
                    PrintPayBean printPayBean = new PrintPayBean();
                    if (printBean != null) {
                        printPayBean.setPhotoname(printBean.getSpec().getName());
                    }
                    printPayBean.setIdnumber(bean.getPhotoNumber());
                    printPayBean.setIncludecount(printBean.getIncludeCount());
                    printPayBean.setUrl(bean.getPhotoUrl());
                    printPayBean.setType(2);
                    Intent intent = new Intent(this, PrintSubmitActivity.class);
                    intent.putExtra(PrintSubmitActivity.PRINTPAY_BEAN, printPayBean);
                    startActivity(intent);
                }
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PREVIEW_PRINT);
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
    public void setPresenter(PreviewContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void submitSuccess(Order order) {
        this.order = order;
        Intent intent = new Intent();
        intent.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
        intent.putExtra(BaseActivity.CLOSEACTIVITY_PREVIEW, 1);
        sendBroadcast(intent);
        isSubmitSuccess = true;

        presenter.prepay(order.getOrderNumber(), payType + "");
    }

    @Override
    protected void finishByReceiver() {

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

    public void payV2(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PreviewActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                PayResult alipayResult = new PayResult(result);
                if (TextUtils.equals(Constants.ALIPAY_PAYSUECCESS_CODE, alipayResult.getResultStatus())) {
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    loadingEnd();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast("支付失败");
                            submitPayfailedFinish();
                        }
                    });
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
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
            previewLayout.setVisibility(View.GONE);
            paySuccessAmout.setText("实付：" + order.getAmount() + "元");
            paySuccessOrderNum.setText(order.getOrderNumber());
            paySuccessTime.setText(order.getPayTime());
            MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    PublicUtil.downloadImage(order.getPhoto().getImage(), order.getPhoto().getId() + "", PreviewActivity.this);
                }
            }, 1000);
        } else {
            ToastUtil.showToast("支付失败", true);
            submitPayfailedFinish();
        }
    }

    @Override
    public void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum) {
        DialogUtil.showPayFailedDialog(this, new PhotoDialog.OnDialogClickListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(PreviewActivity.this, OrderDetailActivity.class);
                intent.putExtra(ORDER, order);
                startActivity(intent);
                finish();
            }

            @Override
            public void cancel() {

            }
        });
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(Constants.PAY_ORDER_STATUS, -3) == Constants.WXPAY_PAYSUCCESS_CODE) {
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                handler.sendMessage(msg);
            } else {
                loadingEnd();
                ToastUtil.showToast("支付失败");
                submitPayfailedFinish();
            }
        }
    }

    private class MyHandler extends Handler {
        WeakReference<PreviewActivity> weakReference;

        public MyHandler(PreviewActivity previewActivity) {
            weakReference = new WeakReference<PreviewActivity>(previewActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final PreviewActivity previewActivity = weakReference.get();
            if (previewActivity != null) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        this.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                previewActivity.presenter.getOrderStatus(previewActivity.order.getId(), previewActivity.order.getOrderNumber(), previewActivity.payType);
                            }
                        }, 3000);

                        break;
                }
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_PREVIEW_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    /**
     * 监听按下事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            submitPayfailedFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void submitPayfailedFinish() {
        if (!isPaySuccess && isSubmitSuccess) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(ORDER, order);
            startActivity(intent);
        }
        finish();
    }
}
