package com.lxw.dailynews.app.ui.viewImp;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.NewsStoryExtraBean;
import com.lxw.dailynews.app.presenter.NewsContentPresenter;
import com.lxw.dailynews.app.ui.view.INewsContentView;
import com.lxw.dailynews.framework.base.BaseMvpFragment;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.HtmlUtil;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.util.ValueUtil;
import com.lxw.dailynews.framework.widget.MyNestedScrollView;
import com.lxw.dailynews.framework.widget.MyNestedScrollViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.type;


/**
 * Created by lxw9047 on 2016/11/21.
 */

public class NewsContentFragment extends BaseMvpFragment<INewsContentView, NewsContentPresenter> implements INewsContentView {

    @BindView(R.id.img_header_picture)
    ImageView imgHeaderPicture;
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.layout_header_content)
    FrameLayout layoutHeaderContent;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.txt_header_author)
    TextView txtHeaderAuthor;
    @BindView(R.id.nestedscrollview_detail)
    MyNestedScrollView nestedscrollviewDetail;

    private NewsContentBean newsContentBean = new NewsContentBean();
    private NewsStoryExtraBean newsStoryExtraBean = new NewsStoryExtraBean();
    private Toolbar toolbar;
    private ImageView imgShare;
    private ImageView imgCollect;
    private TextView txtComment;
    private TextView txtPraise;
    private String newsId;//新闻id
    private View view;//新闻详情界面
    private String type;
    private LinearLayout.LayoutParams layoutParams;

    public static NewsContentFragment newInstance(Bundle bundle) {
        NewsContentFragment fragment = new NewsContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsId = getArguments().getString("newsId");
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_content, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareData();
    }

    @Override
    public NewsContentPresenter createPresenter() {
        return new NewsContentPresenter();
    }

    @Override
    public void setNewContent(NewsContentBean newsContentBean) {
        this.newsContentBean = newsContentBean;
        if (newsContentBean != null) {
            rePrepareData();
        }
    }

    public void setNewsStoryExtraBean(NewsStoryExtraBean newsStoryExtraBean) {
        this.newsStoryExtraBean = newsStoryExtraBean;
        if(newsStoryExtraBean != null){
            initNewsStoryExtra();
        }
    }

    private void initNewsStoryExtra() {
        toolbar.setAlpha(1);
        toolbar.setVisibility(View.VISIBLE);
        txtComment.setText(newsStoryExtraBean.getComments() + "");
        txtPraise.setText(newsStoryExtraBean.getPopularity() + "");
    }

    //获取消息内容
    @Override
    public void getNewsContent(String newsId) {
        getPresenter().getNewsContent(newsId);
    }

    //获取消息额外信息（赞、评论等）
    @Override
    public void getNewsStoryExtra(String newsId) {
        getPresenter().getNewsStoryExtra(newsId);
    }

    @Override
    public void initView() {

        toolbar = ((NewsContentActivity) getActivity()).toolbar;
        imgShare = ((NewsContentActivity) getActivity()).imgShare;
        imgCollect = ((NewsContentActivity) getActivity()).imgCollect;
        txtComment = ((NewsContentActivity) getActivity()).txtComment;
        txtPraise = ((NewsContentActivity) getActivity()).txtPraise;
        //webview初始化
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){

        });
        webview.setWebChromeClient(new WebChromeClient(){

        });
        //webview内容自适应
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        //scrollview滑动监听
        nestedscrollviewDetail.setMyNestedScrollViewListener(new MyNestedScrollViewListener() {
            @Override
            public void onMyNestedScrollChanged(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
                //头部视差效果
                ObjectAnimator
                        .ofFloat(layoutHeaderContent, "translationY", oldy / 2, y / 2)
                        .start();
                int dp200 = ValueUtil.dip2px(NewsContentFragment.this.getActivity(), 200);
                //toolbar透明效果
                if(y >= 0 && y <= dp200 - toolbar.getHeight()){
                    float alpht = 1 - (float)y / (dp200 - toolbar.getHeight());
                    toolbar.setAlpha(alpht);
                    toolbar.setVisibility(View.VISIBLE);
                }else if(y - oldy > 0){
                    toolbar.setVisibility(View.GONE);
                }else if(y - oldy < 0){
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setAlpha(1);
                }
            }
        });

        if("3".equals(type)){
            layoutHeaderContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void prepareData() {
        if (!StringUtil.isEmpty(newsId)) {
            getNewsContent(newsId);
            getNewsStoryExtra(newsId);
        }
    }

    @Override
    public void rePrepareData() {
//加载新闻详情头部数据
        if (!StringUtil.isEmpty(newsContentBean.getImage())) {
            ImageManager.getInstance().loadImage(getActivity(), imgHeaderPicture, newsContentBean.getImage(), true);
        }
        if (!StringUtil.isEmpty(newsContentBean.getImage_source())) {
            txtHeaderAuthor.setText(newsContentBean.getImage_source());
        }
        if (!StringUtil.isEmpty(newsContentBean.getTitle())) {
            txtHeaderTitle.setText(newsContentBean.getTitle());
        }
        //加载新闻详情正文数据
        if (!StringUtil.isEmpty(newsContentBean.getBody())) {
//            String html = newsContentBean.getBody();
            String html = HtmlUtil.getHtmlData(newsContentBean.getBody());
            webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
//            webview.loadDataWithBaseURL("file:///android_asset/test.html", null, "text/html", "utf-8", null);
            webview.setVisibility(View.VISIBLE);
        }
        if("3".equals(type)){
            layoutHeaderContent.setVisibility(View.GONE);
        }else {
            layoutHeaderContent.setVisibility(View.VISIBLE);
        }
    }
}