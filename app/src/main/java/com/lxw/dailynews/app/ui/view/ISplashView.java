package com.lxw.dailynews.app.ui.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Zion on 2016/10/19.
 */

public interface ISplashView extends MvpView {
    void initView();

    void getSplashPictureInfo();

    void setSplashPicture(String imgUrl, String author);

    void setSplashPicture();

    void getLatestNews();

    void jumpToNext();
}
