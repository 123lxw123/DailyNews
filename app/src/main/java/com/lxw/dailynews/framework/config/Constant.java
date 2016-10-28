package com.lxw.dailynews.framework.config;

import android.os.Environment;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class Constant {
    /** 是否debug模式 **/
    public static boolean isDebug = true;
    /** 是否旋转屏幕**/
    public static boolean isAllowScreenRoate = false;
//    /** 启动页图片信息URL**/
//    public static String URL_SPLASH_PICTURE_INFO = "http://news-at.zhihu.com/api/4/start-image/720*1280";
//    /** 最新消息URL**/
//    public static String URL_LATEST_NEWS = "http://news-at.zhihu.com/api/4/news/latest";


    /** 内置SD卡路径**/
    public static String PATH__SD_CARD = Environment.getExternalStorageDirectory().toString() + "/";
    /** APP文件夹路径**/
    public static String PATH_APP = PATH__SD_CARD + "DailyNews/";
    /** 启动页图片文件夹路径**/
    public static String PATH_SPLASH_PICTURE = PATH_APP + "picture/";
    /** 启动页图片路径**/
    public static String PATH_SPLASH_PICTURE_PNG = "splash_picture.png";

}
