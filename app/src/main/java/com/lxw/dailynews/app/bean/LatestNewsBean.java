package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 最新消息
 * Created by Zion on 2016/10/19.
 */

public class LatestNewsBean implements Serializable {

    /*
        date : 日期
        stories : 当日新闻
        title : 新闻标题
        images : 图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
        ga_prefix : 供 Google Analytics 使用
        type : 作用未知
        id : url 与 share_url 中最后的数字（应为内容的 id）
        multipic : 消息是否包含多张图片（仅出现在包含多图的新闻中）
        top_stories : 界面顶部 ViewPager 滚动显示的显示内容（子项格式同上）（请注意区分此处的 image 属性与 stories 中的 images 属性）

        */
    private String date;

    private List<StoriesBean> stories;

    private List<TopStoriesBean> top_stories;

    public LatestNewsBean() {
    }

    public LatestNewsBean(RealmLatestNewsBean realmLatestNewsBean) {
        if(realmLatestNewsBean != null){
            this.date = realmLatestNewsBean.getDate();
            if (realmLatestNewsBean != null && realmLatestNewsBean.getStories() != null) {
                this.stories = new ArrayList<>();
                for (int i = 0; i < realmLatestNewsBean.getStories().size(); i++) {
                    this.stories.add(new StoriesBean(realmLatestNewsBean.getStories().get(i)));
                }
            }
            if (realmLatestNewsBean != null && realmLatestNewsBean.getTop_stories() != null) {
                this.top_stories = new ArrayList<>();
                for (int i = 0; i < realmLatestNewsBean.getTop_stories().size(); i++) {
                    this.top_stories.add(new TopStoriesBean(realmLatestNewsBean.getTop_stories().get(i)));
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

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean implements Serializable {
        private String title;
        private String ga_prefix;
        private boolean multipic = false;//消息是否包含多张图片（仅出现在包含多图的新闻中）默认否
        private int type;
        private int id;
        private String headerTitle = ""; //新闻日期分类标题，如今日热闻，11月01日 星期二
        private List<String> images;

        public StoriesBean() {
        }

        public StoriesBean(RealmStoriesBean realmStoriesBean) {
            if(realmStoriesBean != null){
                this.title = realmStoriesBean.getTitle();
                this.ga_prefix = realmStoriesBean.getGa_prefix();
                this.multipic = realmStoriesBean.isMultipic();
                this.type = realmStoriesBean.getType();
                this.id = realmStoriesBean.getId();
                this.headerTitle = realmStoriesBean.getHeaderTitle();
                images = new ArrayList<>();
                images.add(realmStoriesBean.getImage());
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

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "title='" + title + '\'' +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", multipic=" + multipic +
                    ", type=" + type +
                    ", id=" + id +
                    ", headerTitle='" + headerTitle + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public static class TopStoriesBean implements Serializable {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public TopStoriesBean() {
        }

        public TopStoriesBean(RealmTopStoriesBean realmTopStoriesBean) {
            if(realmTopStoriesBean != null){
                this.title = realmTopStoriesBean.getTitle();
                this.ga_prefix = realmTopStoriesBean.getGa_prefix();
                this.type = realmTopStoriesBean.getType();
                this.id = realmTopStoriesBean.getId();
                this.image = realmTopStoriesBean.getImage();
            }
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

        @Override
        public String toString() {
            return "TopStoriesBean{" +
                    "image='" + image + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LatestNewsBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
