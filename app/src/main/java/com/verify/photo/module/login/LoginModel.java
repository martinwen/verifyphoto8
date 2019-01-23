package com.verify.photo.module.login;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.TextUtils;

import com.verify.photo.activity.MyApplication;
import com.verify.photo.bean.AliasTagsBean;
import com.verify.photo.bean.login.LoginBean;
import com.verify.photo.bean.login.User;
import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.config.Constants;
import com.verify.photo.log.L;
import com.verify.photo.retrofit.PhotoHttpManger;
import com.verify.photo.retrofit.callback.HttpResult;
import com.verify.photo.retrofit.callback.ResultSub;
import com.verify.photo.retrofit.exception.NetException;
import com.verify.photo.utils.LoadDataPostJsonObject;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.SetUtils;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.utils.UserUtil;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by licong on 2018/10/11.
 */

public class LoginModel {
    public interface LoginCallBack {
        void onSuccess();

        void onFailed();
    }

    public void getVerifyCode(String mobile, final LoginCallBack callBack) {
        PhotoHttpManger.getPhotoApi().getVerifyCode(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("mobile"), mobile))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<ResultBean>() {
                    @Override
                    public void onSuccsess(HttpResult<ResultBean> data) {
                        if (data.isSucess()) {
                            callBack.onSuccess();
                        } else {
                            callBack.onFailed();
                            ToastUtil.showToast(data.getMessage(), true);
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callBack.onFailed();
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }

    public void login(String mobile, String verifyCode, final LoginCallBack callback) {
        PhotoHttpManger.getPhotoApi().login(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("mobile", "verifyCode"), mobile, verifyCode))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<LoginBean>() {
                    @Override
                    public void onSuccsess(HttpResult<LoginBean> data) {
                        if (data.isSucess()) {
                            LoginBean bean = data.getData();
                            Constants.TOKEN = bean.getAccessToken();
                            User user = bean.getUser();
                            SetUtils.getInstance().setIsLogin(true);
                            SetUtils.getInstance().setToken(bean.getAccessToken());
                            UserUtil.getInstance().setUserNickName(user.getNickname());
                            UserUtil.getInstance().setAvatar(user.getAvatar());
                            UserUtil.getInstance().setLoginStatus(bean.getLoginStatus());
                            UserUtil.getInstance().setGender(user.getGender());
                            UserUtil.getInstance().setId(user.getId());
                            callback.onSuccess();
                            setJPushAliasTags("verify_" + UserUtil.getInstance().geId());
                        } else {
                            callback.onFailed();
                            ToastUtil.showToast(data.getMessage());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        callback.onFailed();
                        ToastUtil.showToast(Constants.NETERROR);
                    }
                });
    }

    public void login(final LoginCallBack callBack) {
        PhotoHttpManger.getPhotoApi().login(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("mobile", "verifyCode"), "", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<LoginBean>() {
                    @Override
                    public void onSuccsess(HttpResult<LoginBean> data) {
                        if (data.isSucess()) {
                            LoginBean bean = data.getData();
                            User user = bean.getUser();
                            Constants.TOKEN = bean.getAccessToken();
                            SetUtils.getInstance().setToken(bean.getAccessToken());

                            UserUtil.getInstance().setUserNickName(user.getNickname());
                            UserUtil.getInstance().setAvatar(user.getAvatar());
                            UserUtil.getInstance().setLoginStatus(bean.getLoginStatus());
                            UserUtil.getInstance().setGender(user.getGender());
                            UserUtil.getInstance().setId(user.getId());
                            setJPushAliasTags("verify_" + UserUtil.getInstance().geId());
                            callBack.onSuccess();
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }
    public void login() {
        PhotoHttpManger.getPhotoApi().login(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("mobile", "verifyCode"), "", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<LoginBean>() {
                    @Override
                    public void onSuccsess(HttpResult<LoginBean> data) {
                        if (data.isSucess()) {
                            LoginBean bean = data.getData();
                            User user = bean.getUser();
                            Constants.TOKEN = bean.getAccessToken();
                            SetUtils.getInstance().setToken(bean.getAccessToken());

                            UserUtil.getInstance().setUserNickName(user.getNickname());
                            UserUtil.getInstance().setAvatar(user.getAvatar());
                            UserUtil.getInstance().setLoginStatus(bean.getLoginStatus());
                            UserUtil.getInstance().setGender(user.getGender());
                            UserUtil.getInstance().setId(user.getId());
                            setJPushAliasTags("verify_" + UserUtil.getInstance().geId());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }

    public void loadUserData() {
        PhotoHttpManger.getPhotoApi().loadUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<User>() {
                    @Override
                    public void onSuccsess(HttpResult<User> data) {
                        if (data.isSucess()) {
                            User user = data.getData();
                            UserUtil.getInstance().setUserNickName(user.getNickname());
                            UserUtil.getInstance().setAvatar(user.getAvatar());
                            UserUtil.getInstance().setLoginStatus(user.getLoginStatus());
                            UserUtil.getInstance().setGender(user.getGender());
                            UserUtil.getInstance().setId(user.getId());
                            setJPushAliasTags("verify_" + UserUtil.getInstance().geId());
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        ToastUtil.showToast(Constants.NETERROR, true);
                    }
                });
    }


    // Push的设置别名,一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setJPushAliasTags(String alias) {
        if (TextUtils.isEmpty(alias)) {
            L.e("别名为空");
            return;
        }
        if (!PublicUtil.isValidTagAndAlias(alias)) {
            L.e("别名不合法");
            return;
        }
        AliasTagsBean aliasTagsBean = new AliasTagsBean();
        aliasTagsBean.setAlias(alias);
        Set<String> tags = new HashSet<>();
        tags.add(PublicUtil.getVersionCode(MyApplication.getContext()));
        tags.add(PublicUtil.getBuildTpe(MyApplication.getContext()));
        aliasTagsBean.setTags(tags);
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS_TAGS, aliasTagsBean));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    break;
                case 6002:
                    // 延迟 60 秒来调用 Handler 设置别名
                    AliasTagsBean aliasTagsBean = new AliasTagsBean();
                    aliasTagsBean.setTags(tags);
                    aliasTagsBean.setAlias(alias);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS_TAGS, aliasTagsBean), 1000 * 60);
                    break;
                default:
                    break;
            }
        }
    };

    private static final int MSG_SET_ALIAS_TAGS = 1001;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS_TAGS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(MyApplication.getContext(), ((AliasTagsBean) msg.obj).getAlias(), ((AliasTagsBean) msg.obj).getTags(), mAliasCallback);
                    break;
                default:
            }
        }
    };


}
