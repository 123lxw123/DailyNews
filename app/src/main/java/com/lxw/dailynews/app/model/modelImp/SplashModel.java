package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Zion on 2016/10/16.
 * 启动页数据
 */

public class SplashModel implements ISplashModel {

    //获取启动页图片信息
    @Override
    public void getSplashPictureInfo(HttpListener<SplashPictureInfoBean> httpListener) {
        new HttpManager<SplashPictureInfoBean>(httpListener) {
            @Override
            public Observable<SplashPictureInfoBean> createObservable() {
                return HttpHelper.getInstance().getSplashPictureInfo();
            }
        };
    }

    //获取主页最新消息
    @Override
    public void getLatestNews(HttpListener<LatestNewsBean> httpListener) {
        new HttpManager<LatestNewsBean>(httpListener){

            @Override
            public Observable<LatestNewsBean> createObservable() {
                return HttpHelper.getInstance().getLatestNews();
            }
        };
    }
}
