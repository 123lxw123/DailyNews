package com.lxw.dailynews.app.ui.viewImp;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsCommentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.presenter.NewsCommentPresenter;
import com.lxw.dailynews.app.ui.view.INewsCommentView;
import com.lxw.dailynews.framework.base.BaseMultiItemTypeAdapter;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.TimeUtil;
import com.lxw.dailynews.framework.util.ValueUtil;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsCommentActivity extends BaseMvpActivity<INewsCommentView, NewsCommentPresenter> implements INewsCommentView {

    @BindView(R.id.rv_comments)
    RecyclerView rvComments;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private NewsStoryExtraBean newsStoryExtraBean;
    private NewsCommentBean newsCommentBean;
    private String newsId;//新闻id
    private LinearLayoutManager linearLayoutManager;
    private List<NewsCommentBean.CommentsBean> comments = new ArrayList<>();
    private List<NewsCommentBean.CommentsBean> longComments = new ArrayList<>();
    private List<NewsCommentBean.CommentsBean> shortComments = new ArrayList<>();
    private BaseMultiItemTypeAdapter<NewsCommentBean.CommentsBean> adapter;
    private int requset_count = 0;

    private NewsCommentBean.CommentsBean shortCommentsBean;
    private NewsCommentBean.CommentsBean longCommentsBean;
    private NewsCommentBean.CommentsBean emptyCommentsBean;

    private boolean frag_short_comment_expand = false;

    //上拉加载更多评论
    private LoadMoreWrapper shortCommentLoadMoreWrapper;
    private LoadMoreWrapper longCommentLoadMoreWrapper;
    private boolean frag_short_comment_loadmore = false;
    private boolean frag_long_comment_loadmore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        ButterKnife.bind(this);
        newsStoryExtraBean = (NewsStoryExtraBean) getIntent().getSerializableExtra("newsStoryExtraBean");
        newsId = getIntent().getStringExtra("newsId");
        initView();
        prepareData();
    }

    @NonNull
    @Override
    public NewsCommentPresenter createPresenter() {
        return new NewsCommentPresenter();
    }

    @Override
    public void getNewsComments(String newsId, String commentsType) {
        requset_count++;
        getPresenter().getNewsComments(newsId, commentsType);
    }

    @Override
    public void getBeforeNewsComments(String newsId, String commentsType, String commentId) {
        requset_count++;
        getPresenter().getBeforeNewsComments(newsId, commentsType, commentId);
    }

    @Override
    public void setLongCommentBean(NewsCommentBean newsCommentBean) {
        this.newsCommentBean = newsCommentBean;
        rePrepareLongCommentData();
    }

    @Override
    public void setShortCommentBean(NewsCommentBean newsCommentBean) {
        this.newsCommentBean = newsCommentBean;
        rePrepareShortCommentData();
    }

    @Override
    public void stopRefreshAnimation() {
        requset_count--;
        if (requset_count == 0) {
            hideProgressBar();
        }
    }

    @Override
    public void initView() {
        toolbar.setTitle(String.format(getResources().getString(R.string.news_comments), newsStoryExtraBean.getComments() + ""));
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.toolbar_news_comment);//设置右上角的填充菜单
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setNestedScrollingEnabled(false);
        adapter = new BaseMultiItemTypeAdapter<>(NewsCommentActivity.this, comments);
        //评论头部title
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>() {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_commnet_header;
            }

            @Override
            public boolean isForViewType(NewsCommentBean.CommentsBean item, int position) {
                return item.getHeaderTitle().equals(Constant.COMMENTS_TYPE_LONG) || item.getHeaderTitle().equals(Constant.COMMENTS_TYPE_SHORT);
            }

            @Override
            public void convert(final ViewHolder holder, final NewsCommentBean.CommentsBean commentsBean, final int position) {
                if (commentsBean.getHeaderTitle().equals(Constant.COMMENTS_TYPE_LONG)) {
                    holder.setText(R.id.txt_comment_header, String.format(getResources().getString(R.string.long_comments), newsStoryExtraBean.getLong_comments() + ""));
                    holder.setVisible(R.id.img_expand, false);
                    holder.setOnClickListener(R.id.ll_header, null);
                } else {
                    holder.setText(R.id.txt_comment_header, String.format(getResources().getString(R.string.short_comments), newsStoryExtraBean.getShort_comments() + ""));
                    if (newsStoryExtraBean.getShort_comments() > 0) {
                        holder.setVisible(R.id.img_expand, true);
                        if (frag_short_comment_expand) {
                            ((ImageView) holder.getView(R.id.img_expand)).setImageResource(R.mipmap.ic_comment_fold);
                        } else {
                            ((ImageView) holder.getView(R.id.img_expand)).setImageResource(R.mipmap.ic_comment_expand);
                        }
                    } else {
                        holder.setVisible(R.id.img_expand, false);
                    }
                    holder.setOnClickListener(R.id.ll_header, new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            frag_short_comment_expand = !frag_short_comment_expand;
                            if (frag_short_comment_expand) {
                                ((ImageView) holder.getView(R.id.img_expand)).setImageResource(R.mipmap.ic_comment_fold);
                                LoggerHelper.info("NewsCommentActivity", "true");
                                getNewsComments(newsId, Constant.COMMENTS_TYPE_SHORT);
                            } else {
                                ((ImageView) holder.getView(R.id.img_expand)).setImageResource(R.mipmap.ic_comment_expand);
                                LoggerHelper.info("NewsCommentActivity", "false");
                                comments.clear();
                                comments.addAll(longComments);
                                comments.add(shortCommentsBean);
//                                rvComments.setAdapter(shortCommentLoadMoreWrapper);
                                shortCommentLoadMoreWrapper.notifyDataSetChanged();
                                rvComments.smoothScrollToPosition(0);
                            }
                        }

                    });
                }
            }
        });
        //长评论为空展示empty
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>() {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_comment_empty;
            }

            @Override
            public boolean isForViewType(NewsCommentBean.CommentsBean item, int position) {
                return item.getHeaderTitle().equals(Constant.COMMENTS_TYPE_EMPTY);
            }

            @Override
            public void convert(ViewHolder holder, NewsCommentBean.CommentsBean commentsBean, int position) {
                //屏幕高度
                WindowManager wm1 = NewsCommentActivity.this.getWindowManager();
                int height = wm1.getDefaultDisplay().getHeight();
                //评论header高度
                int dp50 = ValueUtil.dip2px(NewsCommentActivity.this, 50);
                //toolbar高度
                int toolBarHeight = toolbar.getHeight();
                //状态栏高度
                int statusBarHeight = 0;
                int statusBarId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (statusBarId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(statusBarId);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - 2 * dp50 - toolBarHeight - statusBarHeight);
                holder.getView(R.id.rl_comment_empty).setLayoutParams(layoutParams);
            }
        });
        //评论内容
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>() {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_news_comment;
            }

            @Override
            public boolean isForViewType(NewsCommentBean.CommentsBean item, int position) {
                return StringUtil.isEmpty(item.getHeaderTitle());
            }

            @Override
            public void convert(final ViewHolder holder, NewsCommentBean.CommentsBean commentsBean, int position) {
                ImageManager.getInstance().loadCircleImage(NewsCommentActivity.this, (ImageView) holder.getView(R.id.img_avatar), commentsBean.getAvatar());
                holder.setText(R.id.txt_author, commentsBean.getAuthor());
                ((TextView) holder.getView(R.id.txt_author)).getPaint().setFakeBoldText(true);
                holder.setText(R.id.txt_praise, commentsBean.getLikes() + "");
                holder.setText(R.id.txt_content, commentsBean.getContent());
                if (commentsBean.getReply_to() == null) {
                    holder.setVisible(R.id.txt_reply_to, false);
                    holder.setVisible(R.id.txt_expand, false);
                } else {
                    boolean frag_show = false;
                    String reply_to = String.format(getResources().getString(R.string.comment_reply_to), commentsBean.getReply_to().getAuthor());
                    int sp16 = ValueUtil.sp2px(NewsCommentActivity.this, 16);
                    final Spannable span = new SpannableString(reply_to + commentsBean.getReply_to().getContent());
                    span.setSpan(new AbsoluteSizeSpan(sp16), 0, reply_to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new StyleSpan(Typeface.BOLD), 0, reply_to.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_000000)), 0, reply_to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (((TextView) holder.getView(R.id.txt_reply_to)).getLineCount() > 2) {
                        frag_show = true;
                    }
                    final boolean frag = frag_show;
                    ((TextView) holder.getView(R.id.txt_reply_to)).setMaxLines(Integer.MAX_VALUE);
                    ((TextView) holder.getView(R.id.txt_reply_to)).setText(span);
                    ((TextView) holder.getView(R.id.txt_reply_to)).post(new Runnable() {
                        @Override
                        public void run() {

                            if (((TextView) holder.getView(R.id.txt_reply_to)).getLineCount() > 2) {
                                holder.setVisible(R.id.txt_expand, true);
                                if (!frag) {
                                    ((TextView) holder.getView(R.id.txt_reply_to)).setMaxLines(2);
                                    ((TextView) holder.getView(R.id.txt_reply_to)).setEllipsize(android.text.TextUtils.TruncateAt.END);
                                    holder.setText(R.id.txt_expand, NewsCommentActivity.this.getResources().getString(R.string.comment_expand));
                                } else {
                                    ((TextView) holder.getView(R.id.txt_reply_to)).setMaxLines(Integer.MAX_VALUE);
                                    ((TextView) holder.getView(R.id.txt_reply_to)).setEllipsize(android.text.TextUtils.TruncateAt.END);
                                    holder.setText(R.id.txt_expand, NewsCommentActivity.this.getResources().getString(R.string.comment_close));
                                }
                            } else {
                                holder.setVisible(R.id.txt_expand, false);
                            }
                            holder.setVisible(R.id.txt_reply_to, true);
                        }
                    });
                    //点击展开收起
                    holder.setOnClickListener(R.id.txt_expand, new View.OnClickListener() {
                        private boolean frag_expand = false;

                        @Override
                        public void onClick(View v) {
                            frag_expand = !frag_expand;
                            if (frag_expand) {
                                ((TextView) holder.getView(R.id.txt_reply_to)).setMaxLines(Integer.MAX_VALUE);
                                holder.setText(R.id.txt_expand, NewsCommentActivity.this.getResources().getString(R.string.comment_close));
                            } else {
                                ((TextView) holder.getView(R.id.txt_reply_to)).setMaxLines(2);
                                holder.setText(R.id.txt_expand, NewsCommentActivity.this.getResources().getString(R.string.comment_expand));
                            }
                        }
                    });
                }
                holder.setText(R.id.txt_time, TimeUtil.getFormatDate(commentsBean.getTime()));
            }
        });
        shortCommentLoadMoreWrapper = new LoadMoreWrapper(adapter);
        shortCommentLoadMoreWrapper.setLoadMoreView(R.layout.layout_enpty);
        shortCommentLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (frag_short_comment_expand && shortComments != null && shortComments.size() > 1) {
                    frag_short_comment_loadmore = true;
                    getBeforeNewsComments(newsId, Constant.COMMENTS_TYPE_SHORT, shortComments.get(shortComments.size() - 1).getId() + "");
                }
            }
        });

        longCommentLoadMoreWrapper = new LoadMoreWrapper(adapter);
        longCommentLoadMoreWrapper.setLoadMoreView(R.layout.layout_enpty);
        longCommentLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (longComments != null && longComments.size() > 1) {
                    frag_long_comment_loadmore = true;
                    getBeforeNewsComments(newsId, Constant.COMMENTS_TYPE_LONG, longComments.get(longComments.size() - 1).getId() + "");
                }
            }
        });
        rvComments.setAdapter(longCommentLoadMoreWrapper);
    }

    @Override
    public void prepareData() {
        longCommentsBean = new NewsCommentBean.CommentsBean();
        longCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_LONG);

        emptyCommentsBean = new NewsCommentBean.CommentsBean();
        emptyCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_EMPTY);

        shortCommentsBean = new NewsCommentBean.CommentsBean();
        shortCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_SHORT);

        getNewsComments(newsId, Constant.COMMENTS_TYPE_LONG);
    }

    @Override
    public void rePrepareData() {

    }

    @Override
    public void rePrepareLongCommentData() {
        comments.clear();
        //长评论
        if (frag_long_comment_loadmore) {//长评论加载更多
            longComments.addAll(newsCommentBean.getComments());
            comments.addAll(longComments);
        } else {
            longComments.clear();
            longComments.add(longCommentsBean);
            if (newsCommentBean == null || newsCommentBean.getComments().size() < 1) {
                longComments.add(emptyCommentsBean);
            } else {
                longComments.addAll(newsCommentBean.getComments());
            }
            comments.addAll(longComments);
        }
        if (longComments.size() - 1 < newsStoryExtraBean.getLong_comments()) {//长评论还没加载完
//            rvComments.setAdapter(longCommentLoadMoreWrapper);
            longCommentLoadMoreWrapper.notifyDataSetChanged();
        } else {//长评论已经加载完毕，添加短评论
            //短评论
            if (shortComments.size() < 1) {
                shortComments.add(shortCommentsBean);
                comments.addAll(shortComments);
            } else {
                comments.addAll(shortComments);
            }
            rvComments.setAdapter(shortCommentLoadMoreWrapper);
            shortCommentLoadMoreWrapper.notifyDataSetChanged();
        }

        frag_long_comment_loadmore = false;
    }

    @Override
    public void rePrepareShortCommentData() {
        comments.clear();
        //长评论
        comments.addAll(longComments);
        //短评论
        if (frag_short_comment_loadmore) {
            shortComments.addAll(newsCommentBean.getComments());
            comments.addAll(shortComments);
        } else {

            shortComments.clear();
            shortComments.add(shortCommentsBean);
            shortComments.addAll(newsCommentBean.getComments());
            comments.addAll(shortComments);
        }

        //刷新
//        rvComments.setAdapter(shortCommentLoadMoreWrapper);
        shortCommentLoadMoreWrapper.notifyDataSetChanged();
        if(!frag_short_comment_loadmore){
            rvComments.smoothScrollToPosition(longComments.size());
            rvComments.post(new Runnable() {
                @Override
                public void run() {
                    smoothMoveToPosition(longComments.size());
                }
            });
        }
        frag_short_comment_loadmore = false;
    }

    private void smoothMoveToPosition(int position) {


        //屏幕高度
        WindowManager wm1 = NewsCommentActivity.this.getWindowManager();
        int height = wm1.getDefaultDisplay().getHeight();
        //评论header高度
        int dp50 = ValueUtil.dip2px(NewsCommentActivity.this, 50);
        //toolbar高度
        int toolBarHeight = toolbar.getHeight();
        //状态栏高度
        int statusBarHeight = 0;
        int statusBarId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(statusBarId);
        }
        int scroll = height - statusBarHeight - toolBarHeight - dp50;
        if (rvComments.getChildAt(position).getTop() < scroll) {
            rvComments.smoothScrollBy(0, rvComments.getChildAt(position).getTop());
        } else {
            rvComments.smoothScrollBy(0, scroll);
        }


//        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
//        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
//        if(position <= lastPosition){
//            int top = rvComments.getChildAt(position - firstPosition).getTop();
//            rvComments.smoothScrollBy(0, top);
//        }
    }
}
