package com.lxw.dailynews.app.model.modelImp;

import com.lxw.dailynews.app.api.HttpHelper;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.model.model.INewContentModel;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.http.HttpManager;

import rx.Observable;

/**
 * Created by Zion on 2016/11/12.
 */

public class NewContentModel implements INewContentModel {

    @Override
    public void getNewContent(final String newId, HttpListener<NewContentBean> httpListener) {
        new HttpManager<NewContentBean>(httpListener){

            @Override
            public Observable<NewContentBean> createObservable() {
                return HttpHelper.getInstance().getNewContent(newId);
            }
        };
    }
}
