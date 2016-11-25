package com.lxw.dailynews.framework.util;

/**
 * Created by lxw9047 on 2016/11/22.
 */

public class HtmlUtil {
    /**
     * 给HTML body添加头部
     * @param bodyHTML  HTML body
     * @return
     */
    public static String getHtmlData(String bodyHTML){
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>" +
                "<style type='text/css'> body{word-wrap:break-word;font-family:Arial}</style>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
