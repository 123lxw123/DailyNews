package com.lxw.dailynews.app.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.model.modelImp.SplashModel;
import com.lxw.dailynews.app.ui.view.ISplashView;

/**
 * Created by Zion on 2016/10/16.
 */

public class SplashPresenter extends MvpBasePresenter<ISplashView> {
    private ISplashView splashView;
    private ISplashModel splashModel;

    public SplashPresenter(){
        splashView = getView();
        splashModel = new SplashModel();
    }
    public SplashPictureInfoBean getSplashPictureInfo() {
        return splashModel.getSplashPictureInfo();
    }
}
