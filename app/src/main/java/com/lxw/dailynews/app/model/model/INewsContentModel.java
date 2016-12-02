package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtra;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/11/12.
 */

public interface INewsContentModel {

    void getNewsContent(String newsId, HttpListener<NewsContentBean> httpListener);

    void getNewsStoryExtra(String newsId, HttpListener<NewsStoryExtra> httpListener);
}
