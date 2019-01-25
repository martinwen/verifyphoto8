package com.shcy.yyzzj.module.editphoto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.preview.PreviewPhotoBean;
import com.shcy.yyzzj.utils.DpPxUtils;
import com.shcy.yyzzj.view.view.BaseItemTempalte;
import com.shcy.yyzzj.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/10/9.
 */

public class EditPhotoColorTemplate extends BaseItemTempalte {

    private Context context;
    public EditPhotoColorTemplate(Context context){
        this.context = context;
    }

    @Override
    public int getViewId() {
        return R.layout.template_editphoto_color;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        PreviewPhotoBean bean = (PreviewPhotoBean) list.get(position);
        View color = holder.getView(R.id.template_editphoto_color);
        if (bean.getChekedStatus() == 0){
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(0,context.getResources().getColor(R.color.template_editphoto_color_stroke));
            drawable.setColor(Color.parseColor(bean.getColorTone()));
            drawable.setCornerRadius(DpPxUtils.dp2px(context,15));
            color.setBackground(drawable);
        }else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setStroke(DpPxUtils.dp2px(context,3),context.getResources().getColor(R.color.template_editphoto_color_stroke));
            drawable.setColor(Color.parseColor(bean.getColorTone()));
            drawable.setCornerRadius(DpPxUtils.dp2px(context,15));
            color.setBackground(drawable);
        }
    }
}
