package com.shcy.yyzzj.module.addresslist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.address.AddressBean;
import com.shcy.yyzzj.bean.address.AddressListBean;
import com.shcy.yyzzj.module.addressadd.AddAddressActivity;
import com.shcy.yyzzj.view.view.HeadFootAdapter;
import com.shcy.yyzzj.view.view.recycleview.swipetoloadlayout.SwipeToLoadLayout;

import java.util.List;

import static com.shcy.yyzzj.module.printsubmit.PrintSubmitActivity.ADDRESS_RESULT_CODE;

/**
 * Created by licong on 2018/11/12.
 */

public class AddressActivity extends Activity implements View.OnClickListener, AddressContract.View {

    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;
    private ImageView leftBack;
    private TextView addAddress, tip;
    private HeadFootAdapter adapter;
    private List<AddressBean> list;
    private AddressContract.Presenter presenter;
    private int editPosition = -1;

    public final static String ADDRESSBEAN = "addressbean";
    private final static int REQUESTCODE = 010;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        new AddressPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        recyclerView = findViewById(R.id.swipe_target);
        addAddress = findViewById(R.id.activity_address_button);
        leftBack = findViewById(R.id.activity_address_back);
        tip = findViewById(R.id.activity_address_tip);
        leftBack.setOnClickListener(this);
        addAddress.setOnClickListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        swipeToLoadLayout.setRefreshEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter());
    }

    private void initData() {
        presenter.getAddressList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_address_back:
                finish();
                break;
            case R.id.activity_address_button:
                Intent intent0 = new Intent(this, AddAddressActivity.class);
                startActivityForResult(intent0, REQUESTCODE);
                break;
            case R.id.template_address_edit:
                int pos = (int) v.getTag();
                editPosition = pos;
                if (pos < list.size() && pos >= 0) {
                    Intent intent = new Intent(this, AddAddressActivity.class);
                    intent.putExtra(ADDRESSBEAN, list.get(pos));
                    startActivityForResult(intent, REQUESTCODE);
                }
                break;
            case R.id.template_address_layout:
                int pos1 = (int) v.getTag();
                editPosition = pos1;
                if (pos1 < list.size() && pos1 >= 0) {
                    Intent intent = new Intent();
                    intent.putExtra(ADDRESSBEAN, list.get(pos1));
                    setResult(ADDRESS_RESULT_CODE, intent);
                    finish();
                }
                break;
        }
    }

    private HeadFootAdapter getAdapter() {
        if (adapter == null) {
            adapter = new HeadFootAdapter(this);
            adapter.addItemTemplate(new AddressTemplate(this));
        }
        return adapter;
    }

    @Override
    public void setPresenter(AddressContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAddressList(AddressListBean addressListBean) {
        list = addressListBean.getData();
        if (list == null) {
            return;
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
        if (list.size() == 0) {
            tip.setVisibility(View.VISIBLE);
        } else {
            tip.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            presenter.getAddressList();
        }
    }
}
