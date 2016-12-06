package com.lxw.dailynews.app.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.ui.viewImp.NewsContentFragment;

import java.util.List;

/**
 * Created by lxw9047 on 2016/11/21.
 */

public class NewsContentAdapter extends FragmentStatePagerAdapter {

    private String type;//1 点击头部viewpager热闻 2 点击列表item新闻
    private List<LatestNewsBean.StoriesBean> stories;
    private List<LatestNewsBean.TopStoriesBean> top_stories;
    private int position;//定位当前是哪条新闻，用于左右滑动切换新闻

    public NewsContentAdapter(FragmentManager fragmentManager, String type, List data, int position) {
        super(fragmentManager);
        this.type = type;
        if ("1".equals(type)) {
            top_stories = (List<LatestNewsBean.TopStoriesBean>) data;
        } else {
            stories = (List<LatestNewsBean.StoriesBean>) data;
        }
        this.position = position;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        if ("1".equals(type)) {
            bundle.putString("newsId", top_stories.get(position).getId() + "");
        } else {
            bundle.putString("newsId", stories.get(position).getId() + "");
        }
        bundle.putString("type", type);
        return NewsContentFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        if ("1".equals(type)) {
            return top_stories.size();
        } else {
            return stories.size();
        }
    }
}
