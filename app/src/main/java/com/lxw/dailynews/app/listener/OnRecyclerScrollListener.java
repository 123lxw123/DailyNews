package com.lxw.dailynews.app.listener;

/**
 * Created by lxw9047 on 2016/10/27.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * 用于上拉加载更多
 */
public abstract class OnRecyclerScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private int firstVisibleItem;
    private int lastVisibleItem;

    public OnRecyclerScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == layoutManager.getItemCount()) {
            //滚动到底部了，可以进行数据加载等操作
//            adapter.addMoreItem(newDatas);
            loadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
    }

    public abstract void loadMore();
}
