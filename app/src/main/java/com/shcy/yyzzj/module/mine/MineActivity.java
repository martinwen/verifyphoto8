package com.shcy.yyzzj.module.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.share.ShareAppBean;
import com.shcy.yyzzj.bean.share.ShareContent;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.module.about.AboutActivity;
import com.shcy.yyzzj.module.help.HelpActivity;
import com.shcy.yyzzj.module.login.LoginActivity;
import com.shcy.yyzzj.module.login.LoginModel;
import com.shcy.yyzzj.module.orderlist.OrderListActivity;
import com.shcy.yyzzj.module.share.ShareManager;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.utils.SetUtils;
import com.shcy.yyzzj.utils.UserUtil;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;

/**
 * Created by licong on 2018/10/11.
 */

public class MineActivity extends BaseActivity implements View.OnClickListener, MineContract.View {

    private static final String TAG = "我的";

    private LinearLayout orderLayout, feedback, about, logout, share, help;
    private SimpleDraweeView head;
    private TextView nickname;
    private ImageView back;
    private MineContract.Presenter presenter;
    private LodingDialog lodingDialog;
    private ShareManager shareManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        initData();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        orderLayout = findViewById(R.id.mine_order_layout);
        feedback = findViewById(R.id.mine_feedback_layout);
        about = findViewById(R.id.mine_about_layout);
        logout = findViewById(R.id.mine_logout_layout);
        share = findViewById(R.id.mine_share_layout);
        back = findViewById(R.id.mine_back);
        head = findViewById(R.id.mine_user_head);
        nickname = findViewById(R.id.mine_nickname);
        help = findViewById(R.id.mine_help_layout);
        orderLayout.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        share.setOnClickListener(this);
        back.setOnClickListener(this);
        head.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    private void initData() {
        new MinePresenter(this);
        shareManager = new ShareManager(this);
        FrescoUtils.getInstance().showImage(head, UserUtil.getInstance().getAvatar(), R.mipmap.default_avatar);
        if (SetUtils.getInstance().getIsLogin()) {
            nickname.setText(UserUtil.getInstance().getUserNickName());
        } else {
            nickname.setText("未登录");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SetUtils.getInstance().getIsLogin()) {
            logout.setVisibility(View.VISIBLE);
        } else {
            logout.setVisibility(View.GONE);
        }
        initData();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.mine_back:
                finish();
                break;
            case R.id.mine_order_layout:
                intent = new Intent(this, OrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_feedback_layout:
                presenter.getCustomServer();
                break;
            case R.id.mine_about_layout:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_logout_layout:
                DialogUtil.showLogoutDialog(this, new PhotoDialog.OnDialogClickListener() {
                    @Override
                    public void confirm() {
                        Constants.TOKEN = "";
                        SetUtils.getInstance().setToken("");
                        SetUtils.getInstance().setIsLogin(false);
                        UserUtil.getInstance().clearAll();
                        logout.setVisibility(View.GONE);
                        new LoginModel().login(new LoginModel.LoginCallBack() {
                            @Override
                            public void onSuccess() {
                                initData();
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.mine_user_head:
                if (!SetUtils.getInstance().getIsLogin()) {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.mine_share_layout:
                presenter.getShareAppConfig();
                break;
            case R.id.mine_help_layout:
                Intent intent1 = new Intent(this, HelpActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void showServerMessage(String msg) {
        DialogUtil.showNoticeDialog(this, msg, new PhotoDialog.OnDialogClickListener() {
            @Override
            public void confirm() {

            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void shareApp(ShareAppBean shareAppBean) {
        ShareContent shareContent = new ShareContent();
        shareContent.setIcon(shareAppBean.getIcon());
        shareContent.setLinkUrl(shareAppBean.getUrl());
        shareContent.setSummary(shareAppBean.getSummary());
        shareContent.setTitle(shareAppBean.getTitle());
        shareManager.shareContent(shareContent, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareManager != null) {
            shareManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
