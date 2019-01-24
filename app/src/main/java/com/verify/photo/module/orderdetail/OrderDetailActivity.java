package com.verify.photo.module.orderdetail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.verify.photo.R;
import com.verify.photo.base.BaseActivity;
import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.bean.order.Order;
import com.verify.photo.bean.order.OrderPhoto;
import com.verify.photo.bean.order.OrderSpec;
import com.verify.photo.bean.pay.PayResult;
import com.verify.photo.bean.pay.PrePayInfoBean;
import com.verify.photo.bean.pay.PrintPayBean;
import com.verify.photo.bean.pay.WechatPayParameter;
import com.verify.photo.config.Constants;
import com.verify.photo.dialog.ImageDialog;
import com.verify.photo.dialog.LodingDialog;
import com.verify.photo.dialog.PayDialog;
import com.verify.photo.dialog.PhotoDialog;
import com.verify.photo.module.printsubmit.PrintSubmitActivity;
import com.verify.photo.utils.DialogUtil;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.utils.fresco.FrescoUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

import static com.verify.photo.module.orderlist.OrderListActivity.RESULTCODE_OEDERDETAIL;
import static com.verify.photo.module.pay.PayActivity.ORDER;


/**
 * Created by licong on 2018/10/12.
 */

public class OrderDetailActivity extends Activity implements OnClickListener, OrderDetailContract.View {

    private static final String TAG = "订单详情";

    private static final int SDK_PAY_FLAG = 1;
    private OrderDetailContract.Presenter presenter;
    private Order order;
    private ImageView back;
    private TextView saveSuccess, title, photoName, photoIns, orderNum, createTime, payTime, refundTime, amout1, amount2, payStatus1, copyExpressNum,
            payStatus2, name, mobile, address, button, buttonPrint, printcount, includecount, expresscompany,
            expressnumber, paySuccessAmout, paySuccessOrderNum, paySuccessTime, viewOrder, toMainpage;
    private RelativeLayout paytimeLayout, refundtimeLayout, amoutLayout1, amoutLayout2, expresscompanyLayout, expressnumberLayout;
    private LinearLayout addressLayout, printcountLayout, savetoWx, paySuccessLayout, orderDetailLayout;
    private SimpleDraweeView photo;
    private ImageDialog imageDialog;
    private LodingDialog lodingDialog;
    private int paytype;

    private Handler handler;
    private MyReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        new OrderDetailPresenter(this);
        handler = new MyHandler(this);
        initView();
        initData();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        title = findViewById(R.id.order_detail_title);
        back = findViewById(R.id.order_detail_back);
        button = findViewById(R.id.order_detail_button);
        buttonPrint = findViewById(R.id.order_detail_button_print);
        photo = findViewById(R.id.order_detail_photo);
        photoName = findViewById(R.id.order_detail_photoname);
        photoIns = findViewById(R.id.order_detail_photo_instruction);
        orderNum = findViewById(R.id.order_detail_ordernum);
        createTime = findViewById(R.id.order_detail_createtime);
        payTime = findViewById(R.id.order_detail_paytime);
        refundTime = findViewById(R.id.order_detail_refundtime);
        amout1 = findViewById(R.id.order_detail_amount);
        amount2 = findViewById(R.id.order_detail_refundAmout);
        paytimeLayout = findViewById(R.id.order_detail_paytime_layout);
        refundtimeLayout = findViewById(R.id.order_detail_refundtime_layout);
        amoutLayout1 = findViewById(R.id.order_detail_amout_layout);
        amoutLayout2 = findViewById(R.id.order_detail_refundAmount_layout);
        payStatus1 = findViewById(R.id.order_detail_pay_status);
        payStatus2 = findViewById(R.id.order_detail_pay_status2);
        addressLayout = findViewById(R.id.order_detail_addresslayout);
        name = findViewById(R.id.order_detail_name);
        mobile = findViewById(R.id.order_detail_mobile);
        address = findViewById(R.id.order_detail_address);
        printcount = findViewById(R.id.order_detail_printcount);
        includecount = findViewById(R.id.order_detail_includecount);
        printcountLayout = findViewById(R.id.order_detail_printcountlayout);
        expresscompany = findViewById(R.id.order_detail_expresscompany);
        expresscompanyLayout = findViewById(R.id.order_detail_expresscompany_layout);
        expressnumber = findViewById(R.id.order_detail_expressnumber);
        expressnumberLayout = findViewById(R.id.order_detail_expressnumber_layout);
        copyExpressNum = findViewById(R.id.order_detail_copy);
        savetoWx = findViewById(R.id.orderdetail_saveto_wx);

        back.setOnClickListener(this);
        button.setOnClickListener(this);
        buttonPrint.setOnClickListener(this);
        copyExpressNum.setOnClickListener(this);
        savetoWx.setOnClickListener(this);

        imageDialog = new ImageDialog(this);

        orderDetailLayout = findViewById(R.id.orderdetail_layout);
        paySuccessLayout = findViewById(R.id.submit_paysuccess_layout);
        toMainpage = findViewById(R.id.submit_pay_success_tomianpage);
        viewOrder = findViewById(R.id.submit_pay_success_vieworder);
        paySuccessAmout = findViewById(R.id.submit_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.submit_pay_success_order_num);
        paySuccessTime = findViewById(R.id.submit_pay_success_order_paytime);
        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);
        saveSuccess = findViewById(R.id.save_success);
    }

    private void initData() {
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));
        order = (Order) getIntent().getSerializableExtra(ORDER);
        presenter.getOrderDetail(order.getId(), order.getOrderNumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_back:
                finish();
                break;
            case R.id.order_detail_button:
                if (order == null) {
                    return;
                }
                if (order.getStatus() == 10) {//待支付
                    new PayDialog(OrderDetailActivity.this, order, new PayDialog.PayLisener() {
                        @Override
                        public void pay(String ordernum, int paytype) {
                            presenter.prepay(ordernum, paytype + "");
                            OrderDetailActivity.this.paytype = paytype;
                        }
                    });
                } else if (order.getStatus() == 20) {//已支付，下载图片
                    PublicUtil.downloadImage(order.getPhoto().getImage(), order.getId() + "", this);
                    ToastUtil.showToast(getString(R.string.download_success), false);
                } else if (order.getStatus() == 22) {//已发货
                    DialogUtil.showConfirmRreceiptDialog(this, new PhotoDialog.OnDialogClickListener() {
                        @Override
                        public void confirm() {
                            presenter.PrintOrderConfirm(order.getOrderNumber());
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
                break;
            case R.id.order_detail_photo:
                String img = (String) v.getTag();
                if (imageDialog != null) {
                    imageDialog.showPhotoDetail(img);
                    imageDialog.show();
                }
                break;
            case R.id.order_detail_button_print:
                PrintPayBean printPayBean = new PrintPayBean();
                printPayBean.setPhotoname(order.getSpec().getName());
                printPayBean.setIdnumber(order.getPhoto().getId() + "");
                printPayBean.setIncludecount(order.getPhoto().getIncludeCount());
                printPayBean.setUrl(order.getPhoto().getImage());
                printPayBean.setType(1);
                Intent intent = new Intent(this, PrintSubmitActivity.class);
                intent.putExtra(PrintSubmitActivity.PRINTPAY_BEAN, printPayBean);
                startActivity(intent);
                break;
            case R.id.order_detail_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(expressnumber.getText().toString().trim());
                ToastUtil.showToast("复制成功");
                break;
            case R.id.orderdetail_saveto_wx:
                UMImage image = new UMImage(this, order.getPhoto().getImage());
                image.setThumb(image);
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(image)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .share();
                break;
            case R.id.printpay_pay_success_tomianpage:
                Intent intent1 = new Intent();
                intent1.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
                sendBroadcast(intent1);
                finish();
                break;
            case R.id.printpay_pay_success_vieworder:
                orderDetailLayout.setVisibility(View.VISIBLE);
                paySuccessLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setPresenter(OrderDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showOrderDetail(final Order order) {
        this.order = order;
        OrderPhoto orderPhoto = order.getPhoto();
        OrderSpec spec = order.getSpec();
        FrescoUtils.getInstance().showImage(photo, orderPhoto.getImage());
        photo.setTag(order.getPhoto().getImage());
        photo.setOnClickListener(this);
        photoName.setText(spec.getName());
        photoIns.setText("尺寸：" + spec.getInstruction());
        orderNum.setText(order.getOrderNumber());
        createTime.setText(order.getCreateTime());
        String amountStr1 = "¥" + order.getAmount();
        String amountStr2 = "¥" + order.getRefundAmount();

        if (order.getType() == 2) {
            addressLayout.setVisibility(View.VISIBLE);
            printcountLayout.setVisibility(View.VISIBLE);

            name.setText(order.getRecipientsName());
            mobile.setText(order.getRecipientsMobile());
            address.setText(order.getProvince() + order.getCity() + order.getDistrict() + order.getDetailedAddress());

            printcount.setText("X" + order.getPrintCount() + "版");
            includecount.setText(order.getPhoto().getIncludeCount() + "张/版");
        } else {
            addressLayout.setVisibility(View.GONE);
            printcountLayout.setVisibility(View.GONE);
        }

        switch (order.getStatus()) {//0—订单关闭 10-待支付 20-支付成功  21—待发货   22—已发货   24—已完成 30-已退款
            case 0://已关闭
                title.setText("已关闭");
                button.setVisibility(View.GONE);
                payStatus1.setText("未支付");
                amoutLayout1.setVisibility(View.VISIBLE);
                amoutLayout2.setVisibility(View.GONE);
                amout1.setText(amountStr1);
                break;
            case 10://待支付
                title.setText("待支付");
                amoutLayout1.setVisibility(View.VISIBLE);
                amoutLayout2.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setText("立即支付");
                payStatus1.setText("待付款");
                amout1.setText(amountStr1);
                break;
            case 20://支付成功
                title.setText("支付成功");
                if (order.getType() == 1) {
                    button.setBackgroundResource(R.mipmap.gradual_circle_back_small);
                    button.setVisibility(View.VISIBLE);
                    button.setText("下载照片");
                    buttonPrint.setVisibility(View.VISIBLE);
                    savetoWx.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
                payTime.setText(order.getPayTime());
                amoutLayout1.setVisibility(View.VISIBLE);
                amoutLayout2.setVisibility(View.GONE);
                payStatus1.setText("已付款");
                amout1.setText(amountStr1);
                break;
            case 21://待发货
                title.setText("待发货");
                button.setVisibility(View.GONE);
                payTime.setText(order.getPayTime());
                amoutLayout1.setVisibility(View.VISIBLE);
                amoutLayout2.setVisibility(View.GONE);
                payStatus1.setText("已付款");
                amout1.setText(amountStr1);
                break;
            case 22://已发货
                title.setText("已发货");
                button.setVisibility(View.VISIBLE);
                button.setText("确认收货");
                payTime.setText(order.getPayTime());
                amoutLayout2.setVisibility(View.GONE);
                payStatus1.setText("已付款");
                amout1.setText(amountStr1);
                expressnumberLayout.setVisibility(View.VISIBLE);
                expresscompanyLayout.setVisibility(View.VISIBLE);
                expresscompany.setText(order.getExpressCompany());
                expressnumber.setText(order.getExpressNumber());
                break;
            case 24://已完成
                title.setText("已完成");
                button.setVisibility(View.GONE);
                payTime.setText(order.getPayTime());
                expressnumberLayout.setVisibility(View.VISIBLE);
                expresscompanyLayout.setVisibility(View.VISIBLE);
                expresscompany.setText(order.getExpressCompany());
                expressnumber.setText(order.getExpressNumber());
                amoutLayout1.setVisibility(View.VISIBLE);
                amout1.setText(amountStr1);
                break;
            case 30://已退款
                title.setText("已退款");
                refundtimeLayout.setVisibility(View.VISIBLE);
                amoutLayout1.setVisibility(View.VISIBLE);
                amoutLayout2.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                payTime.setText(order.getPayTime());
                refundTime.setText(order.getRefundTime());
                payStatus1.setText("已付款");
                payStatus2.setText("已退款");
                amout1.setText(amountStr1);
                amount2.setText(amountStr2);
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(ORDER, order);
        setResult(RESULTCODE_OEDERDETAIL, intent);
    }

    @Override
    public void showPayStatus(final Order order) {
        if (order.getStatus() == 20) {//支付成功
            MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
            paySuccessLayout.setVisibility(View.VISIBLE);
            orderDetailLayout.setVisibility(View.GONE);
            paySuccessAmout.setText("实付：" + order.getAmount() + "元");
            paySuccessOrderNum.setText("订单编号：" + order.getOrderNumber());
            paySuccessTime.setText("支付时间：" + order.getPayTime());
            if (order.getType() == 1) {
                MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
                saveSuccess.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PublicUtil.downloadImage(order.getPhoto().getImage(), order.getPhoto().getId() + "", OrderDetailActivity.this);
                    }
                }, 1000);
            } else {
                MobclickAgent.onEvent(this, Constants.EVENT_PRINT_PAYSUECCESS);
            }
        } else {
            ToastUtil.showToast("支付失败", true);
        }

        showOrderDetail(order);
    }

    @Override
    public void confirmOrder(ResultBean resultBean) {
        button.setVisibility(View.GONE);
        ToastUtil.showToast(resultBean.getMsg(), false);
        presenter.getOrderDetail(order.getUserId(), order.getOrderNumber());
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
                PayTask alipay = new PayTask(OrderDetailActivity.this);
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
                        }
                    });
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void prepaySuccess(PrePayInfoBean prePayInfoBean) {
        if (paytype == 1) {
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
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
            }
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<OrderDetailActivity> weakReference;

        public MyHandler(OrderDetailActivity activity) {
            weakReference = new WeakReference<OrderDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final OrderDetailActivity activity = weakReference.get();
            if (activity != null) {
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.presenter.getOrderStatus(activity.order.getId(), activity.order.getOrderNumber(), activity.paytype);
                    }
                }, 3000);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
