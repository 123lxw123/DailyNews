package com.lxw.dailynews.app.api;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.bean.NewThemeBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public interface HttpService {

    @GET("4/start-image/1080*1776")
    Observable<SplashPictureInfoBean> getSplashPictureInfo();

    @GET("4/news/latest")
    Observable<LatestNewsBean> getLatestNews();

    @GET("4/news/before/{beforeDate}")
    Observable<LatestNewsBean> getBeforeNews(@Path("beforeDate") String beforeDate);

    @GET("4/themes")
    Observable<NewThemeBean> getNewThemes();

    @GET("4/news/{newId}")
    Observable<NewContentBean> getNewContent(@Path("newId") String newId);

}
