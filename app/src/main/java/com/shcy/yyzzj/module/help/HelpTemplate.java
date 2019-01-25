package com.shcy.yyzzj.module.help;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.help.HelpBean;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;
import com.shcy.yyzzj.view.view.BaseItemTempalte;
import com.shcy.yyzzj.view.view.ViewHolder;

import java.util.List;

/**
 * Created by licong on 2018/12/17.
 */

public class HelpTemplate extends BaseItemTempalte {

    private View.OnClickListener listener;

    public HelpTemplate(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getViewId() {
        return R.layout.template_help;
    }

    @Override
    public void convert(ViewHolder holder, int position, List list) {
        LinearLayout questionlayout = holder.getView(R.id.template_help_questionlayout);
        TextView question = holder.getView(R.id.template_help_question);
        ImageView arrow = holder.getView(R.id.template_help_arrow);
        LinearLayout answerlayout = holder.getView(R.id.template_help_answerlayout);
        TextView answer = holder.getView(R.id.template_help_answer);
        LinearLayout imagelayout = holder.getView(R.id.template_help_imagelayout);
        SimpleDraweeView image1 = holder.getView(R.id.template_help_image1);
        SimpleDraweeView image2 = holder.getView(R.id.template_help_image2);
        SimpleDraweeView image3 = holder.getView(R.id.template_help_image3);

        final HelpBean helpBean = (HelpBean) list.get(position);

        question.setText(helpBean.getQuestion());
        answer.setText(helpBean.getAnswer());
        questionlayout.setTag(position);
        questionlayout.setOnClickListener(listener);
        if (helpBean.getIsopen()) {
            answerlayout.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.mipmap.template_search_arrow_down);
        } else {
            answerlayout.setVisibility(View.GONE);
            arrow.setImageResource(R.mipmap.template_search_arrow_right);
        }

        if (TextUtils.isEmpty(helpBean.getImages())) {
            imagelayout.setVisibility(View.GONE);
            image1.setOnClickListener(null);
            image2.setOnClickListener(null);
            image3.setOnClickListener(null);
        } else {
            imagelayout.setVisibility(View.VISIBLE);
            String[] images = helpBean.getImages().split(",");
            image1.setOnClickListener(null);
            image2.setOnClickListener(null);
            image3.setOnClickListener(null);
            switch (images.length) {
                case 0:
                    imagelayout.setVisibility(View.GONE);
                    break;
                case 1:
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.GONE);
                    image3.setVisibility(View.GONE);
                    FrescoUtils.getInstance().showImage(image1, images[0]);
                    image1.setOnClickListener(listener);
                    image1.setTag(images[0]);
                    break;
                case 2:
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    image3.setVisibility(View.GONE);
                    FrescoUtils.getInstance().showImage(image1, images[0]);
                    FrescoUtils.getInstance().showImage(image2, images[1]);
                    image1.setOnClickListener(listener);
                    image1.setTag(images[0]);
                    image2.setOnClickListener(listener);
                    image2.setTag(images[1]);
                    break;
                default:
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    image3.setVisibility(View.VISIBLE);
                    FrescoUtils.getInstance().showImage(image1, images[0]);
                    FrescoUtils.getInstance().showImage(image2, images[1]);
                    FrescoUtils.getInstance().showImage(image3, images[2]);
                    image1.setOnClickListener(listener);
                    image1.setTag(images[0]);
                    image2.setOnClickListener(listener);
                    image2.setTag(images[1]);
                    image3.setOnClickListener(listener);
                    image3.setTag(images[2]);
                    break;

            }
        }
    }
}
