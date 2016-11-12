package com.lxw.dailynews.app.model.model;

import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/11/12.
 */

public interface INewContentModel {

    void getNewContent(String newId, HttpListener<NewContentBean> httpListener);
}
