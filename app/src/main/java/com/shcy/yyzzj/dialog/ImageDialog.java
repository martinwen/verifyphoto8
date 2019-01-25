package com.shcy.yyzzj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;

/**
 * Created by licong on 2018/10/22.
 */

public class ImageDialog extends Dialog {

    private SimpleDraweeView photo;

    public ImageDialog(Context context) {
        super(context, R.style.fn_fullsreen_dialog_tra);
        setContentView(R.layout.dialog_image);
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
