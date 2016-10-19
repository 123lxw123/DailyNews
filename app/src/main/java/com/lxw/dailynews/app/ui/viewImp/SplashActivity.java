package com.lxw.dailynews.app.ui.viewImp;

import android.support.annotation.NonNull;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.lxw.dailynews.R;
import com.lxw.dailynews.app.presenter.SplashPresenter;
import com.lxw.dailynews.app.ui.view.ISplashView;
import com.lxw.dailynews.framework.common.base.BaseMvpActivity;

public class SplashActivity extends BaseMvpActivity<ISplashView, SplashPresenter> implements ISplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return null;
    }

    @Override
    public void findView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

}