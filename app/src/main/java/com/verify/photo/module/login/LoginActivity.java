package com.verify.photo.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.verify.photo.R;
import com.verify.photo.config.Constants;
import com.verify.photo.module.about.H5Activity;
import com.verify.photo.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import static com.verify.photo.module.about.H5Activity.TITLE;
import static com.verify.photo.module.about.H5Activity.URL;

/**
 * Created by licong on 2018/10/10.
 */

public class LoginActivity extends Activity implements View.OnClickListener, LoginContract.View {

    private static final String TAG = "登录";

    private ImageView back;
    private LinearLayout loginLayout, verifyLayout;
    private EditText phoneNum, verifyCode;
    private TextView agreenment, timeText, verifyPhone, code1, code2, code3, code4;
    private ImageView deletePhoneNum, loginBtn;
    private Timer timer = new Timer();
    private int time = 60;
    private long pauseTime = 0;

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new LoginPresenter(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        back = findViewById(R.id.login_back);
        loginLayout = findViewById(R.id.login_login_layout);
        loginBtn = findViewById(R.id.login_nextbtn);
        phoneNum = findViewById(R.id.login_phone_edit);
        agreenment = findViewById(R.id.login_user_argreenment);
        deletePhoneNum = findViewById(R.id.login_phone_delete);

        back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        agreenment.setOnClickListener(this);
        deletePhoneNum.setOnClickListener(this);

        verifyLayout = findViewById(R.id.login_verify_layout);
        verifyPhone = findViewById(R.id.login_verify_phone);
        verifyCode = findViewById(R.id.login_verifycode_edit);
        timeText = findViewById(R.id.login_verify_time);

        code1 = findViewById(R.id.login_verify_code1);
        code2 = findViewById(R.id.login_verify_code2);
        code3 = findViewById(R.id.login_verify_code3);
        code4 = findViewById(R.id.login_verify_code4);

        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneNum.getText().length() == 11) {
                    loginBtn.setClickable(true);
                    loginBtn.setImageResource(R.mipmap.login_btn);
                    deletePhoneNum.setVisibility(View.VISIBLE);
                } else {
                    loginBtn.setClickable(false);
                    loginBtn.setImageResource(R.mipmap.login_btn_un);
                    deletePhoneNum.setVisibility(View.GONE);
                }
            }
        });
        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (verifyCode.getText().length() == 4) {
                    presenter.login(phoneNum.getText().toString(), verifyCode.getText().toString());
                }
                setCodeText(verifyCode.getText().toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_nextbtn:
                presenter.getVerifyCode(phoneNum.getText().toString());
                phoneNum.setEnabled(false);
                break;
            case R.id.login_user_argreenment:
                Intent intent = new Intent(this, H5Activity.class);
                intent.putExtra(URL, Constants.AGREEMENT_URL);
                intent.putExtra(TITLE,"关于我们");
                startActivity(intent);
                break;
            case R.id.login_phone_delete:
                phoneNum.setText("");
                break;
            case R.id.login_verify_time:
                presenter.getVerifyCode(phoneNum.getText().toString());
                phoneNum.setEnabled(false);
                break;
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getVerifySuccess() {
        ToastUtil.showToast("验证码发送成功", false);
        loginLayout.setVisibility(View.GONE);
        verifyLayout.setVisibility(View.VISIBLE);
        MobclickAgent.onEvent(this, Constants.EVENT_VERIFYCODE_PV);
        verifyPhone.setText("+86 " + phoneNum.getText().toString());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                time = 59;
                timeText.setOnClickListener(null);
                timer.schedule(new MyTask(), 1000, 1000);
                timeText.setText(time + "秒后重新获取");
                timeText.setTextColor(getResources().getColor(R.color.login_hint));
            }
        });
    }

    @Override
    public void getVerifyFailed() {
        phoneNum.setEnabled(true);
        timeText.setTextColor(getResources().getColor(R.color.template_editphoto_color_stroke));
        timeText.setText("重新获取");
        timeText.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void loginSuccess() {
        ToastUtil.showToast("登录成功", false);
        MobclickAgent.onEvent(this, Constants.EVENT_LOGIN_SUCCESS);
        finish();
    }

    @Override
    public void loginFailed() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = System.currentTimeMillis();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if (!isFinishing()) {
                if (this == null) {
                    return;
                }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time == 0) {
                            cancel();
                            timeText.setTextColor(getResources().getColor(R.color.template_editphoto_color_stroke));
                            timeText.setText("重新获取");
                            timeText.setOnClickListener(LoginActivity.this);
                        } else {
                            timeText.setText(time + "秒后重新获取");
                            time--;
                        }
                    }
                });
            } else {
                if (this == null) {
                    return;
                }
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (this == null) {
                            cancel();
                            return;
                        }
                        time -= (System.currentTimeMillis() - pauseTime) / 1000;
                        if (time <= 0) {
                            cancel();
                            timeText.setTextColor(getResources().getColor(R.color.template_editphoto_color_stroke));
                            timeText.setText("重新获取");
                            timeText.setOnClickListener(LoginActivity.this);
                        } else {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    timeText.setText(time + "秒后重新获取");
                                }
                            });
                        }
                    }
                });

            }
        }
    }

    private void setCodeText(String codeText) {
        switch (codeText.length()){
            case 1:
                code1.setText(codeText);
                code2.setText("");
                code3.setText("");
                code4.setText("");
                break;
            case 2:
                code1.setText(codeText.substring(0,1));
                code2.setText(codeText.substring(1,2));
                code3.setText("");
                code4.setText("");
                break;
            case 3:
                code1.setText(codeText.substring(0,1));
                code2.setText(codeText.substring(1,2));
                code3.setText(codeText.substring(2,3));
                code4.setText("");
                break;
            case 4:
                code1.setText(codeText.substring(0,1));
                code2.setText(codeText.substring(1,2));
                code3.setText(codeText.substring(2,3));
                code4.setText(codeText.substring(3,4));

                break;
            default:
                code1.setText("");
                code2.setText("");
                code3.setText("");
                code4.setText("");
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_LOGIN_PV);
    }
}
