package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.presenter.NewContentPresenter;
import com.lxw.dailynews.app.ui.view.INewContentView;
import com.lxw.dailynews.framework.base.BaseMvpFragment;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.StringUtil;

import butterknife.ButterKnife;


/**
 * Created by lxw9047 on 2016/11/21.
 */

public class NewsContentFragment extends BaseMvpFragment<INewContentView, NewContentPresenter> implements INewContentView {

    private NewContentBean newContentBean = new NewContentBean();
    private ImageView mShareImageView;
    private ImageView mCollectImageView;
    private ImageView mCommentImageView;
    private TextView mCommentTextView;
    private ImageView mPraiseImageView;
    private TextView mPraiseTextView;
    private LinearLayout mExtraInfoLinearLayout;
    private Toolbar mToolbarToolbar;
    private ImageView mHeaderPictureImageView;
    private TextView mHeaderTitleTextView;
    private TextView mHeaderAuthorTextView;
    private FrameLayout mHeaderContentFrameLayout;
    private CollapsingToolbarLayout mClCollapsingToolbarLayout;
    private WebView mWebviewWebView;
    private FloatingActionButton mActionBtnFloatingActionButton;

    private String newsId;
    private View view;

    public static NewsContentFragment newInstance(Bundle bundle){
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
        view = inflater.inflate(R.layout.layout_news_content, container, false);
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
        mShareImageView = (ImageView) view.findViewById(R.id.img_share);
        mCollectImageView = (ImageView) view.findViewById(R.id.img_collect);
        mCommentImageView = (ImageView) view.findViewById(R.id.img_comment);
        mCommentTextView = (TextView) view.findViewById(R.id.txt_comment);
        mPraiseImageView = (ImageView) view.findViewById(R.id.img_praise);
        mPraiseTextView = (TextView) view.findViewById(R.id.txt_praise);
        mExtraInfoLinearLayout = (LinearLayout) view.findViewById(R.id.layout_extra_info);
        mToolbarToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mHeaderPictureImageView = (ImageView) view.findViewById(R.id.img_header_picture);
        mHeaderTitleTextView = (TextView) view.findViewById(R.id.txt_header_title);
        mHeaderAuthorTextView = (TextView) view.findViewById(R.id.txt_header_author);
        mHeaderContentFrameLayout = (FrameLayout) view.findViewById(R.id.layout__header_content);
        mClCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.ctl_cl);
        mWebviewWebView = (WebView) view.findViewById(R.id.webview);
        mActionBtnFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.float_action_btn);
    }

    @Override
    public void prepareData() {
        if(!StringUtil.isEmpty(newsId)){
            getNewContent(newsId);
        }
    }

    @Override
    public void rePrepareData() {
        if(!StringUtil.isEmpty(newContentBean.getImage())){
            ImageManager.getInstance().loadImage(getActivity(), mHeaderPictureImageView, newContentBean.getImage(), true);
        }
        if(!StringUtil.isEmpty(newContentBean.getImage_source())){
            mHeaderAuthorTextView.setText(newContentBean.getImage_source());
        }
        if(!StringUtil.isEmpty(newContentBean.getTitle())){
            mHeaderTitleTextView.setText(newContentBean.getTitle());
        }
        if(!StringUtil.isEmpty(newContentBean.getBody())){
            mWebviewWebView.loadDataWithBaseURL(null, newContentBean.getBody(), "text/html", "utf-8", null);
            mWebviewWebView.setVisibility(View.VISIBLE);
            mActionBtnFloatingActionButton.setVisibility(View.VISIBLE);
        }
        mHeaderContentFrameLayout.setVisibility(View.VISIBLE);
     }
}
