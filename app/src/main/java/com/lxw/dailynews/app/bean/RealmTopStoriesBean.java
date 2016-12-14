package com.lxw.dailynews.app.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lxw9047 on 2016/12/14.
 */

public class RealmTopStoriesBean extends RealmObject {
    private String image;
    private int type;
    @PrimaryKey
    private int id;
    private String ga_prefix;
    private String title;

    public RealmTopStoriesBean(){}
    public RealmTopStoriesBean(LatestNewsBean.TopStoriesBean topStoriesBean){
        this.title = topStoriesBean.getTitle();
        this.ga_prefix = topStoriesBean.getGa_prefix();
        this.type = topStoriesBean.getType();
        this.id = topStoriesBean.getId();
        this.image = topStoriesBean.getImage();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
