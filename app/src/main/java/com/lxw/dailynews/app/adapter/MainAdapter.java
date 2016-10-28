package com.lxw.dailynews.app.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by lxw9047 on 2016/10/28.
 */

public abstract class MainAdapter<T> extends CommonAdapter<T> {
    public MainAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

}
