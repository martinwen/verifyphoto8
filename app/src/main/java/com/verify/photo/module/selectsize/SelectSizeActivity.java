package com.verify.photo.module.selectsize;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.verify.photo.R;
import com.verify.photo.base.BaseActivity;
import com.verify.photo.config.Constants;
import com.verify.photo.dialog.LodingDialog;
import com.verify.photo.module.camera.CameraActivity;
import com.verify.photo.bean.preview.PreviewPhotoListBean;
import com.verify.photo.bean.size.SelectSizeBean;
import com.verify.photo.module.editphoto.EditPhotoActivity;
import com.verify.photo.module.search.SearchActivity;
import com.verify.photo.utils.PublicUtil;
import com.verify.photo.utils.ToastUtil;
import com.verify.photo.view.view.HeadFootAdapter;
import com.verify.photo.view.view.MultTemplateAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.verify.photo.module.camera.CameraActivity.PREVIEWPHOTOLIST;

/**
 * Created by licong on 2018/10/8.
 */

public class SelectSizeActivity extends BaseActivity implements View.OnClickListener, SelectSizeContract.View {

    private static final String TAG = "选择尺寸";

    public static final String SPECID = "specid";
    public static final String TYPE = "type";
    public static final String IMAGEPATH = "imagepath";
    private TextView nextBtn;
    private ImageView back, moreSpec;
    private RecyclerView recyclerView;
    private LinearLayout searchLayout;
    private HeadFootAdapter adapter;
    private List<SelectSizeBean> list = new ArrayList<>();
    private int specId = 0;
    private String imagePath;
    private int type;//0拍照、1选取相册图片
    private SelectSizeContract.Presenter presenter;

    private LodingDialog lodingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);
        new SelectSizePresenter(this);
        initView();
        initData();
    }

    private void initData() {
        presenter.getSizeList();
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, 0);
        if (type == 1) {
            imagePath = intent.getStringExtra(IMAGEPATH);
        }
    }

    private void initView() {
        lodingDialog = new LodingDialog(this, 1);
        moreSpec = findViewById(R.id.more_spec);
        moreSpec.setOnClickListener(this);
        searchLayout = findViewById(R.id.select_size_searchlayout);
        searchLayout.setOnClickListener(this);
        back = findViewById(R.id.select_size_back);
        back.setOnClickListener(this);
        nextBtn = findViewById(R.id.select_size_next_btn);
        nextBtn.setOnClickListener(this);
        recyclerView = findViewById(R.id.select_size_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter());
        adapter.setOnItemClickListener(new MultTemplateAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(View view) {
                int position = recyclerView.getChildAdapterPosition(view);
                if (position == -1) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIsChecked(0);
                }
                list.get(position).setIsChecked(1);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                specId = list.get(position).getId();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_size_back:
                finish();
                break;
            case R.id.select_size_next_btn:
                if (specId == 0) {
                    ToastUtil.showToast("请选择尺寸！");
                    return;
                }
                if (type == 0) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra(SPECID, specId);
                    startActivity(intent);
                } else {
                    presenter.getPreviewPhoto(PublicUtil.bitmapToString(imagePath), specId + "");
                }
                break;
            case R.id.select_size_searchlayout:
                toSearchActivity();
                break;
            case R.id.more_spec:
                toSearchActivity();
                break;
        }
    }

    private HeadFootAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new SelectSizeTemplate(this));
        }
        return adapter;
    }

    @Override
    public void setPresenter(SelectSizeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showSizeList(List<SelectSizeBean> data) {
        list = data;
        adapter.setList(list);
        adapter.notifyDataSetChanged();
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
        ToastUtil.showToast(message, true);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_SELECTSIZE_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    private void toSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SelectSizeActivity.TYPE, type);
        intent.putExtra(SelectSizeActivity.IMAGEPATH, imagePath);
        startActivity(intent);
    }
}
