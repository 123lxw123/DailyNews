package com.lxw.dailynews.app.ui.viewImp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpView;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.util.FileUtil;
import com.lxw.dailynews.framework.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoPreviewActivity extends AppCompatActivity implements BaseMvpView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_preview)
    ImageView imgPreview;

    private String imgUrl;//预览图片的地址
    private PhotoViewAttacher attacher;
    private String fileName;//图片保存名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        imgUrl = getIntent().getStringExtra("imgUrl");
        String[] strings = imgUrl.split("/");
        fileName = strings[strings.length - 1];
        initView();
    }

    @Override
    public void initView() {
        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.toolbar_photo_preview);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //保存图片
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        ImageManager.getInstance().downloadImage(BaseApplication.appContext, imgUrl, Constant.PATH_SAVE_PICTURE, fileName);
                        subscriber.onNext("");
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                LoggerHelper.info("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showMessage(PhotoPreviewActivity.this, PhotoPreviewActivity.this.getResources().getString(R.string.save_photo_failure));
                            }

                            @Override
                            public void onNext(String s) {
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri uri = Uri.fromFile(new File(Constant.PATH_SAVE_PICTURE + fileName));
                                intent.setData(uri);
                                PhotoPreviewActivity.this.sendBroadcast(intent);
                                ToastUtil.showMessage(PhotoPreviewActivity.this, PhotoPreviewActivity.this.getResources().getString(R.string.save_photo_success));
                            }
                        });

                return true;
            }
        });

        ImageManager.getInstance().loadImage(this, imgPreview, imgUrl, true);
        attacher = new PhotoViewAttacher(imgPreview);
    }

    @Override
    public void prepareData() {

    }

    @Override
    public void rePrepareData() {

    }
}
