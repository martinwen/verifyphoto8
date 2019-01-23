package com.verify.photo.module.addressadd;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.verify.photo.R;
import com.verify.photo.bean.address.AddressBean;
import com.verify.photo.bean.address.ProvinceBean;
import com.verify.photo.bean.login.ResultBean;
import com.verify.photo.dialog.LodingDialog;
import com.verify.photo.dialog.PhotoDialog;
import com.verify.photo.utils.DialogUtil;
import com.verify.photo.utils.FileJsonManager;
import com.verify.photo.utils.SetUtils;
import com.verify.photo.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.verify.photo.module.addresslist.AddressActivity.ADDRESSBEAN;


public class AddAddressActivity extends Activity implements View.OnClickListener, AddAddressContract.View {

    private LinearLayout nameLayout, mobileLayout, addresslayout, detailAddressLayout;
    private TextView address, saveButton, delete;
    private ImageView back;
    private EditText name, mobile, detailAddress;
    private LodingDialog lodingDialog;
    private AddAddressContract.Presenter presenter;
    private AddressBean addressBean;
    private int areaId = -1;

    private List<ProvinceBean> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        new AddAddressPresenter(this);
        handler = new MyHandler(this);
        initView();
        initData();
    }

    private void initData() {
        addressBean = (AddressBean) getIntent().getSerializableExtra(ADDRESSBEAN);
        if (addressBean != null) {
            delete.setVisibility(View.VISIBLE);
            areaId = addressBean.getAreaId();
            name.setText(addressBean.getRecipientsName());
            mobile.setText(addressBean.getRecipientsMobile());
            detailAddress.setText(addressBean.getDetailedAddress());
            address.setText(addressBean.getProvince() + addressBean.getCity() + addressBean.getDistrict());
        }
        if (TextUtils.equals("0", SetUtils.getInstance().getProvinceDownLoadTime()) || (System.currentTimeMillis() - Long.parseLong(SetUtils.getInstance().getProvinceDownLoadTime())) > 1000 * 60 * 60 * 24 * 30 * 6) {
            presenter.getAreaData();
        }
    }

    private void initView() {
        lodingDialog = new LodingDialog(this);
        nameLayout = findViewById(R.id.addaddress_name_layout);
        mobileLayout = findViewById(R.id.addaddress_mobile_layout);
        addresslayout = findViewById(R.id.addaddress_address_layout);
        detailAddressLayout = findViewById(R.id.addaddress_detailaddress_layout);
        name = findViewById(R.id.addaddress_name);
        mobile = findViewById(R.id.addaddress_mobile);
        address = findViewById(R.id.addaddress_address);
        detailAddress = findViewById(R.id.addaddress_detailaddress);
        saveButton = findViewById(R.id.addaddress_save);
        delete = findViewById(R.id.addaddress_delete);
        back = findViewById(R.id.addaddress_back);

        nameLayout.setOnClickListener(this);
        mobileLayout.setOnClickListener(this);
        addresslayout.setOnClickListener(this);
        detailAddressLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addaddress_name_layout:
                name.performClick();
                break;
            case R.id.addaddress_mobile_layout:
                mobile.performClick();
                break;
            case R.id.addaddress_address_layout:
                pHideSoftInput();
                initAreaPicker();
                break;
            case R.id.addaddress_detailaddress_layout:
                detailAddress.performClick();
                break;
            case R.id.addaddress_save:
                if (addressBean == null) {
                    presenter.addAddress(areaId, detailAddress.getText().toString().trim(), name.getText().toString().trim(), mobile.getText().toString().trim());
                } else {
                    presenter.changeAddress(addressBean.getId(), areaId, detailAddress.getText().toString().trim(), name.getText().toString().trim(), mobile.getText().toString().trim());
                }
                break;
            case R.id.addaddress_delete:
                DialogUtil.showDeleteAddressDialog(this, new PhotoDialog.OnDialogClickListener() {
                    @Override
                    public void confirm() {
                        if (addressBean != null) {
                            presenter.deleteAddress(addressBean.getId());
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.addaddress_back:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(AddAddressContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void saveAddressSuccess(ResultBean bean) {
        ToastUtil.showToast(bean.getMsg(), false);
        finish();
    }

    @Override
    public void showAreaData(List<ProvinceBean> list) {

        FileJsonManager.saveJsonObject(this, FileJsonManager.PROVINCE_JSON, list);
    }

    private void initAreaPicker() {
        options1Items = FileJsonManager.getJsonArray(this, FileJsonManager.PROVINCE_JSON, ProvinceBean.class);

        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
            List<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < options1Items.get(i).getNodes().size(); c++) {//遍历该省份的所有城市
                String CityName = options1Items.get(i).getNodes().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (options1Items.get(i).getNodes().get(c).getNodes() == null
                        || options1Items.get(i).getNodes().get(c).getNodes().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int j = 0; j < options1Items.get(i).getNodes().get(c).getNodes().size(); j++) {
                        City_AreaList.add(options1Items.get(i).getNodes().get(c).getNodes().get(j).getName());
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);

        }
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
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

    private class MyHandler extends Handler {
        private WeakReference<AddAddressActivity> weakReference;

        public MyHandler(AddAddressActivity addAddressActivity) {
            weakReference = new WeakReference<AddAddressActivity>(addAddressActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final AddAddressActivity addAddressActivity = weakReference.get();
            if (addAddressActivity != null && msg.what == 0) {
                OptionsPickerView pvOptions = new OptionsPickerBuilder(addAddressActivity, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = addAddressActivity.options1Items.get(options1).getPickerViewText() +
                                addAddressActivity.options2Items.get(options1).get(options2) +
                                addAddressActivity.options3Items.get(options1).get(options2).get(options3);
                        addAddressActivity.areaId = addAddressActivity.options1Items.get(options1).getNodes().get(options2).getNodes().get(options3).getId();
                        addAddressActivity.address.setText(tx);
                    }
                })

                        .setTitleText("城市选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

                pvOptions.setPicker(addAddressActivity.options1Items, addAddressActivity.options2Items, addAddressActivity.options3Items);//三级选择器
                pvOptions.show();
            }
        }
    }
}
