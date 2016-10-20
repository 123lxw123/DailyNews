package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;

/**
 * Created by Zion on 2016/10/16.
 */

public interface ISplashModel {
    SplashPictureInfoBean getSplashPictureInfo();

    LatestNewsBean getLatestNews();
}
