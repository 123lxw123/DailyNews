package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.adapter.NewsContentAdapter;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.presenter.NewContentPresenter;
import com.lxw.dailynews.app.ui.view.INewContentView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewContentActivity extends BaseMvpActivity<INewContentView, NewContentPresenter> implements INewContentView {

    @BindView(R.id.viewpager_news_content)
    ViewPager viewpagerNewsContent;

    private String type;//1 点击头部viewpager热闻 2 点击列表item新闻
    private List<LatestNewsBean.StoriesBean> stories;
    private List<LatestNewsBean.TopStoriesBean> top_stories;
    private int position;//定位当前是哪条新闻，用于左右滑动切换新闻
    private NewsContentAdapter newsContentAdapter;//viewpager适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);
        ButterKnife.bind(this);
        initActivityTag(getString(R.string.new_content));
        initView();
        type = getIntent().getStringExtra("type");
        position = getIntent().getIntExtra("position", 0);
        if (!StringUtil.isEmpty(type)) {
            if ("1".equals(type)) {
                top_stories = (List<LatestNewsBean.TopStoriesBean>) getIntent().getSerializableExtra("top_stories");
            } else {
                stories = (List<LatestNewsBean.StoriesBean>) getIntent().getSerializableExtra("stories");
            }
        }
        initView();
    }

    @NonNull
    @Override
    public NewContentPresenter createPresenter() {
        return new NewContentPresenter();
    }

    @Override
    public void initView() {
        if("1".equals(type) && top_stories != null && top_stories.size() > 0){
            newsContentAdapter = new NewsContentAdapter(getSupportFragmentManager(), type, top_stories, position);
        }else if("2".equals(type) && stories != null && stories.size() > 0){
            newsContentAdapter = new NewsContentAdapter(getSupportFragmentManager(), type, stories, position);
        }
        viewpagerNewsContent.setAdapter(newsContentAdapter);
        viewpagerNewsContent.setCurrentItem(position);
    }

    @Override
    public void prepareData() {
    }

    @Override
    public void rePrepareData() {
    }

    @Override
    public void setNewContent(NewContentBean newContentBean) {

    }

    @Override
    public void getNewContent(String newId) {

    }
}
