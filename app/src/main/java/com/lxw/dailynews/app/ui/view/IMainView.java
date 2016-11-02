package com.lxw.dailynews.app.ui.view;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewThemeBean;
import com.lxw.dailynews.framework.base.BaseMvpView;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public interface IMainView extends BaseMvpView {
    void getLatestNews();
    void setLatestNewsBean(LatestNewsBean latestNewsBean);
    void getBeforeNews(String beforeDate);
    void getNewThemes();
    void setNewThemeBean(NewThemeBean newThemeBean);
    void initNewThemeList();
}
