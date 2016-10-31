package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.adapter.HeaderAdapter;
import com.lxw.dailynews.app.adapter.MainAdapter;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout layoutSwipeRefresh;


    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper loadMoreWrapper;
    private View headerView;
    private ViewPager viewpagerHeaderPicture;
    private LinearLayout layoutHeaderPosition;
    private LatestNewsBean latestNewsBean;
    private MainAdapter<LatestNewsBean.StoriesBean> mainAdapter;
    private HeaderAdapter headerAdapter;
    private List<LatestNewsBean.StoriesBean> stories = new ArrayList<LatestNewsBean.StoriesBean>();
    private List<LatestNewsBean.TopStoriesBean> top_stories = new ArrayList<LatestNewsBean.TopStoriesBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityTag("主页");
        ButterKnife.bind(this);
        latestNewsBean = (LatestNewsBean) getIntent().getSerializableExtra("latestNewsBean");
        initView();
        if (latestNewsBean == null) {
            prepareData();
        } else {
            rePrepareData();
        }
    }

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
        if (this.latestNewsBean != null) {
            rePrepareData();
        }
    }

    //获取最新消息传给主页
    @Override
    public void getLatestNews() {
        getPresenter().getLatestNews();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void initView() {

        //toolbar
        toolbar.inflateMenu(R.menu.toolbar_main);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_notification:
                        break;
                    case R.id.action_styles_night:
                        break;
                    case R.id.action_setting_option:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //SwipeRefreshLayout
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        };
        layoutSwipeRefresh.setOnRefreshListener(refreshListener);

        //recyclerview adapter
        recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter<LatestNewsBean.StoriesBean>(MainActivity.this, R.layout.item_latest_new, stories) {
            @Override
            public void convert(ViewHolder holder, LatestNewsBean.StoriesBean storiesBean, int position) {
                holder.setText(R.id.txt_new_title, storiesBean.getTitle());
                String url = storiesBean.getImages().get(0);
                ImageManager.getInstance().loadImage(MainActivity.this, (ImageView) holder.getView(R.id.img_new_picture), storiesBean.getImages().get(0), true);
            }
        };
//        recyclerview.setAdapter(mainAdapter);

        //recyclerview header
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mainAdapter);
        headerView = getLayoutInflater().inflate(R.layout.header_top_new, null);
        viewpagerHeaderPicture = (ViewPager)headerView.findViewById(R.id.viewpager_header_picture);
        layoutHeaderPosition = (LinearLayout)headerView.findViewById(R.id.layout_header_position);
        headerAdapter = new HeaderAdapter(MainActivity.this, top_stories);
        viewpagerHeaderPicture.setAdapter(headerAdapter);
        headerAndFooterWrapper.addHeaderView(headerView);

        //recyclerview loadmore
//        loadMoreWrapper = new LoadMoreWrapper(mOriginAdapter);
//        loadMoreWrapper.setLoadMoreView(R.layout.default_loading);
//        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
//        {
//            @Override
//            public void onLoadMoreRequested()
//            {
//
//            }
//        });
//        recyclerview.setAdapter(loadMoreWrapper);
    }

    @Override
    public void prepareData() {
        getLatestNews();
    }

    @Override
    public void rePrepareData() {
        if (top_stories == null || top_stories.size() == 0) {
            top_stories.addAll(latestNewsBean.getTop_stories());
        }
        stories.addAll(latestNewsBean.getStories());

        viewpagerHeaderPicture.setAdapter(headerAdapter);
        headerAdapter.notifyDataSetChanged();

//        recyclerview.setAdapter(mainAdapter);
//        mainAdapter.notifyDataSetChanged();

        recyclerview.setAdapter(headerAndFooterWrapper);
        headerAndFooterWrapper.notifyDataSetChanged();

        viewpagerHeaderPicture.setAdapter(headerAdapter);
        headerAdapter.notifyDataSetChanged();

    }
}
