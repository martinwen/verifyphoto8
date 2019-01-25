package com.shcy.yyzzj.module.search;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.size.SelectSizeBean;
import com.shcy.yyzzj.view.view.BaseItemTempalte;
import com.shcy.yyzzj.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/12/13.
 */

public class SearchTemplate extends BaseItemTempalte {

    private Context context;

    public SearchTemplate(Context context) {
        this.context = context;
    }

    @Override
    public int getViewId() {
        return R.layout.template_search;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        SelectSizeBean bean = (SelectSizeBean) list.get(position);

        View rootview = holder.getContentView();
        TextView name = holder.getView(R.id.template_search_name);
        TextView spec = holder.getView(R.id.template_search_spec);
//        TextView tips = holder.getView(R.id.template_search_tips);
//        ImageView arrow = holder.getView(R.id.template_search_arrow);

        name.setText(bean.getName());
        spec.setText(bean.getInstruction());
//        tips.setText(bean.getTips());

        if (bean.getIsChecked() == 0) {
            rootview.setBackgroundColor(context.getResources().getColor(R.color.white));
//            arrow.setImageResource(R.mipmap.template_search_arrow_right);
//            tips.setVisibility(View.GONE);
        } else {
            rootview.setBackgroundColor(context.getResources().getColor(R.color.template_search_selected_backgroud));
//            if (!TextUtils.isEmpty(bean.getTips())) {
//                arrow.setImageResource(R.mipmap.template_search_arrow_down);
//                tips.setVisibility(View.VISIBLE);
//            } else {
//                arrow.setImageResource(R.mipmap.template_search_arrow_right);
//                tips.setVisibility(View.GONE);
//            }
        }
    }
}
