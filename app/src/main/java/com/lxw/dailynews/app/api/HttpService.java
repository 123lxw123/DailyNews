package com.lxw.dailynews.app.api;

import com.lxw.dailynews.app.bean.BeforeThemeContentBean;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;

import java.util.List;

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
    Observable<NewsThemeBean> getNewsThemes();

    @GET("4/news/{newsId}")
    Observable<NewsContentBean> getNewsContent(@Path("newsId") String newsId);

    @GET("4/story-extra/{newsId}")
    Observable<NewsStoryExtraBean> getNewsStoryExtra(@Path("newsId") String newsId);

    @GET("4/theme/{themeId}")
    Observable<ThemeContentBean> getThemeContent(@Path("themeId") String themeId);

    @GET("4/theme/{themeId}/before/{newsId}")
    Observable<BeforeThemeContentBean> getBeforeThemeContent(@Path("themeId") String themeId, @Path("newsId") String newsId);

    @GET("4/story/{newsId}/{commentsType}")
    Observable<NewsCommentBean> getNewsComments(@Path("newsId") String newsId, @Path("commentsType") String commentsType);

    @GET("4/story/{newsId}/{commentsType}/before/{commentId}")
    Observable<NewsCommentBean> getBeforeNewsComments(@Path("newsId") String newsId, @Path("commentsType") String commentsType, @Path("commentId") String commentId);

}
