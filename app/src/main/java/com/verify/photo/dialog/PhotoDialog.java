package com.verify.photo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.verify.photo.R;


public class PhotoDialog extends Dialog implements OnClickListener {

    private OnDialogClickListener onDialogClickListener;
    private TextView dialogContent;
    private Button btn_confirm;
    public static Button btn_cancel;
    public static boolean isFoucus = true;//控制点击外部和返回键 dialog是否消失

    private PhotoDialog(Context context) {
        super(context, R.style.myDialogTheme);
        this.init();
    }

    private void init() {
        setContentView(R.layout.dialog);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        dialogContent = findViewById(R.id.content);
    }

    private void setContent(CharSequence text) {
        dialogContent.setText(text);
    }

    private void setTitleName(CharSequence text) {
    }

    private void setConfirmTextcolor() {
        btn_confirm.setTextColor(getContext().getResources().getColor(R.color.white));
    }

    private void setBtnConfirm(CharSequence text) {
        btn_confirm.setText(text);
    }

    private void setBtnCancel(CharSequence text) {
        btn_cancel.setText(text);
    }

    public void onClick(View view) {

        if (this.onDialogClickListener == null) {
            throw new NullPointerException("not found onDialogClickListener");
        }
        switch (view.getId()) {
            case R.id.btn_confirm:
                this.onDialogClickListener.confirm();
                this.dismiss();
                break;

            case R.id.btn_cancel:
                this.onDialogClickListener.cancel();
                this.dismiss();
                break;
            default:
        }
    }

    public static class Builder {

        private Context context;
        private String title;
        private String confirmBtn;
        private String cancelBtn;
        private CharSequence message;
        private OnDialogClickListener onDialogClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitleName(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence text) {
            this.message = text;
            return this;
        }

        public Builder setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
            this.onDialogClickListener = onDialogClickListener;
            return this;
        }

        public Builder setConfirmBtn(String text) {
            this.confirmBtn = text;
            return this;
        }

        public Builder setCancelBtn(String text) {
            this.cancelBtn = text;
            return this;
        }

        public PhotoDialog create() {
            return createConfig(true);
        }

        public PhotoDialog create(boolean isFoucus) {

            return createConfig(isFoucus);
        }

        public PhotoDialog createSetBtnTextColor(boolean isFoucus) {

            return createConfigSetBtnTextColor(isFoucus);
        }

        public PhotoDialog createConfig(boolean isFoucus) {
            PhotoDialog dialog = new PhotoDialog(context);
            dialog.setTitleName(title);
            dialog.setContent(message);
            dialog.setBtnConfirm(confirmBtn);
            dialog.setBtnCancel(cancelBtn);
            if (TextUtils.isEmpty(cancelBtn)) {
                PhotoDialog.btn_cancel.setVisibility(View.GONE);
            } else {
                PhotoDialog.btn_cancel.setVisibility(View.VISIBLE);
            }
            PhotoDialog.isFoucus = isFoucus;
            dialog.setCanceledOnTouchOutside(isFoucus);
            dialog.onDialogClickListener = this.onDialogClickListener;
            return dialog;
        }

        public PhotoDialog createConfigSetBtnTextColor(boolean isFoucus) {
            PhotoDialog dialog = new PhotoDialog(context);
            dialog.setTitleName(title);
            dialog.setContent(message);
            dialog.setBtnConfirm(confirmBtn);
            dialog.setBtnCancel(cancelBtn);
            dialog.setConfirmTextcolor();
            if (TextUtils.isEmpty(cancelBtn)) {
                PhotoDialog.btn_cancel.setVisibility(View.GONE);
            } else {
                PhotoDialog.btn_cancel.setVisibility(View.VISIBLE);
            }
            PhotoDialog.isFoucus = isFoucus;
            dialog.setCanceledOnTouchOutside(isFoucus);
            dialog.onDialogClickListener = this.onDialogClickListener;
            return dialog;
        }
    }

    public interface OnDialogClickListener {
        public void confirm();

        public void cancel();
    }

}
