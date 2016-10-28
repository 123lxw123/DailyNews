package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.app.model.modelImp.MainModel;
import com.lxw.dailynews.app.model.modelImp.SplashModel;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public class MainPresenter extends BaseMvpPresenter<IMainView> {
    private IMainModel mainModel;
    private LatestNewsBean latestNewsBean;

    public MainPresenter() {
        mainModel = new MainModel();
    }

    public void getLatestNews() {
        if (isNetworkAvailable()) {
            new SplashModel().getLatestNews(new HttpListener<LatestNewsBean>() {

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

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
    }
}
