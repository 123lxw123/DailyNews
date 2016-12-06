package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lxw9047 on 2016/12/6.
 */

public class BeforeThemeContentBean implements Serializable{

    private List<LatestNewsBean.StoriesBean> stories;

    public List<LatestNewsBean.StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<LatestNewsBean.StoriesBean> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "BeforeThemeContentBean{" +
                "stories=" + stories +
                '}';
    }
}
