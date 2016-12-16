package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lxw9047 on 2016/12/15.
 */

public class RealmNewsContentBean extends RealmObject implements Serializable {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String ga_prefix;
    private int type;
    @PrimaryKey
    private int id;
    private String images;

    public RealmNewsContentBean(){}
    public RealmNewsContentBean(NewsContentBean newsContentBean){
        if(newsContentBean != null){
            this.body = newsContentBean.getBody();
            this.image_source = newsContentBean.getImage_source();
            this.title = newsContentBean.getTitle();
            this.image = newsContentBean.getImage();
            this.share_url = newsContentBean.getShare_url();
            this.ga_prefix = newsContentBean.getGa_prefix();
            this.type = newsContentBean.getType();
            this.id = newsContentBean.getId();
            if(newsContentBean.getImages() != null){
                this.images = newsContentBean.getImages().get(0);
            }
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
