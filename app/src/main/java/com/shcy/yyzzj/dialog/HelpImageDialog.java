package com.shcy.yyzzj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;
import com.shcy.yyzzj.view.ZoomableDraweeView;

/**
 * Created by licong on 2018/12/18.
 */

public class HelpImageDialog extends Dialog {

    private ZoomableDraweeView photo;

    public HelpImageDialog(Context context) {
        super(context, R.style.fn_fullsreen_dialog_tra);
        setContentView(R.layout.dialog_help_image);
        photo = findViewById(R.id.order_photo_dialog_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showPhotoDetail(String image) {
        if (photo != null) {
            FrescoUtils.getInstance().showImage(photo, image);
        }
    }
}
