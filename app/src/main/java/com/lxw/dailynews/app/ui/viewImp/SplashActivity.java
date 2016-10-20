package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.lxw.dailynews.R;
import com.lxw.dailynews.app.presenter.SplashPresenter;
import com.lxw.dailynews.app.ui.view.ISplashView;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.utils.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lxw.dailynews.R.id.img_icon;

public class SplashActivity extends BaseMvpActivity<ISplashView, SplashPresenter> implements ISplashView {


    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.txt_author)
    TextView txtAuthor;
    @BindView(img_icon)
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
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initActivityTag("启动页");
        initView();
        getSplashPictureInfo();
        getLatestNews();
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
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
        }, 500);
        imgPicture.postDelayed(new Runnable() {
            @Override
            public void run() {
                //启动页图片下载在sd卡里，如果图片不存在，则加载app自带的默认图片
                if(FileUtil.isFileExists(Constant.PATH_SPLASH_PICTURE_PNG)){
                    ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, Constant.PATH_SPLASH_PICTURE_PNG, true, R.mipmap.default_splash_picture);
                }else{
                    imgPicture.setImageResource(R.mipmap.default_splash_picture);
                }
            }
        }, 2000);
    }

    @Override
    public void getSplashPictureInfo() {

    }

    @Override
    public void getLatestNews() {

    }

    @Override
    public void loadSplashPicture() {

    }

    @Override
    public void jumpToNext() {

    }
}