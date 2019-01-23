package com.verify.photo.module.selectsize;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.verify.photo.R;
import com.verify.photo.bean.size.SelectSizeBean;
import com.verify.photo.utils.fresco.FrescoUtils;
import com.verify.photo.view.view.BaseItemTempalte;
import com.verify.photo.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/10/8.
 */

public class SelectSizeTemplate extends BaseItemTempalte {
    Context context;
    public SelectSizeTemplate(Context context){
        this.context = context;
    }

    @Override
    public int getViewId() {
        return R.layout.template_select_size;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        SimpleDraweeView icon = holder.getView(R.id.select_size_item_icon);
        TextView name = holder.getView(R.id.select_size_item_name);
        TextView instruction = holder.getView(R.id.select_size_item_instruction);
        LinearLayout layout = holder.getView(R.id.select_size_itemlayout);
        SelectSizeBean bean = (SelectSizeBean) list.get(position);
        FrescoUtils.getInstance().showImage(icon,bean.getIcon());
        name.setText(bean.getName());
        instruction.setText(bean.getInstruction());
        if (bean.getIsChecked() == 0){
            layout.setBackground(context.getDrawable(R.drawable.select_size_back_unselected));
        }else {
            layout.setBackground(context.getDrawable(R.drawable.select_size_back));
        }
    }
}
