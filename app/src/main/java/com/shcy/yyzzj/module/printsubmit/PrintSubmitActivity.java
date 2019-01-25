package com.shcy.yyzzj.module.printsubmit;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.AlertBean;
import com.shcy.yyzzj.bean.address.AddressBean;
import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.bean.express.ExpressBean;
import com.shcy.yyzzj.bean.express.ExpressListBean;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.pay.PayResult;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.PrintOrderPrice;
import com.shcy.yyzzj.bean.pay.PrintPayBean;
import com.shcy.yyzzj.bean.pay.WechatPayParameter;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.dialog.PayDialogPreview;
import com.shcy.yyzzj.module.about.H5Activity;
import com.shcy.yyzzj.module.addressadd.AddAddressActivity;
import com.shcy.yyzzj.module.addresslist.AddressActivity;
import com.shcy.yyzzj.module.orderdetail.OrderDetailActivity;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import static com.shcy.yyzzj.module.about.H5Activity.URL;
import static com.shcy.yyzzj.module.addresslist.AddressActivity.ADDRESSBEAN;

/**
 * Created by licong on 2018/10/10.
 */

public class PrintSubmitActivity extends Activity implements View.OnClickListener, PrintSubmitContract.View {

    private static final String TAG = "支付";
    public static final String PRINTPAY_BEAN = "printpay_bean";
    private static final int RESUQSTCODE_ADDRESSLIST = 101;
    public static final int ADDRESS_RESULT_CODE = 66;
    private static final int SDK_PAY_FLAG = 3;

    public static final String ORDER = "order";
    private PrintPayBean printPayBean;
    private TextView payBtn, expressPriceText, expressDetail,
            expressPriceJiText, printCountText, orderAmount1, orderAmount2, firstPrintPrice,
            addPrintAmoutPrice, expressPrice, noticeText, viewOrder, toMainpage,
            addressName, addressMobile, addressDetail, addPrintPrice, photoname, includeconut;
    private ImageView back, addAdress, expressImage, expressJiImage, addPrintCount,
            delPrintCount, noticeClose, noticeArrow;
    private LinearLayout addAddressLayout, noticeLayout, paySuccessLayout;
    private RelativeLayout addressLayout, submitLayout;
    private SimpleDraweeView photo;

    private TextView paySuccessAmout, paySuccessOrderNum, paySuccessTime;

    private int expressType = 1;//快递类型
    private int addressId = 0;//用户选择地址id
    private int printCount = 1;//冲印数量
    private Order order;
    private boolean paySuccess = false;
    private boolean isSubmitSuccess = false;
    private int payType = 2;//1-微信支付 2-支付宝支付
    private MyReceiver receiver;
    private Handler handler;

    private PrintSubmitContract.Presenter presenter;
    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printsubmit);
        handler = new Myhandler(this);
        new PrintSubmitPresenter(this);
        initView();
        initData();
    }

    private void initData() {

        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));

        printPayBean = (PrintPayBean) getIntent().getSerializableExtra(PRINTPAY_BEAN);
        if (printPayBean == null) {
            return;
        }
        FrescoUtils.getInstance().showImage(photo, printPayBean.getUrl());
        setExpressType(1);
        photoname.setText(printPayBean.getPhotoname());
        includeconut.setText(printPayBean.getIncludecount() + "张/版");

        expressImage.setOnClickListener(this);
        expressJiImage.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        addPrintCount.setOnClickListener(this);
        delPrintCount.setOnClickListener(this);

        presenter.getAlert();
        presenter.getAddressList();
        presenter.getExpressList();
        presenter.getOrderPrice(printPayBean.getType(), printPayBean.getIdnumber(), expressType, printCount);
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        back = findViewById(R.id.submit_back);
        payBtn = findViewById(R.id.submit_pay);
        expressImage = findViewById(R.id.submit_express_pu_choice);
        expressJiImage = findViewById(R.id.printsubmit_express_ji_choice);
        photo = findViewById(R.id.printsubmit_photo);
        expressPriceText = findViewById(R.id.printpay_express_price_text);
        expressPriceJiText = findViewById(R.id.printsubmit_expressji_price_text);
        printCountText = findViewById(R.id.printsubmit_printcount_text);

        orderAmount1 = findViewById(R.id.printsubmit_amount1);
        orderAmount2 = findViewById(R.id.printpay_amount2);
        firstPrintPrice = findViewById(R.id.printpay_first_price);
        addPrintAmoutPrice = findViewById(R.id.printpay_addconut_price);
        expressPrice = findViewById(R.id.printpay_express_price);
        addPrintPrice = findViewById(R.id.printsubmit_addprintcount_price);

        addAddressLayout = findViewById(R.id.printsubmit_addaddress_layout);
        addressLayout = findViewById(R.id.printsubmit_address_layout);
        addAdress = findViewById(R.id.printsubmit_addaddress_btn);
        addressName = findViewById(R.id.printsubmit_address_name);
        addressMobile = findViewById(R.id.printsubmit_address_mobile);
        addressDetail = findViewById(R.id.printsubmit_address_address);

        addPrintCount = findViewById(R.id.printsubmit_printcount_add);
        delPrintCount = findViewById(R.id.printsubmit_printcount_del);
        expressDetail = findViewById(R.id.printsubmit_express_detail);
        photoname = findViewById(R.id.printsubmit_photoname);
        includeconut = findViewById(R.id.printsubmit_includecount);

        submitLayout = findViewById(R.id.printsubmit_layout);
        paySuccessLayout = findViewById(R.id.submit_paysuccess_layout);
        paySuccessAmout = findViewById(R.id.submit_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.submit_pay_success_order_num);
        paySuccessTime = findViewById(R.id.submit_pay_success_order_paytime);
        toMainpage = findViewById(R.id.submit_pay_success_tomianpage);
        viewOrder = findViewById(R.id.submit_pay_success_vieworder);

        noticeLayout = findViewById(R.id.printsubmit_notice_layout);
        noticeClose = findViewById(R.id.printsubmit_notice_close);
        noticeText = findViewById(R.id.printsubmit_notice_text);
        noticeArrow = findViewById(R.id.printsubmit_notice_arrow);

        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);

        addAddressLayout.setOnClickListener(this);
        addAdress.setOnClickListener(this);
        expressDetail.setOnClickListener(this);

        noticeClose.setOnClickListener(this);
    }

    private void setExpressType(int type) {
        if (expressType != type) {
            expressType = type;
            if (expressType == 1) {
                expressImage.setImageResource(R.mipmap.submit_paychoice);
                expressJiImage.setImageResource(R.mipmap.submit_pay_unchoice);
            } else {
                expressImage.setImageResource(R.mipmap.submit_pay_unchoice);
                expressJiImage.setImageResource(R.mipmap.submit_paychoice);
            }
            presenter.getOrderPrice(printPayBean.getType(), printPayBean.getIdnumber(), expressType, printCount);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_back:
                submitPayfailedFinish();
                break;
            case R.id.submit_express_pu_choice:
                setExpressType(1);
                break;
            case R.id.printsubmit_express_ji_choice:
                setExpressType(2);
                break;
            case R.id.submit_pay:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_PAY);
                new PayDialogPreview(PrintSubmitActivity.this, new PayDialogPreview.PayLisener() {
                    @Override
                    public void pay(int paytype) {
                        PrintSubmitActivity.this.payType = paytype;
                        presenter.printSubmit(printPayBean.getType(), printPayBean.getIdnumber() + "", addressId + "", expressType + "", printCount + "");
                    }
                });
                //(int type,String photoId,String addressId, String expressType, String printCount,String payType)
                break;
            case R.id.printsubmit_printcount_add:
                printCount++;
                printCountText.setText(printCount + "");
                presenter.getOrderPrice(printPayBean.getType(), printPayBean.getIdnumber(), expressType, printCount);
                break;
            case R.id.printsubmit_printcount_del:
                if (printCount > 1) {
                    printCount--;
                    printCountText.setText(printCount + "");
                    presenter.getOrderPrice(printPayBean.getType(), printPayBean.getIdnumber(), expressType, printCount);
                }
                break;

            case R.id.printsubmit_addaddress_layout:
                Intent intent2 = new Intent(this, AddressActivity.class);
                startActivityForResult(intent2, RESUQSTCODE_ADDRESSLIST);
                break;
            case R.id.printsubmit_addaddress_btn:
                Intent intent4 = new Intent(this, AddAddressActivity.class);
                startActivityForResult(intent4, RESUQSTCODE_ADDRESSLIST);
                break;
            case R.id.printsubmit_express_detail:
                Intent intent3 = new Intent(this, H5Activity.class);
                intent3.putExtra(URL, Constants.EXPRESS_DETAIL_URL);
                startActivity(intent3);
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
            case R.id.printsubmit_notice_close:
                noticeLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setPresenter(PrintSubmitContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void submitSuccess(Order order) {
        if (order != null) {
            isSubmitSuccess = true;
            this.order = order;
            Intent intent2 = new Intent();
            intent2.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
            sendBroadcast(intent2);

            presenter.prepay(order.getOrderNumber(), payType + "");
        }
    }

    @Override
    public void submitFailed() {

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
            submitLayout.setVisibility(View.GONE);
            paySuccessAmout.setText("实付：" + order.getAmount() + "元");
            paySuccessOrderNum.setText("订单编号：" + order.getOrderNumber());
            paySuccessTime.setText("支付时间：" + order.getPayTime());
            MobclickAgent.onEvent(this, Constants.EVENT_PRINT_PAYSUECCESS);
        } else {
            ToastUtil.showToast("支付失败", true);
        }
    }

    @Override
    public void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum) {

    }

    @Override
    public void showAlert(final AlertBean alertBean) {
        if (alertBean == null) {
            return;
        }
        if (alertBean.getStatus() == 1) {
            noticeLayout.setVisibility(View.VISIBLE);
            noticeText.setText(alertBean.getTitle());
            noticeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(PrintSubmitActivity.this, H5Activity.class);
                    intent3.putExtra(URL, alertBean.getUrl());
                    startActivity(intent3);
                }
            });
        } else {
            noticeLayout.setVisibility(View.GONE);
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
    public void showExpressList(ExpressListBean expressListBean) {
        List<ExpressBean> list = expressListBean.getList();
        if (list == null || list.size() == 0) {
            return;
        }
        for (ExpressBean bean : list) {
            if (bean.getExpressType() == 1) {
                expressPriceText.setText("普通快递" + bean.getPrice());
            }
            if (bean.getExpressType() == 2) {
                expressPriceJiText.setText("加急快递" + bean.getPrice());
            }
        }
    }

    @Override
    public void showAddressList(AddressListBean addressListBean) {
        List<AddressBean> list = addressListBean.getData();
        if (list != null && list.size() > 0) {
            addAdress.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);
            setAddress(list.get(0));
        } else {
            addAdress.setVisibility(View.VISIBLE);
            addressLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOrderPrice(PrintOrderPrice printOrderPrice) {
        if (printOrderPrice != null) {
            orderAmount1.setText(printOrderPrice.getFirstPrintAmount());
            orderAmount2.setText("¥" + printOrderPrice.getTotalAmount());
            firstPrintPrice.setText("首版:" + printOrderPrice.getFirstPrintAmount() + "*1");
            if (printOrderPrice.getPrintCount() > 1) {
                int num = printOrderPrice.getPrintCount() - 1;
                addPrintAmoutPrice.setText("加印:" + printOrderPrice.getPrintAmount() + "*" + num);
            } else {
                addPrintAmoutPrice.setText("");
            }
            expressPrice.setText("快递:" + printOrderPrice.getExpressPrice());
            addPrintPrice.setText("(加印一版仅需" + printOrderPrice.getPrintAmount() + "元)");
        }
    }

    public void payV2(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PrintSubmitActivity.this);
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

    private static class Myhandler extends Handler {
        WeakReference<PrintSubmitActivity> weakReference;

        Myhandler(PrintSubmitActivity printPayActivity) {
            weakReference = new WeakReference<PrintSubmitActivity>(printPayActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final PrintSubmitActivity printSubmitActivity = weakReference.get();
            if (printSubmitActivity != null) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        this.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                printSubmitActivity.presenter.getOrderStatus(printSubmitActivity.order.getId(), printSubmitActivity.order.getOrderNumber(), printSubmitActivity.payType);
                            }
                        }, 3000);
                        break;
                }
            }
        }
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
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_PRINT_SUBMIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESUQSTCODE_ADDRESSLIST) {
            if (resultCode == ADDRESS_RESULT_CODE) {
                setAddress((AddressBean) data.getSerializableExtra(ADDRESSBEAN));
            } else {
                presenter.getAddressList();
            }
        }
    }

    private void setAddress(AddressBean address) {
        if (address != null) {
            addressLayout.setVisibility(View.VISIBLE);
            addAdress.setVisibility(View.GONE);
            addressId = address.getId();
            addressName.setText(address.getRecipientsName());
            addressMobile.setText(address.getRecipientsMobile());
            addressDetail.setText(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailedAddress());
        } else {
            addAdress.setVisibility(View.VISIBLE);
        }
    }

    private void submitPayfailedFinish() {
        if (!paySuccess && isSubmitSuccess) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(ORDER, order);
            startActivity(intent);
        }
        finish();
    }
}
