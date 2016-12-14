package com.lxw.dailynews.app.bean;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lxw9047 on 2016/12/14.
 */

public class RealmStoriesBean extends RealmObject{
    private String title;
    private String ga_prefix;
    private boolean multipic = false;//消息是否包含多张图片（仅出现在包含多图的新闻中）默认否
    private int type;
    @PrimaryKey
    private int id;
    private String headerTitle = ""; //新闻日期分类标题，如今日热闻，11月01日 星期二
    private String image;

    public RealmStoriesBean(){}
    public RealmStoriesBean(LatestNewsBean.StoriesBean storiesBean){
        this.title = storiesBean.getTitle();
        this.ga_prefix = storiesBean.getGa_prefix();
        this.multipic = storiesBean.isMultipic();
        this.type = storiesBean.getType();
        this.id = storiesBean.getId();
        this.headerTitle = storiesBean.getHeaderTitle();
        if(storiesBean.getImages() != null && storiesBean.getImages().size() > 0){
            this.image = storiesBean.getImages().get(0);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
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

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
