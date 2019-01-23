package com.verify.photo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.verify.photo.R;

public class PhotoSystemDialog extends Dialog implements OnClickListener {

	private OnDialogClickListener onDialogClickListener;
	private TextView dialogContent;
	private TextView dialogTitle;
	private Button btn_confirm;
	public static ImageButton btn_cancel;
	public static boolean isFoucus=true;//控制点击外部和返回键 dialog是否消失

	private PhotoSystemDialog(Context context) {
		super(context, R.style.myDialogTheme);
		this.init();
	}
	
	private void init(){
		setContentView(R.layout.systemdialog);
		btn_confirm = findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		btn_cancel = findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		dialogTitle = findViewById(R.id.title);
		dialogContent = findViewById(R.id.content);
	}
	
	private void setContent(CharSequence text){
		dialogContent.setText(text);
	}
	
	private void setTitleName(CharSequence text){
		dialogTitle.setText(text);
	}
	
	private void setBtnConfirm(CharSequence text){
		btn_confirm.setText(text);
	}
	
	private void setBtnCancel(CharSequence text){
//		btn_cancel.setText(text);
	}
	
	public void onClick(View view){
		
		if(this.onDialogClickListener == null){
			throw new NullPointerException("not found onDialogClickListener");
		}
		switch(view.getId()){
			case R.id.btn_confirm:
				this.onDialogClickListener.confirm();
//				if(isFoucus){
					this.dismiss();
//				}
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
		
		public Builder setConfirmBtn(String text){
			this.confirmBtn = text;
			return this;
		}
		
		public Builder setCancelBtn(String text){
			this.cancelBtn = text;
			return this;
		}


		public PhotoSystemDialog create(){
    		return createConfig(true);
    	}
		public PhotoSystemDialog create(boolean isFoucus){

			return createConfig(isFoucus);
		}
		public PhotoSystemDialog createConfig(boolean isFoucus){
			PhotoSystemDialog dialog = new PhotoSystemDialog(context);
			dialog.setTitleName(title);
			dialog.setContent(message);
			dialog.setBtnConfirm(confirmBtn);
			dialog.setBtnCancel(cancelBtn);
			if(TextUtils.isEmpty(cancelBtn)){
				PhotoSystemDialog.btn_cancel.setVisibility(View.GONE);

			}else{
				PhotoSystemDialog.btn_cancel.setVisibility(View.VISIBLE);
			}
			PhotoSystemDialog.isFoucus =isFoucus;
//			dialog.setCancelable(isFoucus);
			dialog.setCanceledOnTouchOutside(isFoucus);
			dialog.onDialogClickListener = this.onDialogClickListener;
			return dialog;
		}
	}
	
	public interface OnDialogClickListener{
		public void confirm();
		public void cancel();
	}

}
