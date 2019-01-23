package com.verify.photo.module.help;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.verify.photo.R;
import com.verify.photo.bean.help.HelpBean;
import com.verify.photo.bean.help.HelpListBean;
import com.verify.photo.dialog.HelpImageDialog;
import com.verify.photo.view.view.HeadFootAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licong on 2018/12/17.
 */

public class HelpActivity extends Activity implements HelpContract.View, View.OnClickListener {

    private HelpContract.Presenter presenter;
    private RecyclerView recyclerView;
    private ImageView back;
    private HeadFootAdapter adapter;
    private List<HelpBean> list = new ArrayList<>();
    private HelpImageDialog helpImageDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        new HelpPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        back = findViewById(R.id.help_back);
        recyclerView = findViewById(R.id.help_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter());
        adapter.setList(list);
        back.setOnClickListener(this);
        helpImageDialog = new HelpImageDialog(this);
    }

    private void initData() {
        presenter.getHelpData();
    }

    @Override
    public void setPresenter(HelpContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showHelpData(HelpListBean helpListBean) {
        if (helpListBean != null && helpListBean.getHelpList() != null) {
            list.addAll(helpListBean.getHelpList());
            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
    }

    public HeadFootAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new HelpTemplate(this));
        }
        return adapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_back:
                finish();
                break;
            case R.id.template_help_questionlayout:
                int pos = (int) v.getTag();
                if (pos >= 0 && pos < list.size()) {
                    list.get(pos).setIsopen(!list.get(pos).getIsopen());
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.template_help_image1:
                String image1 = (String) v.getTag();
                showImage(image1);
                break;
            case R.id.template_help_image2:
                String image2 = (String) v.getTag();
                showImage(image2);
                break;
            case R.id.template_help_image3:
                String image3 = (String) v.getTag();
                showImage(image3);
                break;
        }
    }

    private void showImage(String url) {
        if (helpImageDialog != null) {
            helpImageDialog.showPhotoDetail(url);
            helpImageDialog.show();
        }
    }
}
