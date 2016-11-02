package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.adapter.HeaderAdapter;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewThemeBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.app.ui.view.ISplashView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.TimeUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
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
    private LinearLayout layoutHeaderDot;
    private LatestNewsBean latestNewsBean;
    private NewThemeBean newThemeBean;
    private MultiItemTypeAdapter<LatestNewsBean.StoriesBean> mainAdapter;
    private HeaderAdapter headerAdapter;
    private List<LatestNewsBean.StoriesBean> stories = new ArrayList<LatestNewsBean.StoriesBean>();
    private List<LatestNewsBean.TopStoriesBean> top_stories = new ArrayList<LatestNewsBean.TopStoriesBean>();
    private ImageView[] dotViews;//用于包含底部小圆点的图片
    private boolean refreshFlag = true;//是否刷新的标志
    private int count = 0;//前N天计数，用于获取以前的热闻
    private String currentDate;//应用的当前日期，当前展示的今天热闻的日期；不是系统当前日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityTag("主页");
        ButterKnife.bind(this);
        latestNewsBean = (LatestNewsBean) getIntent().getSerializableExtra("latestNewsBean");
        newThemeBean = (NewThemeBean) getIntent().getSerializableExtra("newThemeBean");
        initView();
        if (latestNewsBean == null || newThemeBean == null) {
            prepareData();
        } else {
            rePrepareData();
            initNewThemeList();
        }
    }

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
        if (this.latestNewsBean != null) {
            rePrepareData();
        }
    }

    @Override
    public void getBeforeNews(String beforeDate) {
        getPresenter().getBeforeNews(beforeDate);
    }

    @Override
    public void getNewThemes() {
        getPresenter().getNewThemes();
    }

    @Override
    public void setNewThemeBean(NewThemeBean newThemeBean) {
        this.newThemeBean = newThemeBean;
        if (this.newThemeBean != null) {
            initNewThemeList();
        }
    }

    @Override
    public void initNewThemeList() {

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
        //设置卷内的颜色
        layoutSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        //下拉刷新监听
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFlag = true;
                getLatestNews();
            }
        };
        layoutSwipeRefresh.setOnRefreshListener(refreshListener);

        recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //recyclerview adapter
        mainAdapter = new MultiItemTypeAdapter(MainActivity.this, stories);
        mainAdapter.addItemViewDelegate(new ItemViewDelegate<LatestNewsBean.StoriesBean>() {//热点新闻item
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_latest_new;
            }

            @Override
            public boolean isForViewType(LatestNewsBean.StoriesBean item, int position) {
                return StringUtil.isEmpty(item.getHeaderTitle());
            }

            @Override
            public void convert(ViewHolder holder, LatestNewsBean.StoriesBean storiesBean, int position) {
                holder.setText(R.id.txt_new_title, storiesBean.getTitle());
                ImageManager.getInstance().loadImage(MainActivity.this, (ImageView) holder.getView(R.id.img_new_picture), storiesBean.getImages().get(0), true);
                holder.setVisible(R.id.img_multipic, storiesBean.isMultipic());
            }
        });
        mainAdapter.addItemViewDelegate(new ItemViewDelegate<LatestNewsBean.StoriesBean>() {//新闻日期分类标题item
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_header_title;
            }

            @Override
            public boolean isForViewType(LatestNewsBean.StoriesBean item, int position) {
                return !StringUtil.isEmpty(item.getHeaderTitle());
            }

            @Override
            public void convert(ViewHolder holder, LatestNewsBean.StoriesBean storiesBean, int position) {
                holder.setText(R.id.txt_header_title, storiesBean.getHeaderTitle());
            }
        });

        //recyclerview header
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mainAdapter);
        headerView = getLayoutInflater().inflate(R.layout.header_top_new, null);
        viewpagerHeaderPicture = (ViewPager) headerView.findViewById(R.id.viewpager_header_picture);
        layoutHeaderDot = (LinearLayout) headerView.findViewById(R.id.layout_header_dot);
        headerAndFooterWrapper.addHeaderView(headerView);
        headerAdapter = new HeaderAdapter(MainActivity.this, top_stories);
        //底部小圆点监听
        viewpagerHeaderPicture.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotViews.length; i++) {
                    if (position == i) {
                        dotViews[i].setSelected(true);
                    } else {
                        dotViews[i].setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //recyclerview loadmore
        loadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
        loadMoreWrapper.setLoadMoreView(R.layout.layout_enpty);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {//获取前N天的热闻
                refreshFlag = false;
                getBeforeNews(TimeUtil.getBeforeDate(currentDate, count));
                count++;//前N天+1
            }
        });
//
    }

    @Override
    public void prepareData() {
        if(latestNewsBean == null){
            getLatestNews();
        }
        if(newThemeBean == null){
            getNewThemes();
        }
    }

    @Override
    public void rePrepareData() {

        if (refreshFlag) {
            currentDate = latestNewsBean.getDate();//应用的当前日期
            count = 0;//前N天初始化为0
            top_stories.clear();
            top_stories.addAll(latestNewsBean.getTop_stories());
            stories.clear();
            initDots();
        }

        //添加新闻日期分类标题的item数据
        LatestNewsBean.StoriesBean storie = new LatestNewsBean.StoriesBean();
        TimeUtil.getDateNumber();
        if (latestNewsBean.getDate().equals(TimeUtil.getDateNumber())) {//如果时间是今天
            storie.setHeaderTitle(getString(R.string.main_latest_news));
        } else {//时间是过去的一天
            String date = latestNewsBean.getDate().substring(4, 6) + "月" + latestNewsBean.getDate().substring(6, 8) + "日";
            String week = TimeUtil.getWeek(latestNewsBean.getDate());
            storie.setHeaderTitle(date + " " + week);
        }
        stories.add(storie);
        stories.addAll(latestNewsBean.getStories());

        //刷新列表
        if (refreshFlag) {
            viewpagerHeaderPicture.setAdapter(headerAdapter);
            recyclerview.setAdapter(loadMoreWrapper);
        }
        headerAdapter.notifyDataSetChanged();
        loadMoreWrapper.notifyDataSetChanged();


        //停止刷新小圈圈动画
        if(refreshFlag && layoutSwipeRefresh.isRefreshing()){
            layoutSwipeRefresh.setRefreshing(false);
        }
    }
    //初始化header底部小圆点
    private void initDots() {
        layoutHeaderDot.removeAllViews();
        dotViews = new ImageView[top_stories.size()];
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(0, 0, 5, 0);//设置小圆点左右之间的间隔

        for (int i = 0; i < top_stories.size(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.selector_dot);
            if (i == 0) {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layoutHeaderDot.addView(imageView);//添加到布局里面显示
        }
    }
}
