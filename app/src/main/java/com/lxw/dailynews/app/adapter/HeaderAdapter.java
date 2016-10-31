package com.lxw.dailynews.app.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.framework.image.ImageManager;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxw9047 on 2016/10/31.
 */

public class HeaderAdapter extends PagerAdapter {


    private List<LatestNewsBean.TopStoriesBean> dataList;
    private Context context;
    private View layoutView;
    private ViewHolder holderList[];

    public HeaderAdapter(Context context, List<LatestNewsBean.TopStoriesBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutView = LayoutInflater.from(context).inflate(R.layout.header_content, null);
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        if (dataList != null && dataList.size() > 0) {
            if(holderList == null){
                holderList = new ViewHolder[dataList.size()];
            }
            View contentView = null;
            if (holderList[position] == null) {
                ViewHolder holder = new ViewHolder();
                holder.txtHeaderTitle.setText(dataList.get(position).getTitle());
                ImageManager.getInstance().loadImage(context, holder.imgHeaderPicture, dataList.get(position).getImage(), false);
                contentView = holder.layoutHeaderContent;
                holderList[position] = holder;
                if(contentView.getParent() != null){
                    view.removeView(contentView);
                }
                view.addView(contentView);
            } else {
                contentView = holderList[position].layoutHeaderContent;
            }
            return contentView;
        } else {
            return null;
        }
    }

    public class ViewHolder {
        ViewHolder() {
            ButterKnife.bind(this, layoutView);
        }

        @BindView(R.id.layout__header_content)
        FrameLayout layoutHeaderContent;
        @BindView(R.id.img_header_picture)
        ImageView imgHeaderPicture;
        @BindView(R.id.txt_header_title)
        TextView txtHeaderTitle;
    }

}
