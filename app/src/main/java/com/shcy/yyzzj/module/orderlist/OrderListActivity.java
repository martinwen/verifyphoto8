package com.shcy.yyzzj.module.orderlist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.order.Order;
import com.shcy.yyzzj.bean.order.OrderListBean;
import com.shcy.yyzzj.bean.pay.PayResult;
import com.shcy.yyzzj.bean.pay.PrePayInfoBean;
import com.shcy.yyzzj.bean.pay.WechatPayParameter;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.dialog.PayDialog;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.module.orderdetail.OrderDetailActivity;
import com.shcy.yyzzj.module.pay.PayActivity;
import com.shcy.yyzzj.module.printsubmit.PrintSubmitActivity;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.view.view.HeadFootAdapter;
import com.shcy.yyzzj.view.view.MultTemplateAdapter;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.OnLoadMoreListener;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.SwipeToLoadLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.shcy.yyzzj.module.pay.PayActivity.ORDER;

/**
 * Created by licong on 2018/10/12.
 */

public class OrderListActivity extends Activity implements View.OnClickListener, OrderListContract.View, OnLoadMoreListener {

    private static final String TAG = "订单列表";
    public static final String PAYRECEIVER_ACTION = "pay_receiver_action";
    private static final int SDK_PAY_FLAG = 2;
    private static final int REQUESTCODE_OEDERDETAIL = 19;
    public static final int RESULTCODE_OEDERDETAIL = 3;

    private OrderListContract.Presenter presenter;
    private ImageView back;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private HeadFootAdapter adapter;
    private LinearLayout listLayout, paySuccessLayout;
    private List<Order> list = new ArrayList<>();
    private int pageNo = 1;
    private boolean isLoadding;
    private TextView empty, paySuccessAmout, paySuccessOrderNum, paySuccessTime, saveSuccess, viewOrder, toMainpage;
    private int clickPosition = -1;
    private PayReceiver receiver;
    private MyReceiver wxPayReceiver;
    private Handler handler;
    private int payPosition;
    private LodingDialog lodingDialog;
    private int paytype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        new OrderListPresenter(this);
        handler = new MyHandler(this);
        initView();
        initData();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        back = findViewById(R.id.orderlist_back);
        empty = findViewById(R.id.order_detail_empty);
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        recyclerView = findViewById(R.id.swipe_target);
        swipeToLoadLayout.setRefreshEnabled(false);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter());
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new MultTemplateAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                if (position < list.size()) {
                    Order order = list.get(position);
                    Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                    intent.putExtra(ORDER, order);
                    startActivityForResult(intent, REQUESTCODE_OEDERDETAIL);
                    clickPosition = position;
                }
            }
        });

        back.setOnClickListener(this);
        receiver = new PayReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PAYRECEIVER_ACTION);
        registerReceiver(receiver, intentFilter);
        wxPayReceiver = new MyReceiver();
        registerReceiver(wxPayReceiver, new IntentFilter(Constants.PAY_BROADCASTRECEIVER));

        listLayout = findViewById(R.id.orderlist_layout);
        paySuccessLayout = findViewById(R.id.submit_paysuccess_layout);
        toMainpage = findViewById(R.id.submit_pay_success_tomianpage);
        viewOrder = findViewById(R.id.submit_pay_success_vieworder);
        paySuccessAmout = findViewById(R.id.submit_pay_success_amout);
        paySuccessOrderNum = findViewById(R.id.submit_pay_success_order_num);
        paySuccessTime = findViewById(R.id.submit_pay_success_order_paytime);
        saveSuccess = findViewById(R.id.save_success);
        toMainpage.setOnClickListener(this);
        viewOrder.setOnClickListener(this);
    }

    private void initData() {
        presenter.getOrderList(pageNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderlist_back:
                finish();
                break;
            case R.id.template_orderlist_button:
                payPosition = (int) v.getTag();
                if (payPosition >= 0 && payPosition < list.size()) {
                    final Order order = list.get(payPosition);
                    if (order.getStatus() == 10) {
                        new PayDialog(OrderListActivity.this, order, new PayDialog.PayLisener() {
                            @Override
                            public void pay(String ordernum, int paytype) {
                                presenter.prepay(ordernum, paytype + "");
                                OrderListActivity.this.paytype = paytype;
                            }
                        });
                    } else {
                        DialogUtil.showConfirmRreceiptDialog(this, new PhotoDialog.OnDialogClickListener() {
                            @Override
                            public void confirm() {
                                presenter.PrintOrderConfirm(order);
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                    }
                }
                break;
            case R.id.submit_pay_success_tomianpage:
                Intent intent1 = new Intent();
                intent1.setAction(BaseActivity.CLOSEACTIVITY_ACTION);
                sendBroadcast(intent1);
                finish();
                break;
            case R.id.submit_pay_success_vieworder:
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(PrintSubmitActivity.ORDER, list.get(payPosition));
                startActivity(intent);
                listLayout.setVisibility(View.VISIBLE);
                paySuccessLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setPresenter(OrderListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showOrderList(OrderListBean bean) {
        isLoadding = false;
        swipeToLoadLayout.setLoadingMore(false);
        if (pageNo == 1) {
            list.clear();
        }
        if (bean.getNextCursor() != 0) {
            swipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            swipeToLoadLayout.setLoadMoreEnabled(false);
        }
        list.addAll(bean.getData());
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
        pageNo++;
    }

    private static class MyHandler extends Handler {
        WeakReference<OrderListActivity> weakReference;

        public MyHandler(OrderListActivity orderListActivity) {
            weakReference = new WeakReference<OrderListActivity>(orderListActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final OrderListActivity orderListActivity = weakReference.get();
            if (orderListActivity != null) {
                final Order order = orderListActivity.list.get(orderListActivity.payPosition);
                if (msg.what == SDK_PAY_FLAG) {
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            orderListActivity.presenter.getOrderStatus(order.getId(), order.getOrderNumber(), orderListActivity.paytype);
                        }
                    }, 3000);
                }
            }
        }
    }

    public void alipay(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderListActivity.this);
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
    public void prepaySuccess(PrePayInfoBean bean) {
        if (paytype == 1) {
            WechatPayParameter wechatPayParameter = bean.getWeixinParameter();
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
            alipay(bean.getAlipayParameter());
        }
    }

    @Override
    public void prepayFailed() {

    }

    @Override
    public void getOrderStuatusSuccess(final Order order) {
        list.set(payPosition, order);
        adapter.notifyDataSetChanged();
        if (order.getStatus() == 20) {//支付成功
            MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
            paySuccessLayout.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            paySuccessAmout.setText("实付：" + order.getAmount() + "元");
            paySuccessOrderNum.setText("订单编号：" + order.getOrderNumber());
            paySuccessTime.setText("支付时间：" + order.getPayTime());
            if (order.getType() == 1) {
                MobclickAgent.onEvent(this, Constants.EVENT_PAYSUCCESS_PV);
                saveSuccess.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        PublicUtil.downloadImage(order.getPhoto().getImage(), order.getPhoto().getId() + "", OrderListActivity.this);
                    }
                }, 1000);
            } else {
                saveSuccess.setVisibility(View.GONE);
                MobclickAgent.onEvent(this, Constants.EVENT_PRINT_PAYSUECCESS);
            }
        } else {
            ToastUtil.showToast("支付失败", true);
        }
    }

    @Override
    public void getOrderStuatusFailed(@Nullable int orderId, @Nullable String orderNum) {
        DialogUtil.showPayFailedDialog(this, new PhotoDialog.OnDialogClickListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra(ORDER, list.get(payPosition));
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

    @Override
    public void showOrder(Order order) {
        list.set(payPosition, order);
        adapter.notifyDataSetChanged();
    }

    public HeadFootAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new OrderListTemplate(this, this));
        }
        return adapter;
    }

    @Override
    public void onLoadMore() {
        if (isLoadding) {
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        presenter.getOrderList(pageNo);
        isLoadding = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_ORDERLIST_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (wxPayReceiver != null) {
            unregisterReceiver(wxPayReceiver);
        }
    }

    private class PayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = (int) intent.getIntExtra(PayActivity.ORDER_STATUS, 0);
            if (adapter != null && clickPosition != -1) {
                ((Order) adapter.getList().get(clickPosition)).setStatus(status);
                adapter.notifyDataSetChanged();
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_OEDERDETAIL && resultCode == RESULTCODE_OEDERDETAIL) {
            Order order = (Order) data.getSerializableExtra(ORDER);
            if (adapter != null && clickPosition != -1) {
                list.set(clickPosition, order);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
