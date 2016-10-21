package com.lxw.dailynews.app.bean;

import java.io.Serializable;

/**
 * 启动页图片
 * Created by Zion on 2016/10/19.
 */

public class SplashPictureInfoBean implements Serializable {
    public String text;//启动页图片版权者
    public String img;//启动页图片地址

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "SplashPictureInfoBean{" +
                "text='" + text + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
