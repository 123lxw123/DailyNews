package com.lxw.dailynews.app.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lxw9047 on 2016/12/15.
 */

public class RealmOthersBean extends RealmObject implements Serializable{

    private int color;
    private String thumbnail;
    private String description;
    @PrimaryKey
    private int id;
    private String name;
    private boolean frag_select = false;//是否选中的标识，

    public RealmOthersBean(){}
    public RealmOthersBean(NewsThemeBean.OthersBean othersBean){
        if(othersBean != null){
            this.color = othersBean.getColor();
            this.thumbnail = othersBean.getThumbnail();
            this.description = othersBean.getDescription();
            this.id = othersBean.getId();
            this.name = othersBean.getName();
            this.frag_select = othersBean.isFrag_select();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrag_select() {
        return frag_select;
    }

    public void setFrag_select(boolean frag_select) {
        this.frag_select = frag_select;
    }
}
