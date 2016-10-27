package com.lxw.dailynews.app.ui.viewImp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.presenter.SplashPresenter;
import com.lxw.dailynews.app.ui.view.ISplashView;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.FileUtil;
import com.lxw.dailynews.framework.util.SharePreferencesUtil;
import com.lxw.dailynews.framework.util.StringUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SplashActivity extends BaseMvpActivity<ISplashView, SplashPresenter> implements ISplashView {


    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.txt_author)
    TextView txtAuthor;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_slogan)
    TextView txtSlogan;
    @BindView(R.id.layout_footview)
    LinearLayout layoutFootview;
    @BindView(R.id.layout_picture)
    RelativeLayout layoutPicture;

    private final String SPLASH_AUTHOR = "SPLASH_AUTHOR";
    private LatestNewsBean latestNewsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏虚拟按键
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initActivityTag("启动页");
        initView();
        getSplashPictureInfo();
        getLatestNews();
        jumpToNext();
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void initView() {
        layoutFootview.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.in_bottom_to_top));
        Observable.timer(1200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long along) {
                        imgIcon.setImageResource(R.mipmap.icon_logo);
                    }
                });
    }

    //获取启动页图片信息
    @Override
    public void getSplashPictureInfo() {
            getPresenter().getSplashPictureInfo();
    }

    //加载本地图片
    @Override
    public void setSplashPicture() {
        if (FileUtil.isFileExists(Constant.PATH_SPLASH_PICTURE_PNG)) {
            ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, Constant.PATH_SPLASH_PICTURE_PNG, true, R.mipmap.default_splash_picture);
            String author = SharePreferencesUtil.getStringSharePreferences(SplashActivity.this, SPLASH_AUTHOR, "");
            if (!StringUtil.isEmpty(author)) {
                txtAuthor.setText(author);
                SharePreferencesUtil.setStringSharePreferences(SplashActivity.this, SPLASH_AUTHOR, author);
            }
        } else {
            imgPicture.setImageResource(R.mipmap.default_splash_picture);
        }
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long along) {
                        layoutPicture.setVisibility(View.VISIBLE);
                    }
                });
    }

    //加载网络图片和显示版权作者
    @Override
    public void setSplashPicture(final String imgUrl, final String author) {
        //加载网络图片URL 启动页图片则加载app自带的默认图片
        ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, imgUrl, true, R.mipmap.default_splash_picture);
        if (!StringUtil.isEmpty(author)) {
            txtAuthor.setText(author);
            SharePreferencesUtil.setStringSharePreferences(SplashActivity.this, SPLASH_AUTHOR, author);
        }
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long along) {
                        layoutPicture.setVisibility(View.VISIBLE);
                    }
                });
    }

    //获取最新消息传给主页
    @Override
    public void getLatestNews() {
        getPresenter().getLatestNews();
    }

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
    }

    //跳转到主页
    @Override
    public void jumpToNext() {

        Observable.timer(4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long along) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("latestNewsBean", latestNewsBean);
                        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}