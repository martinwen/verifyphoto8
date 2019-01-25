package com.shcy.yyzzj.module.album;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.shcy.yyzzj.R;
import com.shcy.yyzzj.bean.album.ALbumBean;
import com.shcy.yyzzj.bean.album.AlbumListBean;
import com.shcy.yyzzj.bean.pay.PrintPayBean;
import com.shcy.yyzzj.config.Constants;
import com.shcy.yyzzj.module.camera.PictrueConfirmActivity;
import com.shcy.yyzzj.module.imagepicker.ImagePicker;
import com.shcy.yyzzj.module.printsubmit.PrintSubmitActivity;
import com.shcy.yyzzj.module.selectsize.SelectSizeActivity;
import com.shcy.yyzzj.utils.DialogUtil;
import com.shcy.yyzzj.utils.PermissionUtil;
import com.shcy.yyzzj.dialog.PhotoDialog;
import com.shcy.yyzzj.utils.PublicUtil;
import com.shcy.yyzzj.utils.ToastUtil;
import com.shcy.yyzzj.view.view.HeadFootAdapter;
import com.shcy.yyzzj.view.view.HorizontalPageLayoutManager;
import com.shcy.yyzzj.view.view.PagingScrollHelper;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;

import static com.shcy.yyzzj.module.imagepicker.ImagePicker.REQUEST_CODE_CHOOSE;

/**
 * Created by licong on 2018/9/29.
 */

public class AlbumActivity extends Activity implements View.OnClickListener, AlbumContract.View {

    private static final String TAG = "相册";

    private LinearLayout emptyLayout, buttonLayout, savetoWx;
    private ImageView back;
    private TextView importPhoto, pageNum, download, print;
    private RecyclerView recyclerView;

    private AlbumContract.Presenter presenter;
    private List<ALbumBean> list = new ArrayList<>();
    private PagingScrollHelper scrollHelper = new PagingScrollHelper();
    private HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(1, 1);
    private HeadFootAdapter headFootAdapter;
    private final int REQUEST_PERMISSION_CODE = 015;
    private final int SET_PERMISSION_CODE = REQUEST_PERMISSION_CODE + 1;

    private int pageNo = 1;
    private boolean hasMoreData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        new AlbumPresenter(this);
        initView();
        initData();
    }

    private void initView() {
        emptyLayout = findViewById(R.id.album_empty_layout);
        back = findViewById(R.id.album_back);
        importPhoto = findViewById(R.id.album_import_photo);
        pageNum = findViewById(R.id.album_pageNum);
        buttonLayout = findViewById(R.id.album_button_layout);
        recyclerView = findViewById(R.id.album_viewpager);
        download = findViewById(R.id.album_button_download);
        print = findViewById(R.id.album_button_print);
        print.setOnClickListener(this);
        download.setOnClickListener(this);
        back.setOnClickListener(this);
        importPhoto.setOnClickListener(this);
        recyclerView.setLayoutManager(horizontalPageLayoutManager);
        recyclerView.setAdapter(getHeadFootAdapter());
        headFootAdapter.setList(list);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(new PagingScrollHelper.OnPageChangeListener() {
            @Override
            public void onPageChange(int index) {
                pageNum.setText(index + 1 + "/" + list.size());
            }
        });
        savetoWx = findViewById(R.id.album_saveto_wx);
        savetoWx.setOnClickListener(this);
    }

    private void initData() {
        presenter.getAlbumList(pageNo);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.album_back:
                finish();
                break;
            case R.id.album_import_photo:
                MobclickAgent.onEvent(this, Constants.EVENT_BUTTON_ALNUM_IMPORT);
                PermissionUtil.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE, new PermissionUtil.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ImagePicker.getImagesPath(AlbumActivity.this, 1, false);
                    }

                    @Override
                    public void onPermissionDenied() {
                        PermissionUtil.showExsit(AlbumActivity.this, getString(R.string.need_permission_camera), new PermissionUtil.OnSetListener() {
                            @Override
                            public void onSueccess() {

                            }

                            @Override
                            public void onFailed() {

                            }
                        }, SET_PERMISSION_CODE);
                    }
                });
                break;
            case R.id.album_button_download:
                int p = scrollHelper.getPageIndex();
                if (list.size() - 1 < p) {
                    return;
                }
                PublicUtil.downloadImage(list.get(p).getImage(), list.get(p).getId() + "", this);
                ToastUtil.showToast(getString(R.string.download_success), false);
                break;

            case R.id.album_button_print:
                int p1 = scrollHelper.getPageIndex();
                if (list.size() - 1 < p1) {
                    return;
                }
                ALbumBean bean = list.get(p1);
                PrintPayBean printPayBean = new PrintPayBean();
                printPayBean.setPhotoname(bean.getSpecName());
                printPayBean.setIdnumber(bean.getId() + "");
                printPayBean.setIncludecount(bean.getIncludeCount());
                printPayBean.setUrl(bean.getImage());
                printPayBean.setType(1);
                Intent intent = new Intent(this, PrintSubmitActivity.class);
                intent.putExtra(PrintSubmitActivity.PRINTPAY_BEAN, printPayBean);
                startActivity(intent);
                break;
            case R.id.album_delete_layout:
                DialogUtil.showDeletePhotoDialog(this, new PhotoDialog.OnDialogClickListener() {
                    @Override
                    public void confirm() {
                        int p = (int) v.getTag();
                        presenter.deletePhoto(list.get(p).getId() + "");
                        headFootAdapter.getList().remove(p);
                        headFootAdapter.notifyDataSetChanged();
                        if (p > headFootAdapter.getList().size() - 1) {
                            scrollHelper.scrollToPosition(headFootAdapter.getList().size() - 1);
                        }
                        ToastUtil.showToast("删除成功！", false);
                        if (headFootAdapter.getList().size() == 0) {
                            savetoWx.setVisibility(View.GONE);
                            buttonLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            pageNum.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.album_saveto_wx:
                int p2 = scrollHelper.getPageIndex();
                if (list.size() - 1 < p2) {
                    return;
                }
                UMImage image = new UMImage(this, list.get(p2).getImage());
                image.setThumb(image);
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(image)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .share();
                break;
        }
    }

    @Override
    public void setPresenter(AlbumContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAlbumData(AlbumListBean listBean) {
        if (pageNo == 1) {
            list.clear();
        }
        list.addAll(listBean.getData());
        headFootAdapter.notifyDataSetChanged();
        if (list.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            savetoWx.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            pageNum.setVisibility(View.GONE);
        } else {
            pageNum.setVisibility(View.VISIBLE);
            savetoWx.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        if (listBean.getNextCursor() != 0) {
            pageNo++;
            hasMoreData = true;
        }
        pageNum.setText(scrollHelper.getPageIndex() + 1 + "/" + list.size());
    }

    private HeadFootAdapter getHeadFootAdapter() {
        if (headFootAdapter == null) {
            headFootAdapter = new HeadFootAdapter(this);
            headFootAdapter.addItemTemplate(new Albumtemplate(this));
        }
        return headFootAdapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.onActivityResult(requestCode, this);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0 && !TextUtils.isEmpty(paths.get(0))) {
                Intent intent = new Intent(this, PictrueConfirmActivity.class);
                intent.putExtra(SelectSizeActivity.TYPE, 1);
                intent.putExtra(SelectSizeActivity.IMAGEPATH, paths.get(0));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, Constants.EVENT_ALBUM_PV);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
