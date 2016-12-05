package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.framework.http.HttpListener;

import java.util.List;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public interface IMainModel {
    void getBeforeNews(String beforeDate, HttpListener<LatestNewsBean> httpListener);

    void getThemeContent(String themeId, HttpListener<ThemeContentBean> httpListener);

    void getBeforeThemeContent(String themeId, String timeStamp, HttpListener<List<LatestNewsBean.StoriesBean>> httpListener);
}
