package com.lxw.dailynews.framework.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.lxw.dailynews.app.ui.viewImp.MainActivity;
import com.lxw.dailynews.framework.log.LoggerHelper;
import com.lxw.dailynews.framework.util.SharePreferencesUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class BaseApplication extends Application implements
        Thread.UncaughtExceptionHandler {


    public static Context appContext;// 全局Context
    public static int appStartCount;// app启动次数
    private final String APP_START_COUNT = "APP_START_COUNT";


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        appStartCount = SharePreferencesUtil.getIntSharePreferences(appContext, APP_START_COUNT, 0);
        appStartCount = appStartCount + 1;
        SharePreferencesUtil.setIntSharePreferences(appContext, APP_START_COUNT, appStartCount);

        //友盟分享配置
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        UMShareAPI.get(this);
    }

    public static Context getappContext() {
        return appContext;
    }

    public static int getAppStartCount() {
        return appStartCount;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LoggerHelper.info("UncaughtException", "UncaughtException");
        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
