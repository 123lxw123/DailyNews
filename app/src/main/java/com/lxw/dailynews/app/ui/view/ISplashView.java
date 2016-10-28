package com.lxw.dailynews.app.ui.view;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.framework.base.BaseMvpView;

/**
 * Created by Zion on 2016/10/19.
 */

public interface ISplashView extends BaseMvpView {
    void initView();

    void getSplashPictureInfo();

    void setSplashPicture(String imgUrl, String author);

    void setSplashPicture();

    void getLatestNews();

    void setLatestNewsBean(LatestNewsBean latestNewsBean);

    void jumpToNext();
}
