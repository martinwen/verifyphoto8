package com.shcy.yyzzj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.utils.DpPxUtils;

public class LodingDialog extends Dialog {
    private ImageView mImageView;
    private TextView loadingText;
    private RelativeLayout layout;
    private int type = 0;//0 普通loding  1 制作证件照loading

    /**
     * @param context 上下文对象
     */
    public LodingDialog(Context context) {
        super(context, R.style.fk_nomal_dialog);
        setContentView(R.layout.dialog_loding);
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
        setCancelable(true);
        getWindow().setDimAmount(0f);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    /**
     * @param context 上下文对象
     */
    public LodingDialog(Context context, int type) {
        super(context, R.style.fk_nomal_dialog);
        setContentView(R.layout.dialog_loding);
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
        setCancelable(true);
        getWindow().setDimAmount(0f);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageView = findViewById(R.id.iv_loding);
        loadingText = findViewById(R.id.loading_text);
        layout = findViewById(R.id.loding_layout);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        if (type == 1) {
            Glide.with(getContext()).load(R.drawable.loading_anim).into(mImageView);
            loadingText.setVisibility(View.VISIBLE);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            params.width = DpPxUtils.dp2px(getContext(), 100);
            params.height = DpPxUtils.dp2px(getContext(), 100);
            layout.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
            params1.width = DpPxUtils.dp2px(getContext(), 50);
            params1.height = DpPxUtils.dp2px(getContext(), 50);
            mImageView.setLayoutParams(params1);
            Glide.with(getContext()).load(R.drawable.loading_anim1).into(mImageView);
            loadingText.setVisibility(View.GONE);
        }
    }
}

