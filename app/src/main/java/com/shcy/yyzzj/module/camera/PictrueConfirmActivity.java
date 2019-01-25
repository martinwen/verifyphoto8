package com.shcy.yyzzj.module.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.module.editphoto.EditPhotoActivity;
import com.shcy.yyzzj.module.selectsize.SelectSizeActivity;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;

import static com.shcy.yyzzj.module.camera.CameraActivity.PREVIEWPHOTOLIST;
import static com.shcy.yyzzj.module.selectsize.SelectSizeActivity.*;

/**
 * Created by licong on 2018/12/14.
 */

public class PictrueConfirmActivity extends BaseActivity implements View.OnClickListener, CameraContract.View {

    public static final String PICTRUECONFIRM_IMAGEPATH = "pictrueconfirm_imagepath";
    private ImageView connfirm, cancel;
    private CameraContract.Presenter presenter;
    private String imagePath;
    private String specid;
    private int type;
    private SimpleDraweeView photo;
    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictrueconfirm);
        new CameraPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        photo = findViewById(R.id.pictruecancelconnfirm_image);
        connfirm = findViewById(R.id.pictrueconfirm);
        cancel = findViewById(R.id.pictruecancel);
        cancel.setOnClickListener(this);
        connfirm.setOnClickListener(this);
        lodingDialog = new LodingDialog(this, 1);
    }

    private void initData() {
        type = getIntent().getIntExtra(TYPE, 0);
        imagePath = getIntent().getStringExtra(IMAGEPATH);
        if (type == 0) {
            specid = getIntent().getStringExtra(SPECID);
        }
        FrescoUtils.getInstance().showImageFile(photo, imagePath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pictruecancel:
                finish();
                break;
            case R.id.pictrueconfirm:
                if (presenter == null) {
                    return;
                }
                presenter.getPreviewStatus();
                break;
        }
    }

    @Override
    public void setPresenter(CameraContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPreviewPhoto(PreviewPhotoListBean listBean) {
        if (listBean.getPhotoList() != null && listBean.getPhotoList().size() != 0) {
            Intent intent = new Intent(this, EditPhotoActivity.class);
            intent.putExtra(PREVIEWPHOTOLIST, listBean);
            startActivity(intent);
        }
    }

    @Override
    public void getPreViewPhotoError(String message) {
        DialogUtil.showCheckFaceDialog(this, new PhotoDialog.OnDialogClickListener() {
            @Override
            public void confirm() {
                finish();
            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void gotPreviewStatus() {
        if (type == 0) {
            presenter.getPreviewPhoto(PublicUtil.bitmapToString(imagePath), specid);
        } else {
            Intent intent = new Intent(this, SelectSizeActivity.class);
            intent.putExtra(SelectSizeActivity.TYPE, 1);
            intent.putExtra(SelectSizeActivity.IMAGEPATH, imagePath);
            startActivity(intent);
        }
    }

    @Override
    public void loading() {
        if (lodingDialog != null && !lodingDialog.isShowing()) {
            lodingDialog.show();
        }
    }

    @Override
    public void loadingEnd() {
        if (lodingDialog != null && lodingDialog.isShowing()) {
            lodingDialog.dismiss();
        }
    }
}
