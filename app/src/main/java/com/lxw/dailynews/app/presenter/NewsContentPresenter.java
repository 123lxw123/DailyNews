package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.model.model.INewsContentModel;
import com.lxw.dailynews.app.model.modelImp.NewsContentModel;
import com.lxw.dailynews.app.ui.view.INewsContentView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/11/12.
 */

public class NewsContentPresenter extends BaseMvpPresenter<INewsContentView> {
    private INewsContentModel newsContentModel;

    public NewsContentPresenter() {
        newsContentModel = new NewsContentModel();
    }

    //获取消息内容
    public void getNewsContent(String newsId) {
        if(MainPresenter.frag_offline){
            if(newsContentModel.getOfflineNewsContent(newsId) != null){
                getView().setNewContent(newsContentModel.getOfflineNewsContent(newsId));
            }
        }else if (checkNetword()) {
            newsContentModel.getNewsContent(newsId, new HttpListener<NewsContentBean>() {
                @Override
                public void onSuccess(NewsContentBean response) {
                    if (response != null) {
                        getView().setNewContent(response);
                    } else {
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

    //获取消息额外信息
    public void getNewsStoryExtra(String newsId) {
        if (checkNetword()) {
            newsContentModel.getNewsStoryExtra(newsId, new HttpListener<NewsStoryExtraBean>() {
                @Override
                public void onSuccess(NewsStoryExtraBean response) {
                    if (response != null) {
                        getView().setNewsStoryExtraBean(response);
                    } else {
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
