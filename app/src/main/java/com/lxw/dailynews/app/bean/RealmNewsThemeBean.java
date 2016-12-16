package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by lxw9047 on 2016/12/15.
 */

public class RealmNewsThemeBean extends RealmObject implements Serializable{

    private int limit;
    private RealmList<RealmOthersBean> others;

    public RealmNewsThemeBean(){}
    public RealmNewsThemeBean(NewsThemeBean newsThemeBean){
        if(newsThemeBean != null){
            this.limit = newsThemeBean.getLimit();
            if(newsThemeBean.getOthers() != null){
                others = new RealmList<>();
                for(int n = 0; n < newsThemeBean.getOthers().size(); n++){
                    others.add(new RealmOthersBean(newsThemeBean.getOthers().get(n)));
                }
            }
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public RealmList<RealmOthersBean> getOthers() {
        return others;
    }

    public void setOthers(RealmList<RealmOthersBean> others) {
        this.others = others;
    }
}
