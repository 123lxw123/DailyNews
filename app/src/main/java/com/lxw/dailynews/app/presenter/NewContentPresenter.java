package com.lxw.dailynews.app.presenter;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewContentBean;
import com.lxw.dailynews.app.model.model.INewContentModel;
import com.lxw.dailynews.app.model.modelImp.NewContentModel;
import com.lxw.dailynews.app.ui.view.INewContentView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.base.BaseMvpPresenter;
import com.lxw.dailynews.framework.http.HttpListener;

/**
 * Created by Zion on 2016/11/12.
 */

public class NewContentPresenter extends BaseMvpPresenter<INewContentView> {
    private INewContentModel newContentModel;

    public NewContentPresenter() {
        newContentModel = new NewContentModel();
    }

    //获取消息内容
    public void getNewContent(String newId) {
        if (checkNetword()) {
            newContentModel.getNewContent(newId, new HttpListener<NewContentBean>() {
                @Override
                public void onSuccess(NewContentBean response) {
                    if (response != null) {
                        getView().setNewContent(response);
                    } else {
                        showMessage(BaseApplication.appContext.getString(R.string.error_request_failure));
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    showMessage(error.getMessage());
                }
            });
        }
    }
}
