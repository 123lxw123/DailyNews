package com.lxw.dailynews.app.ui.viewImp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.app.presenter.NewsCommentPresenter;
import com.lxw.dailynews.app.ui.view.INewsCommentView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;

public class NewsCommentActivity extends BaseMvpActivity<INewsCommentView, NewsCommentPresenter> implements INewsCommentView{

    private String comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        comments = getIntent().getStringExtra("comments");
        initView();
    }

    @NonNull
    @Override
    public NewsCommentPresenter createPresenter() {
        return new NewsCommentPresenter();
    }

    @Override
    public void getNewsComments(String newsId, String commentsType) {

    }

    @Override
    public void setNewsCommentBean(NewsCommentBean newsCommentBean) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void prepareData() {

    }

    @Override
    public void rePrepareData() {

    }
}
