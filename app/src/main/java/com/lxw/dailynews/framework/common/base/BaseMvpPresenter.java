package com.lxw.dailynews.framework.common.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by lxw9047 on 2016/10/21.
 */

public class BaseMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

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
