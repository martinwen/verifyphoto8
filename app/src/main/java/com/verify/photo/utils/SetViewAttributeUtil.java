package com.verify.photo.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SetViewAttributeUtil {

	private Context mContext;
	private static SetViewAttributeUtil util;

	private SetViewAttributeUtil(Context context) {
		mContext = context.getApplicationContext();
	}

	public static SetViewAttributeUtil getInstance(Context context) {
		if (util == null) {
			util = new SetViewAttributeUtil(context);
		}
		return util;
	}



	public void setBgRes(View mView, int id, int resId) {
		View view = mView.findViewById(id);
		this.setBgRes(view, resId);
	}

	public void setBgRes(View view, int resId) {
		if (view != null) {
			view.setBackgroundResource(resId);
		}
	}

	public void setTxtColorRes(View mView, int id, int resId) {
		View view = mView.findViewById(id);
		this.setTxtColorRes(view, resId);
	}

	public void setTxtColorRes(View view, int resId) {
		if (view != null) {
			((TextView) view).setTextColor(mContext.getResources().getColor(
					resId));
		}
	}

	public void setTxtStateListRes(View view, int resId) {
		if (view != null) {
			((TextView) view).setTextColor(mContext.getResources().getColorStateList(
					resId));
		}
	}

	public void setImageRes(View mView, int id, int resId) {
		ImageView view = (ImageView) mView.findViewById(id);
		view.setImageResource(resId);
	}
	public void setImageRes(ImageView mView,  int resId) {
		mView.setImageResource(resId);
	}

}
