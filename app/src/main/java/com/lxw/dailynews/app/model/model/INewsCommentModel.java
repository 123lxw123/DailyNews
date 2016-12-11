package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.framework.http.HttpListener;

import retrofit2.http.Path;

/**
 * Created by lxw9047 on 2016/12/8.
 */

public interface INewsCommentModel  {
    void getNewsComments(String newsId, String commentsType, HttpListener<NewsCommentBean> httpListener);

    void getBeforeNewsComments(String newsId, String commentsType, String commentId, HttpListener<NewsCommentBean> httpListener);
}
