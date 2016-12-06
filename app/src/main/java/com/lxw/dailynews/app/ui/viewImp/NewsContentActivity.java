package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.adapter.NewsContentAdapter;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.presenter.NewsContentPresenter;
import com.lxw.dailynews.app.ui.view.INewsContentView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsContentActivity extends BaseMvpActivity<INewsContentView, NewsContentPresenter> implements INewsContentView {

    @BindView(R.id.viewpager_news_content)
    public ViewPager viewpagerNewsContent;
    @BindView(R.id.img_share)
    public ImageView imgShare;
    @BindView(R.id.img_collect)
    public ImageView imgCollect;
    @BindView(R.id.img_comment)
    public ImageView imgComment;
    @BindView(R.id.txt_comment)
    public TextView txtComment;
    @BindView(R.id.img_praise)
    public ImageView imgPraise;
    @BindView(R.id.txt_praise)
    public TextView txtPraise;
    @BindView(R.id.layout_extra_info)
    public LinearLayout layoutExtraInfo;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private String type;//1 点击头部viewpager热闻 2 点击列表item新闻
    private List<LatestNewsBean.StoriesBean> stories;
    private List<LatestNewsBean.TopStoriesBean> top_stories;
    private int position;//定位当前是哪条新闻，用于左右滑动切换新闻
    private NewsContentAdapter newsContentAdapter;//viewpager适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);
        initActivityTag(getString(R.string.news_content));
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
    public NewsContentPresenter createPresenter() {
        return new NewsContentPresenter();
    }

    @Override
    public void initView() {
        if ("1".equals(type) && top_stories != null && top_stories.size() > 0) {
            newsContentAdapter = new NewsContentAdapter(getSupportFragmentManager(), type, top_stories, position);
        } else if (("2".equals(type) || "3".equals(type)) && stories != null && stories.size() > 0) {
            newsContentAdapter = new NewsContentAdapter(getSupportFragmentManager(), type, stories, position);
        }
        viewpagerNewsContent.setAdapter(newsContentAdapter);
        viewpagerNewsContent.setCurrentItem(position);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsContentActivity.this.finish();
            }
        });
    }

    @Override
    public void prepareData() {
    }

    @Override
    public void rePrepareData() {
    }

    @Override
    public void setNewContent(NewsContentBean newsContentBean) {

    }

    public void setNewsStoryExtraBean(NewsStoryExtraBean newsStoryExtraBean) {

    }

    @Override
    public void getNewsContent(String newsId) {

    }

    @Override
    public void getNewsStoryExtra(String newsId) {

    }

}
