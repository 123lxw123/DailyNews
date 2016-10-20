package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.framework.http.HttpHelper;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Zion on 2016/10/16.
 */

public class SplashModel implements ISplashModel {

    //获取启动页图片信息
    @Override
    public SplashPictureInfoBean getSplashPictureInfo() {
        Observable<SplashPictureInfoBean> observable = HttpHelper.httpService.getSplashPictureInfo();
        Subscriber subscriber = new Subscriber<SplashPictureInfoBean>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SplashPictureInfoBean splashPictureInfoBean) {

            }
        };
//        HttpHelper.doSubscribe(observable, subscriber);
        return null;
    }
    //获取主页最新消息
    @Override
    public LatestNewsBean getLatestNews() {
        return null;
    }
}
