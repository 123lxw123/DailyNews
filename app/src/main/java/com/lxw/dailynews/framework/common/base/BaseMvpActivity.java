package com.lxw.dailynews.framework.common.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.activitystack.ActivityStack;
import com.lxw.dailynews.framework.log.LoggerHelper;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity{

    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = Constant.isAllowScreenRoate;
    private String activityTag = "";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //添加activity到activity栈
        ActivityStack.create().addActivity(this);

        //是否禁止旋转屏幕
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        findView();
        initView();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        //移除activity
        ActivityStack.create().finishActivity(this);
    }

    public abstract void findView();
    public abstract void initView();
    public abstract void initData();

    //为activity添加一个标签
    public void initActivityTag(String activityTag){
        this.activityTag = activityTag;
        LoggerHelper.info("ActivityTag", activityTag);
    }
}
