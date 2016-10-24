package com.lxw.dailynews.app.ui.viewImp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.presenter.MainPresenter;
import com.lxw.dailynews.app.ui.view.IMainView;
import com.lxw.dailynews.framework.common.base.BaseMvpActivity;

public class MainActivity extends BaseMvpActivity<IMainView, MainPresenter> implements IMainView{
    private LatestNewsBean latestNewsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latestNewsBean = (LatestNewsBean)getIntent().getSerializableExtra("latestNewsBean");
        if(latestNewsBean == null){
            getLatestNews();
        }else{
            initView();
        }
    }

    public void setLatestNewsBean(LatestNewsBean latestNewsBean) {
        this.latestNewsBean = latestNewsBean;
        if(this.latestNewsBean != null){
            initView();
        }
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

    }
}
