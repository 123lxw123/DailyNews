package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.presenter.SplashPresenter;
import com.lxw.dailynews.app.ui.view.ISplashView;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.utils.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initActivityTag("启动页");
        initView();
        getSplashPictureInfo();
        getLatestNews();
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void initView() {
        layoutFootview.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.in_bottom_to_top));
        imgIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgIcon.setImageResource(R.mipmap.icon_logo);
            }
        }, 1000);
    }

    //获取启动页图片信息
    @Override
    public void getSplashPictureInfo() {
        if (isNetworkAvailable()) {
            getPresenter().getSplashPictureInfo();
        } else {
            setSplashPicture();
        }
    }

    //加载本地图片
    @Override
    public void setSplashPicture() {

        imgPicture.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FileUtil.isFileExists(Constant.PATH_SPLASH_PICTURE_PNG)) {
                    ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, Constant.PATH_SPLASH_PICTURE_PNG, true, R.mipmap.default_splash_picture);
                } else {
                    imgPicture.setImageResource(R.mipmap.default_splash_picture);
                }
            }
        }, 2000);
    }

    //加载网络图片和显示版权作者
    @Override
    public void setSplashPicture(final String imgUrl, String author) {

        imgPicture.postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载网络图片URL 启动页图片则加载app自带的默认图片
                ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, imgUrl, true, R.mipmap.default_splash_picture);
            }
        }, 2000);
    }

    @Override
    public void getLatestNews() {

    }

    @Override
    public void jumpToNext() {

    }
}