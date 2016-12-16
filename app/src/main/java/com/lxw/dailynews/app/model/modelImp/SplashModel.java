package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.NewsThemeBean;
import com.lxw.dailynews.app.bean.RealmLatestNewsBean;
import com.lxw.dailynews.app.bean.RealmNewsThemeBean;
import com.lxw.dailynews.app.bean.RealmOthersBean;
import com.lxw.dailynews.app.bean.SplashPictureInfoBean;
import com.lxw.dailynews.app.model.model.ISplashModel;
import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.app.service.OfflineDownloadService;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;
import com.lxw.dailynews.framework.util.HtmlUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

import static android.R.id.list;
import static com.lxw.dailynews.framework.config.Constant.getDownloadFilePath;

/**
 * Created by Zion on 2016/10/16.
 * 启动页数据
 */

public class SplashModel implements ISplashModel {

    //获取启动页图片信息
    @Override
    public void getSplashPictureInfo(HttpListener<SplashPictureInfoBean> httpListener) {
        new HttpManager<SplashPictureInfoBean>(httpListener) {
            @Override
            public Observable<SplashPictureInfoBean> createObservable() {
                return HttpHelper.getInstance().getSplashPictureInfo();
            }
        };
    }

    //获取主页最新消息
    @Override
    public void getLatestNews(HttpListener<LatestNewsBean> httpListener) {
        new HttpManager<LatestNewsBean>(httpListener){

            @Override
            public Observable<LatestNewsBean> createObservable() {
                return HttpHelper.getInstance().getLatestNews();
            }
        };
    }
    @Override
    public LatestNewsBean getOfflineLatestNews(String date) {
        LatestNewsBean latestNewsBean = null;
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<RealmLatestNewsBean> result = mRealm.where(RealmLatestNewsBean.class).findAll();
        if(result == null || result.size() < 1){
            return null;
        }
        if(result.size() > 0){
            result.sort("date", Sort.DESCENDING);
            if(date == null){
                latestNewsBean = new LatestNewsBean(result.get(0));
            }else{
                latestNewsBean = new LatestNewsBean(mRealm.where(RealmLatestNewsBean.class).equalTo("date", date).findFirst());
            }
        }
        mRealm.close();
        if(latestNewsBean != null && latestNewsBean.getStories() != null){
            for(int i = 0; i < latestNewsBean.getStories().size(); i++){
                //替换图片地址
                String url = latestNewsBean.getStories().get(i).getImages().get(0);
                ArrayList<String> list = new ArrayList<>();
                list.add(getDownloadFilePath(latestNewsBean.getDate(), Constant.STORIES_TYPE_COMMON, latestNewsBean.getStories().get(i).getId(), HtmlUtil.getFileName(url)));
                latestNewsBean.getStories().get(i).setImages(list);
            }
            for(int i = 0; i < latestNewsBean.getTop_stories().size(); i++){
                //替换图片地址
                String url = latestNewsBean.getTop_stories().get(i).getImage();
                String replyUrl = Constant.getDownloadFilePath(latestNewsBean.getDate(), Constant.STORIES_TYPE_TOP, latestNewsBean.getTop_stories().get(i).getId(), HtmlUtil.getFileName(url));
                latestNewsBean.getTop_stories().get(i).setImage(replyUrl);
            }
        }else{
            return null;
        }
        return latestNewsBean;
    }

    @Override
    public void getNewsThemes(HttpListener<NewsThemeBean> httpListener) {
        new HttpManager<NewsThemeBean>(httpListener){

            @Override
            public Observable<NewsThemeBean> createObservable() {
                return HttpHelper.getInstance().getNewsThemes();
            }
        };
    }
    @Override
    public NewsThemeBean getOfflineNewsThemes() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmNewsThemeBean result = mRealm.where(RealmNewsThemeBean.class).findFirst();
        if(result == null){
            return null;
        }
        NewsThemeBean newsThemeBean = new NewsThemeBean(result);
        mRealm.close();
        return  newsThemeBean;
    }
}
