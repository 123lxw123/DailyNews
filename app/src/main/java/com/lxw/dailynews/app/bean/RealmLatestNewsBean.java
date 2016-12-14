package com.lxw.dailynews.app.bean;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by lxw9047 on 2016/12/14.
 */

public class RealmLatestNewsBean extends RealmObject{

    private String date;

    private RealmList<RealmStoriesBean> stories;

    private RealmList<RealmTopStoriesBean> top_stories;

    public RealmLatestNewsBean(){}
    public RealmLatestNewsBean(LatestNewsBean latestNewsBean, List<LatestNewsBean.StoriesBean> storiesBeens){
        this.date = latestNewsBean.getDate();
        this.top_stories = new RealmList<>();
        if(latestNewsBean != null && latestNewsBean.getTop_stories() != null){
            for(int m = 0; m < latestNewsBean.getTop_stories().size(); m++){
                top_stories.add(new RealmTopStoriesBean(latestNewsBean.getTop_stories().get(m)));
            }
        }
        this.stories = new RealmList<>();
        if(storiesBeens != null){
            for(int n = 0; n < storiesBeens.size(); n++){
                stories.add(new RealmStoriesBean(storiesBeens.get(n)));
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
