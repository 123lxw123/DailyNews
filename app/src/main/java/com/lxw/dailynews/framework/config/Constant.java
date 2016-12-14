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
    /** 保存图片文件夹路径**/
    public static String PATH_SAVE_PICTURE = PATH_APP + "pictures/";
    /** 启动页图片文件夹路径**/
    public static String PATH_SPLASH_PICTURE = PATH_APP + "splash/";
    /** 启动页图片路径**/
    public static String PATH_SPLASH_PICTURE_PNG = "splash_picture.png";

    /** 新闻评论的类型：长评论| 短评论**/
    public static String COMMENTS_TYPE_LONG = "long-comments";
    public static String COMMENTS_TYPE_SHORT = "short-comments";
    public static String COMMENTS_TYPE_EMPTY = "empty-comments";

    /** 通用状态值**/

    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";
    public static final String STATUS_2 = "2";
    public static final String STATUS_3 = "3";
}
