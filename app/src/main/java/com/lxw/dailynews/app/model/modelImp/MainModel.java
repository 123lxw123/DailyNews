package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;

import java.util.List;

import rx.Observable;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public class MainModel implements IMainModel{
    @Override
    public void getBeforeNews(final String beforeDate, HttpListener<LatestNewsBean> httpListener) {
        new HttpManager<LatestNewsBean>(httpListener){

            @Override
            public Observable<LatestNewsBean> createObservable() {
                return HttpHelper.getInstance().getBeforeNews(beforeDate);
            }
        };
    }

    @Override
    public void getThemeContent(final String themeId, HttpListener<ThemeContentBean> httpListener) {
        new HttpManager<ThemeContentBean>(httpListener){

            @Override
            public Observable<ThemeContentBean> createObservable() {
                return HttpHelper.getInstance().getThemeContent(themeId);
            }
        };
    }

    @Override
    public void getBeforeThemeContent(final String themeId, final String timeStamp, HttpListener<List<LatestNewsBean.StoriesBean>> httpListener) {
        new HttpManager<List<LatestNewsBean.StoriesBean>>(httpListener){

            @Override
            public Observable<List<LatestNewsBean.StoriesBean>> createObservable() {
                return HttpHelper.getInstance().getBeforeThemeContent(themeId, timeStamp);
            }
        };
    }
}
