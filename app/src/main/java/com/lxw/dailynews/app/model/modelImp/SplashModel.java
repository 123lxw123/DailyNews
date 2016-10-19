package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.model.model.ISplashModel;

/**
 * Created by Zion on 2016/10/16.
 */

public class SplashModel implements ISplashModel {

    //获取启动页图片信息
    @Override
    public SplashPictureInfoBean getSplashPictureInfo(String url) {
        return null;
    }
    //获取主页最新消息
    @Override
    public LatestNewsBean getLatestNews(String url) {
        return null;
    }
}
