package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.dailynews.R;
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
    private LatestNewsBean latestNewsBean;
    private MainAdapter<LatestNewsBean.StoriesBean> mainAdapter;
    private List<LatestNewsBean.StoriesBean> stories = new ArrayList<LatestNewsBean.StoriesBean>();
    private List<LatestNewsBean.TopStoriesBean> top_stories = new ArrayList<LatestNewsBean.TopStoriesBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActivityTag("主页");
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
        mainAdapter = new MainAdapter<LatestNewsBean.StoriesBean>(MainActivity.this, R.layout.item_latest_new, stories) {
            @Override
            public void convert(ViewHolder holder, LatestNewsBean.StoriesBean storiesBean, int position) {
                holder.setText(R.id.txt_new_title, storiesBean.getTitle());
                ImageManager.getInstance().loadImage(MainActivity.this, (ImageView) holder.getView(R.id.img_new_picture), storiesBean.getImages().get(0), true);
            }
        };
        //recyclerview header
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mainAdapter);

        headerAndFooterWrapper.addHeaderView(t1);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        //recyclerview loadmore
        loadMoreWrapper = new LoadMoreWrapper(mOriginAdapter);
        loadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested()
            {

            }
        });

        recyclerview.setAdapter(loadMoreWrapper);
    }

    @Override
    public void prepareData() {
        getLatestNews();
    }

    @Override
    public void rePrepareData() {
        top_stories.addAll(latestNewsBean.getTop_stories());
        stories.addAll(latestNewsBean.getStories());
        mainAdapter.notifyDataSetChanged();

    }
}
