package com.lxw.dailynews.framework.util;

import static android.R.attr.name;
import static android.R.attr.width;
import static android.R.string.no;

/**
 * Created by lxw9047 on 2016/11/22.
 */

public class HtmlUtil {
    /**
     * 给HTML body添加头部
     *
     * @param bodyHTML HTML body
     * @return
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width,user-scalable=no'>" +
                "<link href='file:///android_asset/news_qa.min.css' rel='stylesheet'>" +
                "<script src='file:///android_asset/zepto.min.js'></script>" +
                "<script src='file:///android_asset/img_replace.js'></script>" +
                "<script src='file:///android_asset/video.js'></script>" +
                "</head>";
        String script = "<script>" +
                "(function(){" +
                " var links = document.querySelectorAll('a[href^=\"http://daily.zhihu.com/story/\"]');" +
                "var length = links.length;" +
                "if (length) {" +
                "for (var i = 0; i < length; ++i) {" +
                "var link = links[i];" +
                "link.href = link.href.replace('http://daily.zhihu.com/story/', 'zhihudaily://story/');" +
                "}" +
                "}" +
                "})()" +
                "</script>";
        return "<html>" + head + "<body className=\"\" onload=\"onLoaded()\">" + bodyHTML + script + "</body></html>";
    }
//    public static String getHtmlData(String bodyHTML){
//        String head = "<head>" +
//                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
//                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
//                "</head>" +
//                "<style type='text/css'> body{word-wrap:break-word;font-family:Arial}</style>";
//        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
//    }
}
