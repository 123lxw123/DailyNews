package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewThemeBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/10/16.
 */

public interface ISplashModel {
    void getSplashPictureInfo(HttpListener<SplashPictureInfoBean> httpListener);

    void getLatestNews(HttpListener<LatestNewsBean> httpListener);

    void getNewThemes(HttpListener<NewThemeBean> httpListener);
}
