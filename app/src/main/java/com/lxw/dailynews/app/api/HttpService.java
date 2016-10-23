package com.lxw.dailynews.app.api;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public interface HttpService {
    @GET("4/start-image/1080*1776")
    Observable<SplashPictureInfoBean> getSplashPictureInfo();

    @GET("4/news/latest")
    Observable<LatestNewsBean> getLatestNews();
}
