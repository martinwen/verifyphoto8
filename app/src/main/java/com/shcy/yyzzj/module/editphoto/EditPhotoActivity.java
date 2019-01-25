package com.shcy.yyzzj.module.editphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.preview.PreviewPhotoBean;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.preview.PreviewPrintPhotoBean;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.module.preview.PreviewActivity;
import com.shcy.yyzzj.retrofit.PhotoHttpManger;
import com.shcy.yyzzj.retrofit.callback.HttpResult;
import com.shcy.yyzzj.retrofit.callback.ResultSub;
import com.shcy.yyzzj.retrofit.exception.NetException;
import com.shcy.yyzzj.utils.LoadDataPostJsonObject;
import com.shcy.yyzzj.utils.fresco.FrescoUtils;
import com.shcy.yyzzj.view.view.HeadFootAdapter;
import com.shcy.yyzzj.view.view.HorizontalPageLayoutManager;
import com.shcy.yyzzj.view.view.MultTemplateAdapter;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.shcy.yyzzj.module.camera.CameraActivity.PREVIEWPHOTOLIST;

/**
 * Created by licong on 2018/10/9.
 */

public class EditPhotoActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "编辑图片";

    public static final String PREVIEWPHOTOBEAN = "previewphotobean";
    public static final String PREVIEW_PRINT_PHOTO_BEAM = "preview_print_photo_bean";
    private ImageView back;
    private TextView next;
    private SimpleDraweeView photo;
    private RecyclerView recyclerView;
    private HeadFootAdapter adapter;
    private List<PreviewPhotoBean> list;
    private PreviewPhotoBean bean;
    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_edit_photo);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        list = ((PreviewPhotoListBean) intent.getSerializableExtra(PREVIEWPHOTOLIST)).getPhotoList();
        FrescoUtils.getInstance().showImage(photo,list.get(0).getPhotoUrl());
        list.get(0).setChekedStatus(1);
        bean = list.get(0);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        back = findViewById(R.id.editphoto_back);
        next = findViewById(R.id.editphoto_next);
        photo = findViewById(R.id.editphoto_photo);
        recyclerView = findViewById(R.id.editphoto_color);
        recyclerView.setLayoutManager(new HorizontalPageLayoutManager(1,6));
        recyclerView.setAdapter(getAdapter());
        adapter.setOnItemClickListener(new MultTemplateAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                if (position<list.size()){
                    for (int i = 0;i<list.size();i++){
                        list.get(i).setChekedStatus(0);
                    }
                    list.get(position).setChekedStatus(1);
                    adapter.notifyDataSetChanged();
                    FrescoUtils.getInstance().showImage(photo,list.get(position).getPhotoUrl());
                    bean = list.get(position);
                }
            }
        });

        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editphoto_back:
                finish();
                break;
            case R.id.editphoto_next:
                getPrintPhoto(bean.getPhotoNumber());
                break;
        }
    }

    private void loading() {
        if (lodingDialog != null && !lodingDialog.isShowing()) {
            lodingDialog.show();
        }
    }

    private void loadingEnd() {
        if (lodingDialog != null && lodingDialog.isShowing()) {
            lodingDialog.dismiss();
        }
    }

    private HeadFootAdapter getAdapter(){
        if (adapter == null){
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new EditPhotoColorTemplate(this));
        }
        return adapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    private void getPrintPhoto(String photoNumber) {
        loading();
        PhotoHttpManger.getPhotoApi().getPreviewPrintPhoto(LoadDataPostJsonObject.getInstance().GetStringJsonObj(LoadDataPostJsonObject.getInstance().GetStringToList("photoNumber"), photoNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultSub<PreviewPrintPhotoBean>() {
                    @Override
                    public void onSuccsess(HttpResult<PreviewPrintPhotoBean> data) {
                        loadingEnd();
                        if (data.isSucess()) {
                            Intent intent = new Intent(EditPhotoActivity.this, PreviewActivity.class);
                            intent.putExtra(PREVIEWPHOTOBEAN, bean);
                            intent.putExtra(PREVIEW_PRINT_PHOTO_BEAM,data.getData());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFilad(NetException e) {
                        loadingEnd();
                    }
                });
    }
}
