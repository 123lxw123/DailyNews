package com.lxw.dailynews.app.ui.viewImp;

import android.graphics.Color;
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
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.TimeUtil;
import com.lxw.dailynews.framework.util.ValueUtil;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
    private List<NewsCommentBean.CommentsBean> comments = new ArrayList<>();
    private List<NewsCommentBean.CommentsBean> longComments = new ArrayList<>();
    private List<NewsCommentBean.CommentsBean> shortComments = new ArrayList<>();
    private BaseMultiItemTypeAdapter<NewsCommentBean.CommentsBean> adapter;
    private int requset_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_comment);
        ButterKnife.bind(this);
        newsStoryExtraBean = (NewsStoryExtraBean)getIntent().getSerializableExtra("newsStoryExtraBean");
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
        requset_count ++;
        getPresenter().getNewsComments(newsId, commentsType);
    }

    @Override
    public void setLongCommentBean(NewsCommentBean newsCommentBean) {
        this.newsCommentBean = newsCommentBean;
        rePrepareLongCommentData();
    }

    @Override
    public void setShortCommentBean(NewsCommentBean newsCommentBean) {

    }

    @Override
    public void stopRefreshAnimation() {
        requset_count --;
        if(requset_count == 0){
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setNestedScrollingEnabled(false);
        adapter = new BaseMultiItemTypeAdapter<>(NewsCommentActivity.this, comments);
        //评论头部title
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>(){

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_commnet_header;
            }

            @Override
            public boolean isForViewType(NewsCommentBean.CommentsBean item, int position) {
                return item.getHeaderTitle().equals(Constant.COMMENTS_TYPE_LONG) || item.getHeaderTitle().equals(Constant.COMMENTS_TYPE_SHORT);
            }

            @Override
            public void convert(ViewHolder holder, NewsCommentBean.CommentsBean commentsBean, int position) {
                if(commentsBean.getHeaderTitle().equals(Constant.COMMENTS_TYPE_LONG)){
                    holder.setText(R.id.txt_comment_header, String.format(getResources().getString(R.string.long_comments), newsStoryExtraBean.getLong_comments() + ""));
                    holder.setVisible(R.id.img_expand, false);
                }else{
                    holder.setText(R.id.txt_comment_header, String.format(getResources().getString(R.string.short_comments), newsStoryExtraBean.getShort_comments() + ""));
                    holder.setVisible(R.id.img_expand, true);
                }
            }
        });
        //长评论为空展示empty
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>(){

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
                int statusBarHeight  = 0;
                int statusBarId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (statusBarId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(statusBarId);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - 2 * dp50 - toolBarHeight - statusBarHeight);
                holder.getView(R.id.rl_comment_empty).setLayoutParams(layoutParams);
            }
        });
        //评论内容
        adapter.addItemViewDelegate(new ItemViewDelegate<NewsCommentBean.CommentsBean>(){

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_news_comment;
            }

            @Override
            public boolean isForViewType(NewsCommentBean.CommentsBean item, int position) {
                return StringUtil.isEmpty(item.getHeaderTitle());
            }

            @Override
            public void convert(ViewHolder holder, NewsCommentBean.CommentsBean commentsBean, int position) {
                ImageManager.getInstance().loadCircleImage(NewsCommentActivity.this, (ImageView) holder.getView(R.id.img_avatar), commentsBean.getAvatar());
                holder.setText(R.id.txt_author, commentsBean.getAuthor());
                ((TextView)holder.getView(R.id.txt_author)).getPaint().setFakeBoldText(true);
                holder.setText(R.id.txt_praise, commentsBean.getLikes() + "");
                holder.setText(R.id.txt_content, commentsBean.getContent());
                if(commentsBean.getReply_to() == null){
                    holder.setVisible(R.id.txt_reply_to, false);
                    holder.setVisible(R.id.txt_expand, false);
                }else{
                    holder.setVisible(R.id.txt_reply_to, true);
                    String reply_to = String.format(getResources().getString(R.string.comment_reply_to), commentsBean.getReply_to().getAuthor());
                    int sp16 = ValueUtil.sp2px(NewsCommentActivity.this, 16);
                    Spannable span = new SpannableString(reply_to + commentsBean.getReply_to().getContent());
                    span.setSpan(new AbsoluteSizeSpan(sp16), 0, reply_to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new StyleSpan(Typeface.BOLD), 0, reply_to.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_000000)), 0, reply_to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((TextView)holder.getView(R.id.txt_reply_to)).setText(span);
                    if(((TextView)holder.getView(R.id.txt_reply_to)).getLineCount() > 2){
                        holder.setVisible(R.id.txt_expand, true);
                        ((TextView)holder.getView(R.id.txt_reply_to)).setMaxLines(2);
                        ((TextView)holder.getView(R.id.txt_reply_to)).setEllipsize(android.text.TextUtils.TruncateAt.END);
                    }else{
                        holder.setVisible(R.id.txt_expand, false);
                    }
                }
                holder.setText(R.id.txt_time, TimeUtil.getFormatDate(commentsBean.getTime()));
            }
        });

        rvComments.setAdapter(adapter);
    }

    @Override
    public void prepareData() {
        getNewsComments(newsId, Constant.COMMENTS_TYPE_LONG);
    }

    @Override
    public void rePrepareData() {

    }

    @Override
    public void rePrepareLongCommentData() {
        comments.clear();
        //长评论
        longComments.clear();
        NewsCommentBean.CommentsBean longCommentsBean = new NewsCommentBean.CommentsBean();
        longCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_LONG);
        longComments.add(longCommentsBean);
        if(newsCommentBean == null || newsCommentBean.getComments().size() < 1){
            NewsCommentBean.CommentsBean emptyCommentsBean = new NewsCommentBean.CommentsBean();
            emptyCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_EMPTY);
            longComments.add(emptyCommentsBean);
        }else{
            longComments.addAll(newsCommentBean.getComments());
        }
        comments.addAll(longComments);
        //短评论
        if(shortComments.size() < 1){
            NewsCommentBean.CommentsBean shortCommentsBean = new NewsCommentBean.CommentsBean();
            shortCommentsBean.setHeaderTitle(Constant.COMMENTS_TYPE_SHORT);
            shortComments.add(shortCommentsBean);
            comments.addAll(shortComments);
        }else {
            comments.addAll(shortComments);
        }
        //刷新
        adapter.notifyDataSetChanged();
    }

    @Override
    public void rePrepareShortCommentData() {

    }
}
