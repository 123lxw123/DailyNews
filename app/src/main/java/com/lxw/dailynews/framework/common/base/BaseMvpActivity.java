package com.lxw.dailynews.framework.common.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.R;
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
        initData();
        initView();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //activity跳转动画
        overridePendingTransition(R.anim.in_right_to_left, R.anim.out_right_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        //移除activity
        ActivityStack.create().finishActivity(this);
        //activity移除动画
        overridePendingTransition(R.anim.in_left_to_right, R.anim.out_left_to_right);
    }

    public abstract void findView();
    public abstract void initData();
    public abstract void initView();

    //为activity添加一个标签
    public void initActivityTag(String activityTag){
        this.activityTag = activityTag;
        LoggerHelper.info("ActivityTag", activityTag);
    }
}
