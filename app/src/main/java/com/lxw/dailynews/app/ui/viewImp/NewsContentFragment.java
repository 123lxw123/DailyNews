package com.lxw.dailynews.app.ui.viewImp;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.presenter.NewContentPresenter;
import com.lxw.dailynews.app.ui.view.INewContentView;
import com.lxw.dailynews.framework.base.BaseMvpFragment;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.HtmlUtil;
import com.lxw.dailynews.framework.util.StringUtil;
import com.lxw.dailynews.framework.widget.MyNestedScrollView;
import com.lxw.dailynews.framework.widget.MyNestedScrollViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lxw9047 on 2016/11/21.
 */

public class NewsContentFragment extends BaseMvpFragment<INewContentView, NewContentPresenter> implements INewContentView {

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
    @BindView(R.id.nestedscrollview_head)
    MyNestedScrollView nestedscrollviewHead;
    @BindView(R.id.nestedscrollview_detail)
    MyNestedScrollView nestedscrollviewDetail;
    @BindView(R.id.txt_empty)
    TextView txtEmpty;

    private NewContentBean newContentBean = new NewContentBean();
    private Toolbar toolbar;
    private String newsId;//新闻id
    private View view;//新闻详情界面
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
    public NewContentPresenter createPresenter() {
        return new NewContentPresenter();
    }

    @Override
    public void setNewContent(NewContentBean newContentBean) {
        this.newContentBean = newContentBean;
        if (newContentBean != null) {
            rePrepareData();
        }
    }

    //获取消息内容
    @Override
    public void getNewContent(String newsId) {
        getPresenter().getNewContent(newsId);
    }

    @Override
    public void initView() {

        toolbar = ((NewContentActivity) getActivity()).toolbar;
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
                //头部的scrollview长度不足以滑动，给它加上一个足够长的空textview
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int)webview.getY() + webview.getHeight()));
                txtEmpty.setLayoutParams(layoutParams);
                nestedscrollviewHead.scrollTo(x / 2, y / 2);
                if(y >= 0 && y <= layoutHeaderContent.getHeight() - toolbar.getHeight()){
                    float alpht = 1 - (float)y / (layoutHeaderContent.getHeight() - toolbar.getHeight());
                    toolbar.setAlpha(alpht);
//                }else if(y - oldy > 0){
//                    toolbar.setVisibility(View.VISIBLE);
//                }else if(y - oldy < 0){
//                    toolbar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void prepareData() {
        if (!StringUtil.isEmpty(newsId)) {
            getNewContent(newsId);
        }
    }

    @Override
    public void rePrepareData() {
//加载新闻详情头部数据
        if (!StringUtil.isEmpty(newContentBean.getImage())) {
            ImageManager.getInstance().loadImage(getActivity(), imgHeaderPicture, newContentBean.getImage(), true);
        }
        if (!StringUtil.isEmpty(newContentBean.getImage_source())) {
            txtHeaderAuthor.setText(newContentBean.getImage_source());
        }
        if (!StringUtil.isEmpty(newContentBean.getTitle())) {
            txtHeaderTitle.setText(newContentBean.getTitle());
        }
        //加载新闻详情正文数据
        if (!StringUtil.isEmpty(newContentBean.getBody())) {
//            String html = newContentBean.getBody();
            String html = HtmlUtil.getHtmlData(newContentBean.getBody());
            webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
//            webview.loadDataWithBaseURL("file:///android_asset/test.html", null, "text/html", "utf-8", null);
            webview.setVisibility(View.VISIBLE);
        }
        layoutHeaderContent.setVisibility(View.VISIBLE);
    }
}