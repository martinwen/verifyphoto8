package com.shcy.yyzzj.module.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.base.BaseActivity;
import com.shcy.yyzzj.bean.preview.PreviewPhotoListBean;
import com.shcy.yyzzj.bean.size.SelectSizeBean;
import com.shcy.yyzzj.bean.size.SelectSizeListBean;
import com.shcy.yyzzj.dialog.LodingDialog;
import com.shcy.yyzzj.module.camera.CameraActivity;
import com.shcy.yyzzj.module.editphoto.EditPhotoActivity;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.view.view.HeadFootAdapter;
import com.shcy.yyzzj.view.view.MultTemplateAdapter;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.OnLoadMoreListener;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import static com.shcy.yyzzj.module.camera.CameraActivity.PREVIEWPHOTOLIST;
import static com.shcy.yyzzj.module.selectsize.SelectSizeActivity.*;

/**
 * Created by licong on 2018/12/13.
 */

public class SearchActivity extends BaseActivity implements SearchContract.View, View.OnClickListener, OnLoadMoreListener {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ImageView back;
    private TextView searchButton, nodata;
    private EditText searchEdit;
    private LodingDialog lodingDialog;
    private HeadFootAdapter adapter;
    private List<SelectSizeBean> list = new ArrayList<>();
    private int clickPosition;
    private SearchContract.Presenter presenter;

    private int pageNo = 1;

    private int specId = 0;
    private String imagePath;
    private int type;//0拍照、1选取相册图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new SearchPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        recyclerView = findViewById(R.id.swipe_target);
        back = findViewById(R.id.search_back);
        back.setOnClickListener(this);
        searchEdit = findViewById(R.id.search_searedit);
        searchButton = findViewById(R.id.search_searbutton);
        searchButton.setOnClickListener(this);
        nodata = findViewById(R.id.search_nodata);
        swipeToLoadLayout.setRefreshEnabled(false);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter());
        swipeToLoadLayout.setOnLoadMoreListener(this);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pageNo = 1;
                presenter.getSearchData(searchEdit.getText().toString().trim(), pageNo);
            }
        });
        adapter.setOnItemClickListener(new MultTemplateAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(View view) {
                int pos = recyclerView.getChildAdapterPosition(view);
                if (pos == -1) {
                    return;
                }
                if (pos < list.size() && list.get(pos).getIsChecked() == 0) {
                    list.get(clickPosition).setIsChecked(0);
                    list.get(pos).setIsChecked(1);
                    adapter.notifyDataSetChanged();
                    clickPosition = pos;
                    specId = list.get(pos).getId();
                }
            }
        });
    }

    private void initData() {
        adapter.setList(list);
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, 0);
        if (type == 1) {
            imagePath = intent.getStringExtra(IMAGEPATH);
        }
        presenter.getSearchData("", pageNo);
    }

    private HeadFootAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new SearchTemplate(this));
        }
        return adapter;
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showSearchData(SelectSizeListBean selectSizeListBean) {
        swipeToLoadLayout.setLoadingMore(false);
        if (selectSizeListBean.getData() == null) {
            return;
        }
        if (selectSizeListBean.getNextCursor() != 0) {
            pageNo++;
            swipeToLoadLayout.setLoadMoreEnabled(true);
        } else {
            swipeToLoadLayout.setLoadMoreEnabled(false);
            adapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.footview_nodata, null));
        }
        if (pageNo == 1) {
            list.clear();
            adapter.removeAllFooter();
            specId = 0;
            clickPosition = 0;
        }
        list.addAll(selectSizeListBean.getData());
        if (list.size() == 0){
            nodata.setVisibility(View.VISIBLE);
        }else {
            nodata.setVisibility(View.GONE);
        }
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
    public void getSearchDataFailed() {
        swipeToLoadLayout.setLoadingMore(false);
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

    public void pHideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != imm) {
            View focus = getCurrentFocus();
            if (null != focus && null != focus.getWindowToken()) {
                imm.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void pShowSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_searbutton:
                if (specId == 0) {
                    ToastUtil.showToast("请选择尺寸");
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
            case R.id.search_back:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMore() {
        presenter.getSearchData(searchEdit.getText().toString().trim(), pageNo);
    }
}
