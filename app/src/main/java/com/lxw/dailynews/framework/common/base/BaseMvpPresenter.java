package com.lxw.dailynews.framework.common.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.framework.common.application.BaseApplication;
import com.lxw.dailynews.framework.util.NetUtil;

/**
 * Created by lxw9047 on 2016/10/21.
 */

public class BaseMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    //判断手机是否联网，联网失败toast提示
    public boolean checkNetword(){
        return NetUtil.note_Intent(BaseApplication.appContext);
    }

    //判断手机是否联网，联网失败toast提示
    public boolean isNetworkAvailable(){
        return NetUtil.isNetworkAvailable(BaseApplication.appContext);
    }

    public void showProgressBar(){
        ((BaseMvpActivity)getView()).showProgressBar();
    }

    public void hideProgressBar(){
        ((BaseMvpActivity)getView()).hideProgressBar();
    }

    public void showMessage(String message){
        ((BaseMvpActivity)getView()).showMessage(message);
    }
    public void showMessage(String message, int showTime){
        ((BaseMvpActivity)getView()).showMessage(message, showTime);
    }
}
