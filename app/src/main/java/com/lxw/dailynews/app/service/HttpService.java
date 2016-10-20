package com.lxw.dailynews.app.service;

import com.lxw.dailynews.app.bean.SplashPictureInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public interface HttpService {
    @GET("4/news/latest")
    Observable<SplashPictureInfoBean> getSplashPictureInfo();
}
