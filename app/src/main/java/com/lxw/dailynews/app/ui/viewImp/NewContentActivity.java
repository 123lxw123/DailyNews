package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.presenter.NewContentPresenter;
import com.lxw.dailynews.app.ui.view.INewContentView;
import com.lxw.dailynews.framework.base.BaseMvpActivity;
import com.lxw.dailynews.framework.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewContentActivity extends BaseMvpActivity<INewContentView, NewContentPresenter> implements INewContentView {


    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.img_collect)
    ImageView imgCollect;
    @BindView(R.id.img_comment)
    ImageView imgComment;
    @BindView(R.id.txt_comment)
    TextView txtComment;
    @BindView(R.id.img_praise)
    ImageView imgPraise;
    @BindView(R.id.txt_praise)
    TextView txtPraise;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_header_picture)
    ImageView imgHeaderPicture;
    @BindView(R.id.txt_header_title)
    TextView txtHeaderTitle;
    @BindView(R.id.ctl_cl)
    CollapsingToolbarLayout ctlCl;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.float_action_btn)
    FloatingActionButton floatActionBtn;
    private String type;//1 点击头部viewpager热闻 2 点击列表item新闻
    private List<LatestNewsBean.StoriesBean> stories;
    private List<LatestNewsBean.TopStoriesBean> top_stories;
    private NewContentBean newContentBean = new NewContentBean();
    private int position;//定位当前是哪条新闻，用于左右滑动切换新闻

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);
        ButterKnife.bind(this);
        initActivityTag(getString(R.string.new_content));
        initView();
        type = getIntent().getStringExtra("type");
        position = getIntent().getIntExtra("position", 0);
        if (!StringUtil.isEmpty(type)) {
            if ("1".equals(type)) {
                top_stories = (List<LatestNewsBean.TopStoriesBean>) getIntent().getSerializableExtra("top_stories");
            } else {
                stories = (List<LatestNewsBean.StoriesBean>) getIntent().getSerializableExtra("stories");
            }
            prepareData();
        }
    }

    @NonNull
    @Override
    public NewContentPresenter createPresenter() {
        return new NewContentPresenter();
    }

    @Override
    public void initView() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
    }

    @Override
    public void prepareData() {
        if (top_stories != null && position < top_stories.size()) {
            getNewContent(top_stories.get(position).getId() + "");
        } else if (stories != null && position < stories.size()) {
            getNewContent(stories.get(position).getId() + "");
        }
    }

    @Override
    public void rePrepareData() {
        webview.loadDataWithBaseURL(null, newContentBean.getBody(), "text/html", "utf-8", null);
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
    public void getNewContent(String newId) {
        getPresenter().getNewContent(newId);
    }
}
