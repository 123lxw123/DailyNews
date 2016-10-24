package com.lxw.dailynews.app.ui.view;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.framework.common.base.BaseMvpView;

import java.util.Map;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public interface IMainView extends BaseMvpView {
    void getLatestNews();
    void setLatestNewsBean(LatestNewsBean latestNewsBean);
}
