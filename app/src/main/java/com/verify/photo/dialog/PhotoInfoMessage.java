package com.verify.photo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.verify.photo.R;


public class PhotoInfoMessage extends Dialog
{

    public PhotoInfoMessage(Context context)
    {
        super(context, R.style.fk_nomal_dialog);
        setContentView(R.layout.photo_info_message);
    }

    public void setTitle(String title)
    {
        TextView text = (TextView) findViewById(R.id.jm_t);
        text.setText(title);
    }

}
