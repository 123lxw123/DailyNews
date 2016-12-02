package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.model.modelImp.MainModel;
import com.lxw.dailynews.app.model.modelImp.SplashModel;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private IMainModel mainModel;
    private ISplashModel splashModel;

    public MainPresenter() {
        mainModel = new MainModel();
        splashModel = new SplashModel();
    }

    //获取最新热闻
    public void getLatestNews() {
        if (isNetworkAvailable()) {
            splashModel.getLatestNews(new HttpListener<LatestNewsBean>() {

                @Override
                public void onSuccess(LatestNewsBean response) {
                    if (response != null) {
                        getView().setLatestNewsBean(response);
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    //展示上次加载的消息
                }
            });
        } else {
            //展示上次加载的消息
        }
    }

    //获取之前某一天的热闻
    public void getBeforeNews(String beforeDate) {
        if (checkNetword()) {
            mainModel.getBeforeNews(beforeDate, new HttpListener<LatestNewsBean>() {
                @Override
                public void onSuccess(LatestNewsBean response) {
                    if (response != null) {
                        getView().setLatestNewsBean(response);
                    }else {
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                }
            });
        }
    }

    //获取热闻主题
    public void getNewsThemes(){
        if (checkNetword()) {
            splashModel.getNewsThemes(new HttpListener<NewsThemeBean>() {
                @Override
                public void onSuccess(NewsThemeBean response) {
                    if (response != null) {
                        getView().setNewsThemeBean(response);
                    }else {
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                }
            });
        }
    }
}
