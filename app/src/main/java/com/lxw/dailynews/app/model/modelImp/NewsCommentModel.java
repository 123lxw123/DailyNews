package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.model.model.INewsCommentModel;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;

import rx.Observable;

/**
 * Created by lxw9047 on 2016/12/8.
 */

public class NewsCommentModel implements INewsCommentModel {
    @Override
    public void getNewsComments(final String newsId, final String commentsType, final HttpListener<NewsCommentBean> httpListener) {
        new HttpManager<NewsCommentBean>(httpListener) {

            @Override
            public Observable<NewsCommentBean> createObservable() {
                return HttpHelper.getInstance().getNewsComments(newsId, commentsType);
            }
        };
    }
}
