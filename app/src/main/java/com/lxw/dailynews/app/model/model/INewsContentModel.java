package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/11/12.
 */

public interface INewsContentModel {

    void getNewsContent(String newsId, HttpListener<NewsContentBean> httpListener);
    NewsContentBean getOfflineNewsContent(String newsId);

    void getNewsStoryExtra(String newsId, HttpListener<NewsStoryExtraBean> httpListener);
}
