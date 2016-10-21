package com.lxw.dailynews.framework.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.R;
import com.lxw.dailynews.framework.common.Config.Constant;
import com.lxw.dailynews.framework.common.activitystack.ActivityStack;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.utils.NetUtil;

import dmax.dialog.SpotsDialog;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public abstract class BaseMvpActivity<V extends MvpView, P extends BaseMvpPresenter<V>> extends MvpActivity<V, P>{

    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRoate = Constant.isAllowScreenRoate;
    /** activity 加载中进度条 **/
    private SpotsDialog progressBar;
    /** activity标签 **/
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

    //为activity添加一个标签
    public void initActivityTag(String activityTag){
        this.activityTag = activityTag;
        LoggerHelper.info("ActivityTag", activityTag);
    }

    //判断手机是否联网，联网失败toast提示
    public boolean checkNetword(){
        return NetUtil.note_Intent(BaseMvpActivity.this);
    }

    //判断手机是否联网，联网失败toast提示
    public boolean isNetworkAvailable(){
        return NetUtil.isNetworkAvailable(BaseMvpActivity.this);
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
