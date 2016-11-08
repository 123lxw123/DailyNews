package com.lxw.dailynews.app.ui.viewImp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewContentActivity extends AppCompatActivity {

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
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.activity_new_content)
    LinearLayout activityNewContent;

    private String type;//1 点击头部viewpager热闻 2 点击列表item新闻
    private List<LatestNewsBean.StoriesBean> stories;
    private List<LatestNewsBean.TopStoriesBean> top_stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        if("1".equals(type)){
            top_stories = (List<LatestNewsBean.TopStoriesBean>)getIntent().getSerializableExtra("top_stories");
        }else{
            stories = (List<LatestNewsBean.StoriesBean>)getIntent().getSerializableExtra("stories");
        }
        
    }
}
