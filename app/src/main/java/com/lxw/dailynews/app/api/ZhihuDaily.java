package com.lxw.dailynews.app.api;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.lxw.dailynews.app.ui.viewImp.PhotoPreviewActivity;
import com.lxw.dailynews.framework.log.LoggerHelper;

/**
 * Created by lxw9047 on 2016/12/14.
 */

public class ZhihuDaily {

    private Context context;

    public ZhihuDaily(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void clickToLoadImage(String imgUrl){
        LoggerHelper.info("jacascript", "clickToLoadImage");
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        context.startActivity(intent);
    }
    @JavascriptInterface
    public void loadImage(String imgUrl){
        LoggerHelper.info("javascript", imgUrl);
    }

    @JavascriptInterface
    public void openImage(String imgUrl){
        LoggerHelper.info("javascript", "openImage");
    }
}
