package com.lxw.dailynews.app.ui.view;

import com.lxw.dailynews.app.bean.BeforeThemeContentBean;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.bean.ThemeContentBean;
import com.lxw.dailynews.framework.base.BaseMvpView;

import java.util.List;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public interface IMainView extends BaseMvpView {
    void getLatestNews();
    void setLatestNewsBean(LatestNewsBean latestNewsBean);
    void getBeforeNews(String beforeDate);
    void getNewsThemes();
    void setNewsThemeBean(NewsThemeBean newsThemeBean);
    void initNewThemeList();
    void initThemeView(NewsThemeBean.OthersBean newsThemeBean);
    void rePrepareThemeData();
    void getThemeContent(String themeId);
    void setThemeContentBean(ThemeContentBean themeContentBean);
    void getBeforeThemeContent(String themeId, String newsId);
    void setBeforeThemeContentBean(BeforeThemeContentBean beforeThemeContentBean);
    void stopRefreshAnimation();
}
