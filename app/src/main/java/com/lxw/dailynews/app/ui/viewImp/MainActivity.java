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

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lxw.dailynews.R;
import com.lxw.dailynews.app.adapter.HeaderAdapter;
import com.lxw.dailynews.app.adapter.LatestNewsDiffCallBack;
import com.lxw.dailynews.app.bean.BeforeThemeContentBean;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.base.BaseCommonAdapter;
import com.lxw.dailynews.framework.base.BaseMultiItemTypeAdapter;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.TimeUtil;
import com.lxw.dailynews.framework.util.ValueUtil;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    private NewsThemeBean newsThemeBean;
    private HeaderAndFooterWrapper drawerHeaderAndFooterWrapper;
    private View drawerHeaderView;
    private LinearLayout ll_drawer_home;
    private BaseCommonAdapter<NewsThemeBean.OthersBean> drawerAdapter;
    private List<NewsThemeBean.OthersBean> otherNewThemes = new ArrayList<NewsThemeBean.OthersBean>();
    //回到顶部、底部
    private View.OnClickListener backToTopListener;
    private View.OnClickListener backToBottomListener;
    private Subscription subscription;// 头部轮播热闻订阅

    private LinearLayoutManager linearLayoutManager;

    //主題日報
    private boolean frag_content_type = true;//主页内容的类型标识,true(首页新闻列表),false(主题新闻列表)
    private String themeId;
    private ThemeContentBean themeContentBean;//主題內容
    private BaseMultiItemTypeAdapter<LatestNewsBean.StoriesBean> mainThemeAdapter;
    private HeaderAndFooterWrapper themeHeaderAndFooterWrapper;
    private LoadMoreWrapper themeLoadMoreWrapper;
    private View themeHeaderView;
    private KenBurnsView kenBurnsView;
    private LinearLayout layout_editor;
    private LinearLayout ll_editor;
    private List<LatestNewsBean.StoriesBean> stories_theme = new ArrayList<LatestNewsBean.StoriesBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityTag("主页");
        ButterKnife.bind(this);
        latestNewsBean = (LatestNewsBean) getIntent().getSerializableExtra("latestNewsBean");
        newsThemeBean = (NewsThemeBean) getIntent().getSerializableExtra("newsThemeBean");
        initView();
        if (latestNewsBean == null || newsThemeBean == null) {
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
    public void getNewsThemes() {
        getPresenter().getNewsThemes();
    }

    public void setNewsThemeBean(NewsThemeBean newsThemeBean) {
        this.newsThemeBean = newsThemeBean;
        if (this.newsThemeBean != null) {
            initNewThemeList();
        }
    }

    @Override
    public void initNewThemeList() {
        if (refreshFlag) {
            otherNewThemes.clear();
            otherNewThemes.addAll(newsThemeBean.getOthers());
            drawerRecyclerview.setAdapter(drawerHeaderAndFooterWrapper);
        }
        drawerHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void getThemeContent(String themeId) {
        getPresenter().getThemeContent(themeId);
    }

    @Override
    public void setThemeContentBean(ThemeContentBean themeContentBean) {
        this.themeContentBean = themeContentBean;
        rePrepareThemeData();
    }

    @Override
    public void getBeforeThemeContent(String themeId, String timeStamp) {
        getPresenter().getBeforeThemeContent(themeId, timeStamp);
    }

    @Override
    public void setBeforeThemeContentBean(BeforeThemeContentBean beforeThemeContentBean) {
        stories_theme.addAll(beforeThemeContentBean.getStories());
        themeLoadMoreWrapper.notifyDataSetChanged();
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
        //设置圈内的颜色
        layoutSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright);
        //下拉刷新监听
        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFlag = true;
                if (frag_content_type) {
                    getLatestNews();
                    getNewsThemes();
                } else {
                    getThemeContent(themeId);
                }

            }
        };
        layoutSwipeRefresh.setOnRefreshListener(refreshListener);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //recyclerview adapter
        mainAdapter = new BaseMultiItemTypeAdapter(MainActivity.this, stories);
        mainAdapter.addItemViewDelegate(new ItemViewDelegate<LatestNewsBean.StoriesBean>() {//热点新闻item
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_latest_news;
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
                        for (int i = 0; i < storiesList.size(); i++) {
                            if (storiesList.get(i).getId() == stories.get(position - 1).getId()) {
                                position_only = i;
                            }
                        }
                        Intent intent = new Intent(MainActivity.this, NewsContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "2");
                        bundle.putInt("position", position_only);
                        bundle.putSerializable("stories", (Serializable) storiesList);
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
            }
        });

        //recyclerview header
        headerAndFooterWrapper = new HeaderAndFooterWrapper(mainAdapter);
        headerView = getLayoutInflater().inflate(R.layout.header_top_news, null);
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
                if (stories != null && stories.size() > 0) {
                    refreshFlag = false;
                    getBeforeNews(TimeUtil.getBeforeDate(currentDate, count));
                    count++;//前N天+1
                }
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
        drawerAdapter = new BaseCommonAdapter<NewsThemeBean.OthersBean>(MainActivity.this, R.layout.item_news_theme, otherNewThemes) {
            @Override
            protected void convert(ViewHolder holder, final NewsThemeBean.OthersBean newThemeBean, final int position) {
                holder.setText(R.id.txt_drawer_theme_title, newThemeBean.getName());
                holder.setImageResource(R.id.img_drawer_follow_state, R.mipmap.ic_follow);
                if (newThemeBean.isFrag_select()) {
                    holder.getView(R.id.rl_news_theme).setBackgroundColor(MainActivity.this.getResources().getColor(R.color.color_DDDDDD));
                } else {
                    holder.getView(R.id.rl_news_theme).setBackgroundColor(MainActivity.this.getResources().getColor(R.color.color_FFFFFF));
                }

                //点击日报主题监听
                holder.setOnClickListener(R.id.rl_news_theme, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeId = newThemeBean.getId() + "";
                        for (int i = 0; i < otherNewThemes.size(); i++) {
                            if (i == position - 1) {
                                otherNewThemes.get(i).setFrag_select(true);
                            } else {
                                otherNewThemes.get(i).setFrag_select(false);
                            }
                        }
                        drawerHeaderAndFooterWrapper.notifyDataSetChanged();
                        ll_drawer_home.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.color_FFFFFF));
                        //取消头部热闻轮播订阅
                        if (subscription != null) {
                            subscription.unsubscribe();
                        }
                        initThemeView(newThemeBean);
                    }
                });
            }
        };
        drawerHeaderAndFooterWrapper = new HeaderAndFooterWrapper(drawerAdapter);
        drawerHeaderView = getLayoutInflater().inflate(R.layout.header_drawer, null);
        ll_drawer_home = (LinearLayout) drawerHeaderView.findViewById(R.id.drawer_home);
        ll_drawer_home.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.color_DDDDDD));
        drawerHeaderView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ll_drawer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_content_type = true;
                refreshFlag = true;
                getLatestNews();
                getNewsThemes();
                ll_drawer_home.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.color_DDDDDD));
                if (layoutDrawer.isDrawerOpen(findViewById(R.id.ll_drawer))) {
                    layoutDrawer.closeDrawers();
                }
            }
        });
        drawerHeaderAndFooterWrapper.addHeaderView(drawerHeaderView);

        //回到顶部按钮
        backToBottomListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag_content_type) {
                    if (stories.size() > 0) {
                        recyclerview.smoothScrollToPosition(stories.size());
                    }
                } else {
                    if (stories_theme.size() > 0) {
                        recyclerview.smoothScrollToPosition(stories_theme.size());
                    }
                }
            }
        };

        backToTopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag_content_type) {
                    if (stories.size() > 0) {
                        recyclerview.smoothScrollToPosition(0);
                    }
                } else {
                    if (stories_theme.size() > 0) {
                        recyclerview.smoothScrollToPosition(0);
                    }
                }
            }
        };

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatActionBtn.setVisibility(View.VISIBLE);
                } else {
                    floatActionBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {//上滑
                    if (frag_content_type && firstPosition < stories.size() && StringUtil.isNotEmpty(stories.get(firstPosition).getHeaderTitle())) {
                        toolbar.setTitle(stories.get(firstPosition).getHeaderTitle());
                    }
                    floatActionBtn.setOnClickListener(backToBottomListener);
                    floatActionBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_to_bottom));
                } else {//下滑
                    if (frag_content_type && firstPosition == 0) {
                        toolbar.setTitle(getString(R.string.toolbar_title));
                    } else if (frag_content_type && StringUtil.isNotEmpty(stories.get(firstPosition).getHeaderTitle())) {
                        for (int i = firstPosition - 1; i >= 0; i--) {
                            if (StringUtil.isNotEmpty(stories.get(i).getHeaderTitle())) {
                                toolbar.setTitle(stories.get(i).getHeaderTitle());
                                break;
                            }
                        }
                    }
                    floatActionBtn.setOnClickListener(backToTopListener);
                    floatActionBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_to_top));
                }
            }
        });
    }

    //主题日報界面初始化
    @Override
    public void initThemeView(NewsThemeBean.OthersBean newsThemeBean) {
        frag_content_type = false;
        refreshFlag = true;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.toolbar_main_theme);//设置右上角的填充菜单
        String name = newsThemeBean.getName();
        toolbar.setTitle(newsThemeBean.getName());

        mainThemeAdapter = new BaseMultiItemTypeAdapter(MainActivity.this, stories_theme);
        mainThemeAdapter.addItemViewDelegate(new ItemViewDelegate<LatestNewsBean.StoriesBean>() {//热点新闻item
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_latest_news;
            }

            @Override
            public boolean isForViewType(LatestNewsBean.StoriesBean item, int position) {
                return StringUtil.isEmpty(item.getHeaderTitle());
            }

            @Override
            public void convert(ViewHolder holder, final LatestNewsBean.StoriesBean storiesBean, final int position) {
                holder.setText(R.id.txt_new_title, storiesBean.getTitle());
                if (storiesBean.getImages() != null) {
                    ImageManager.getInstance().loadImage(MainActivity.this, (ImageView) holder.getView(R.id.img_new_picture), storiesBean.getImages().get(0), true);
                    holder.setVisible(R.id.fl_picture, true);
                } else {
                    holder.setVisible(R.id.fl_picture, false);
                }

                holder.setVisible(R.id.img_multipic, storiesBean.isMultipic());
                final List<LatestNewsBean.StoriesBean> storiesList = MainActivity.this.stories_theme;
                //点击item打开消息内容
                holder.setOnClickListener(R.id.cardview_new_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, NewsContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "3");
                        bundle.putInt("position", position - 1);
                        bundle.putSerializable("stories", (Serializable) storiesList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });

        themeHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mainThemeAdapter);
        themeHeaderView = getLayoutInflater().inflate(R.layout.header_theme_content, null);
        themeHeaderView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        kenBurnsView = (KenBurnsView) themeHeaderView.findViewById(R.id.kenburnsview_picture);
        layout_editor = (LinearLayout) themeHeaderView.findViewById(R.id.layout_editor);
        layout_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(themeContentBean != null && themeContentBean.getEditors() != null && themeContentBean.getEditors().size() > 0) {
                    Intent intent = new Intent(MainActivity.this, ThemeEditorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("editorsBean", (Serializable) themeContentBean.getEditors());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        ll_editor = (LinearLayout) themeHeaderView.findViewById(R.id.ll_editor);
        themeHeaderAndFooterWrapper.addHeaderView(themeHeaderView);
        themeLoadMoreWrapper = new LoadMoreWrapper(themeHeaderAndFooterWrapper);
        themeLoadMoreWrapper.setLoadMoreView(R.layout.layout_enpty);
        themeLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (stories_theme != null && stories_theme.size() > 0) {
                    refreshFlag = false;
                    getBeforeThemeContent(themeId, stories_theme.get(stories_theme.size() - 1).getId() + "");
                }
            }
        });
        getThemeContent(newsThemeBean.getId() + "");
    }

    @Override
    public void rePrepareThemeData() {
        if (refreshFlag) {

            //初始化头部图片
            ImageManager.getInstance().loadImage(MainActivity.this, kenBurnsView, themeContentBean.getBackground(), true);

            //初始化主题主编头像列表
            ll_editor.removeAllViews();
            List<ThemeContentBean.EditorsBean> editors = themeContentBean.getEditors();
            int dp15 = ValueUtil.dip2px(MainActivity.this, 15);
            int dp30 = ValueUtil.dip2px(MainActivity.this, 30);
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(dp30, dp30);
            mParams.setMargins(dp15, 0, 0, 0);
            for (int i = 0; i < editors.size(); i++) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(mParams);
                ImageManager.getInstance().loadCircleImage(MainActivity.this, imageView, editors.get(i).getAvatar());
                ll_editor.addView(imageView);
            }

            stories_theme.clear();

            recyclerview.setAdapter(themeLoadMoreWrapper);
        }

        stories_theme.addAll(themeContentBean.getStories());
        themeLoadMoreWrapper.notifyDataSetChanged();
        //异步比较新旧数据差异刷新recyclerview
//        Observable.create(new Observable.OnSubscribe<DiffUtil.DiffResult>() {
//            @Override
//            public void call(Subscriber<? super DiffUtil.DiffResult> subscriber) {
//                ArrayList<LatestNewsBean.StoriesBean> oldStories = (ArrayList<LatestNewsBean.StoriesBean>) mainThemeAdapter.getData();
//                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LatestNewsDiffCallBack(oldStories, stories_theme), true);
//                subscriber.onNext(diffResult);
//            }
//        }).subscribe(new Action1<DiffUtil.DiffResult>() {
//
//            @Override
//            public void call(DiffUtil.DiffResult diffResult) {
//                diffResult.dispatchUpdatesTo(themeLoadMoreWrapper);
//                mainThemeAdapter.setData(stories_theme);
//            }
//        });

        stopRefreshAnimation();

        if (layoutDrawer.isDrawerOpen(findViewById(R.id.ll_drawer))) {
            layoutDrawer.closeDrawers();
        }
    }

    @Override
    public void prepareData() {
        if (latestNewsBean == null) {
            getLatestNews();
        }
        if (newsThemeBean == null) {
            getNewsThemes();
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
            toolbar.setTitle(getString(R.string.toolbar_title));
            initDots();
            //热闻轮播操作
            subscription = Observable.interval(4, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long along) {
                    int currentItem = new Long(along).intValue() % top_stories.size();
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
            storie.setId(-1000 - count);
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


        stopRefreshAnimation();
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

    @Override
    public void onBackPressed() {
        if (layoutDrawer.isDrawerOpen(findViewById(R.id.ll_drawer))) {
            layoutDrawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void stopRefreshAnimation() {

        //停止刷新小圈圈动画
        if (refreshFlag && layoutSwipeRefresh.isRefreshing()) {
            layoutSwipeRefresh.setRefreshing(false);
        }
    }
}
