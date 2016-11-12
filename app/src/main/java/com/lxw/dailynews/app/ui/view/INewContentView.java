package com.lxw.dailynews.app.ui.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.framework.base.BaseMvpView;

/**
 * Created by Zion on 2016/11/12.
 */

public interface INewContentView extends BaseMvpView {
    void setNewContent(NewContentBean newContentBean);
    void getNewContent(String newId);

}
