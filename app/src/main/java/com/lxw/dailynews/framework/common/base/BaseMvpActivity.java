package com.lxw.dailynews.framework.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.delegate.ActivityMvpDelegateCallback;
import com.lxw.dailynews.R;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.activitystack.ActivityStack;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.utils.NetUtil;

import dmax.dialog.SpotsDialog;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends BaseMvpPresenter<V>> extends MvpActivity<V, P> {

    /** 是否旋转屏幕 **/
    private boolean isAllowScreenRoate = Constant.isAllowScreenRoate;
    /** activity 加载中进度条 **/
    private SpotsDialog progressBar;
    /** activity标签 **/
    private String activityTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //添加activity到activity栈
        ActivityStack.getInstance().addActivity(this);

        //是否旋转屏幕
        if (isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //隐藏虚拟按键
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //activity跳转动画
        overridePendingTransition(R.anim.in_left_to_right, R.anim.out_left_to_right);
    }

    @Override
    public void finish() {
        super.finish();
        //移除activity
        ActivityStack.getInstance().finishActivity(this);
        //activity移除动画
        overridePendingTransition(R.anim.in_right_to_left, R.anim.out_right_to_left);
    }

    //为activity添加一个标签
    public void initActivityTag(String activityTag){
        this.activityTag = activityTag;
        LoggerHelper.info("ActivityTag", activityTag);
    }

    //显示加载中进度条
    public void showProgressBar(){
        if(progressBar == null){
            progressBar = new SpotsDialog(BaseMvpActivity.this);
        }
        if(!progressBar.isShowing()){
            progressBar.show();
        }
    }

    //隐藏加载中进度条
    public void hideProgressBar(){
        if(progressBar != null && progressBar.isShowing()){
            progressBar.dismiss();
        }
    }

    //toast提示信息
    public void showMessage(String message){
        showMessage(message, Toast.LENGTH_SHORT);
    }
    public void showMessage(String message, int showTime){
        Toast.makeText(BaseMvpActivity.this, message, showTime).show();
    }
}
