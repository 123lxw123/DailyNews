package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lxw9047 on 2016/12/14.
 */

public class RealmLatestNewsBean extends RealmObject implements Serializable {
    @PrimaryKey
    private String date;

    private RealmList<RealmStoriesBean> stories;

    private RealmList<RealmTopStoriesBean> top_stories;

    public RealmLatestNewsBean(){}
    public RealmLatestNewsBean(LatestNewsBean latestNewsBean){
        if(latestNewsBean != null){
            this.date = latestNewsBean.getDate();
            if(latestNewsBean != null && latestNewsBean.getTop_stories() != null){
                this.top_stories = new RealmList<>();
                for(int m = 0; m < latestNewsBean.getTop_stories().size(); m++){
                    top_stories.add(new RealmTopStoriesBean(latestNewsBean.getTop_stories().get(m)));
                }
            }
            if(latestNewsBean != null && latestNewsBean.getStories() != null){
                this.stories = new RealmList<>();
                for(int n = 0; n < latestNewsBean.getStories().size(); n++){
                    stories.add(new RealmStoriesBean(latestNewsBean.getStories().get(n)));
                }
            }
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<RealmStoriesBean> getStories() {
        return stories;
    }

    public void setStories(RealmList<RealmStoriesBean> stories) {
        this.stories = stories;
    }

    public RealmList<RealmTopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(RealmList<RealmTopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

}
