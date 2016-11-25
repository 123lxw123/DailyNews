package com.lxw.dailynews.app.ui.viewImp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.util.DiffUtil;
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
import com.lxw.dailynews.app.adapter.LatestNewsDiffCallBack;
import com.lxw.dailynews.app.adapter.OtherNewThemesDiffCallBack;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewThemeBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.base.BaseCommonAdapter;
import com.lxw.dailynews.framework.base.BaseMultiItemTypeAdapter;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.TimeUtil;
import com.lxw.dailynews.framework.util.ValueUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout layoutSwipeRefresh;
    @BindView(R.id.drawer_recyclerview)
    RecyclerView drawerRecyclerview;
    @BindView(R.id.layout_drawer)
    DrawerLayout layoutDrawer;
    @BindView(R.id.float_action_btn)
    FloatingActionButton floatActionBtn;

    //主界面
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper loadMoreWrapper;
    private View headerView;
    private ViewPager viewpagerHeaderPicture;
    private LinearLayout layoutHeaderDot;
    private LatestNewsBean latestNewsBean;
    private BaseMultiItemTypeAdapter<LatestNewsBean.StoriesBean> mainAdapter;
    private HeaderAdapter headerAdapter;
    private List<LatestNewsBean.StoriesBean> stories = new ArrayList<LatestNewsBean.StoriesBean>();
    private List<LatestNewsBean.StoriesBean> stories_only = new ArrayList<LatestNewsBean.StoriesBean>();
    private List<LatestNewsBean.TopStoriesBean> top_stories = new ArrayList<LatestNewsBean.TopStoriesBean>();
    private ImageView[] dotViews;//用于包含底部小圆点的图片
    private boolean refreshFlag = true;//是否刷新的标志
    private int count = 0;//前N天计数，用于获取以前的热闻
    private String currentDate;//应用的当前日期，当前展示的今天热闻的日期；不是系统当前日期
    //侧滑菜单
    private NewThemeBean newThemeBean;
    private HeaderAndFooterWrapper drawerHeaderAndFooterWrapper;
    private View drawerHeaderView;
    private BaseCommonAdapter<NewThemeBean.OthersBean> drawerAdapter;
    private List<NewThemeBean.OthersBean> otherNewThemes = new ArrayList<NewThemeBean.OthersBean>();
    //回到顶部、底部
    private View.OnClickListener backToTopListener;
    private View.OnClickListener backToBottomListener;

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
        if(refreshFlag){
            otherNewThemes.clear();
            otherNewThemes.addAll(newThemeBean.getOthers());
            drawerRecyclerview.setAdapter(drawerHeaderAndFooterWrapper);
        }
        drawerHeaderAndFooterWrapper.notifyDataSetChanged();
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
        mainAdapter = new BaseMultiItemTypeAdapter(MainActivity.this, stories);
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
            public void convert(ViewHolder holder, final LatestNewsBean.StoriesBean storiesBean, final int position) {
                holder.setText(R.id.txt_new_title, storiesBean.getTitle());
                ImageManager.getInstance().loadImage(MainActivity.this, (ImageView) holder.getView(R.id.img_new_picture), storiesBean.getImages().get(0), true);
                holder.setVisible(R.id.img_multipic, storiesBean.isMultipic());
                final List<LatestNewsBean.StoriesBean> storiesList = MainActivity.this.stories_only;
                //点击item打开消息内容
                holder.setOnClickListener(R.id.cardview_new_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position_only = 0;
                        for(int i = 0; i < storiesList.size(); i++){
                            if(storiesList.get(i).getId() == stories.get(position - 1).getId()){
                                position_only = i;
                            }
                        }
                        Intent intent = new Intent(MainActivity.this, NewContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "2");
                        bundle.putInt("position", position_only);
                        bundle.putSerializable("stories", (Serializable)storiesList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
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
                toolbar.setTitle(storiesBean.getHeaderTitle());
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

        /*侧滑菜单*/
        //实现打开关/闭监听
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, layoutDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                mAnimationDrawable.stop();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                mAnimationDrawable.start();
            }
        };
        //设置左上角的图标响应
        drawerToggle.syncState();
        layoutDrawer.addDrawerListener(drawerToggle);
        drawerRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        drawerRecyclerview.setItemAnimator(new DefaultItemAnimator());
        drawerAdapter = new BaseCommonAdapter<NewThemeBean.OthersBean>(MainActivity.this, R.layout.item_new_theme, otherNewThemes) {
            @Override
            protected void convert(ViewHolder holder, NewThemeBean.OthersBean newThemeBean, int position) {
                holder.setText(R.id.txt_drawer_theme_title, newThemeBean.getName());
                holder.setImageResource(R.id.img_drawer_follow_state, R.mipmap.ic_follow);
            }
        };
        drawerHeaderAndFooterWrapper = new HeaderAndFooterWrapper(drawerAdapter);
        drawerHeaderView = getLayoutInflater().inflate(R.layout.header_drawer, null);
        drawerHeaderView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        drawerHeaderAndFooterWrapper.addHeaderView(drawerHeaderView);

        //回到顶部按钮
        backToBottomListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stories.size() > 0){
                    recyclerview.smoothScrollToPosition(stories.size() - 1);
                }
            }
        };

        backToTopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stories.size() > 0){
                    recyclerview.smoothScrollToPosition(0);
                }
            }
        };

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    floatActionBtn.setVisibility(View.VISIBLE);
                }else{
                    floatActionBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    floatActionBtn.setOnClickListener(backToBottomListener);
                    floatActionBtn.setImageResource(R.mipmap.ic_to_bottom);
                }else{
                    floatActionBtn.setOnClickListener(backToTopListener);
                    floatActionBtn.setImageResource(R.mipmap.ic_to_top);
                }
            }
        });
    }

    @Override
    public void prepareData() {
        if (latestNewsBean == null) {
            getLatestNews();
        }
        if (newThemeBean == null) {
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
            stories_only.clear();
            initDots();
            //热闻轮播操作
            Observable.interval(4, TimeUnit.SECONDS).subscribe(new Subscriber<Long>(){
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long along) {
                    int currentItem = Integer.parseInt(along.toString());
                    viewpagerHeaderPicture.setCurrentItem(currentItem);
                    for (int i = 0; i < dotViews.length; i++) {
                        if (currentItem == i) {
                            dotViews[i].setSelected(true);
                        } else {
                            dotViews[i].setSelected(false);
                        }
                    }
                }
            });
        }

        //添加新闻日期分类标题的item数据
        LatestNewsBean.StoriesBean storie = new LatestNewsBean.StoriesBean();
        TimeUtil.getDateNumber();
        if (latestNewsBean.getDate().equals(currentDate)) {//如果时间是今天
            storie.setHeaderTitle(getString(R.string.main_latest_news));
            storie.setId(-1000);
        } else {//时间是过去的一天
            String date = latestNewsBean.getDate().substring(4, 6) + "月" + latestNewsBean.getDate().substring(6, 8) + "日";
            String week = TimeUtil.getWeek(latestNewsBean.getDate());
            storie.setHeaderTitle(date + " " + week);
            storie.setId(-1000-count);
        }
        stories.add(storie);
        stories.addAll(latestNewsBean.getStories());
        stories_only.addAll(latestNewsBean.getStories());

        //刷新列表
        if (refreshFlag) {
            viewpagerHeaderPicture.setAdapter(headerAdapter);
            recyclerview.setAdapter(loadMoreWrapper);
        }
        headerAdapter.notifyDataSetChanged();
//        loadMoreWrapper.notifyDataSetChanged();

        //异步比较新旧数据差异刷新recyclerview
        Observable.create(new Observable.OnSubscribe<DiffUtil.DiffResult>() {
            @Override
            public void call(Subscriber<? super DiffUtil.DiffResult> subscriber) {
                ArrayList<LatestNewsBean.StoriesBean> oldStories = (ArrayList<LatestNewsBean.StoriesBean>) mainAdapter.getData();
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LatestNewsDiffCallBack(oldStories, stories), true);
                subscriber.onNext(diffResult);
            }
        }).subscribe(new Action1<DiffUtil.DiffResult>() {

            @Override
            public void call(DiffUtil.DiffResult diffResult) {
                diffResult.dispatchUpdatesTo(loadMoreWrapper);
                mainAdapter.setData(stories);
            }
        });



        //停止刷新小圈圈动画
        if (refreshFlag && layoutSwipeRefresh.isRefreshing()) {
            layoutSwipeRefresh.setRefreshing(false);
        }
    }

    //初始化header底部小圆点
    private void initDots() {
        layoutHeaderDot.removeAllViews();
        dotViews = new ImageView[top_stories.size()];
        int dp5 = ValueUtil.dip2px(MainActivity.this, 5);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(dp5, dp5);
        mParams.setMargins(0, 0, dp5, 0);//设置小圆点左右之间的间隔

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
