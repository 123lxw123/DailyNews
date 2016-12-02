package com.lxw.dailynews.app.bean;

import java.io.Serializable;

/**
 * Created by lxw9047 on 2016/12/2.
 * 新闻额外信息
 */

public class NewsStoryExtra implements Serializable{
    /**
     * long_comments : 12
     * popularity : 2
     * short_comments : 76
     * comments : 88
     *
     long_comments : 长评论总数
     popularity : 点赞总数
     short_comments : 短评论总数
     comments : 评论总数
     */

    private int long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "NewsStoryExtra{" +
                "long_comments=" + long_comments +
                ", popularity=" + popularity +
                ", short_comments=" + short_comments +
                ", comments=" + comments +
                '}';
    }
}
