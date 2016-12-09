package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.app.model.model.INewsCommentModel;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.model.modelImp.MainModel;
import com.lxw.dailynews.app.model.modelImp.NewsCommentModel;
import com.lxw.dailynews.app.model.modelImp.SplashModel;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.app.ui.view.INewsCommentView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by lxw9047 on 2016/12/8.
 */

public class NewsCommentPresenter extends BaseMvpPresenter<INewsCommentView> {
    private INewsCommentModel newsCommentModel;

    public NewsCommentPresenter() {
        newsCommentModel = new NewsCommentModel();
    }

    //获取新闻评论
    public void getNewsComments(String newsId, final String commentsType) {
        if (isNetworkAvailable()) {
            newsCommentModel.getNewsComments(newsId, commentsType, new HttpListener<NewsCommentBean>() {

                @Override
                public void onSuccess(NewsCommentBean response) {
                    if (response != null) {
                        if(commentsType.equals(Constant.COMMENTS_TYPE_LONG)){
                            getView().setLongCommentBean(response);
                        }else{
                            getView().setShortCommentBean(response);
                        }
                    }else{
                        getView().stopRefreshAnimation();
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    //展示上次加载的消息
                    getView().stopRefreshAnimation();
                }
            });
        } else {
            //展示上次加载的消息
            getView().stopRefreshAnimation();
        }
    }
}
