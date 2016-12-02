package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtra;
import com.lxw.dailynews.app.model.model.INewsContentModel;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;

import rx.Observable;

/**
 * Created by Zion on 2016/11/12.
 */

public class NewsContentModel implements INewsContentModel {

    @Override
    public void getNewsContent(final String newsId, HttpListener<NewsContentBean> httpListener) {
        new HttpManager<NewsContentBean>(httpListener){

            @Override
            public Observable<NewsContentBean> createObservable() {
                return HttpHelper.getInstance().getNewsContent(newsId);
            }
        };
    }

    @Override
    public void getNewsStoryExtra(final String newsId, HttpListener<NewsStoryExtra> httpListener) {
        new HttpManager<NewsStoryExtra>(httpListener){

            @Override
            public Observable<NewsStoryExtra> createObservable() {
                return HttpHelper.getInstance().getNewsStoryExtra(newsId);
            }
        };
    }
}
