package com.lxw.dailynews.app.service;

import android.app.IntentService;
import android.content.Intent;

import com.lxw.dailynews.app.bean.FileInfoBean;
import com.lxw.dailynews.app.bean.LatestNewsBean;
import com.lxw.dailynews.app.bean.RealmLatestNewsBean;
import com.lxw.dailynews.app.bean.RealmNewsContentBean;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Zion on 2016/12/18.
 */

public class ClearOfflineCachService extends IntentService {

    final Realm mRealm = Realm.getDefaultInstance();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ClearOfflineCachService() {
        super("ClearOfflineCachService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(FileUtil.isFileExists(Constant.PATH_OFFLINE_PICTURE)){
            File root = new File(Constant.PATH_OFFLINE_PICTURE);
            File files[] = root.listFiles();
            if(files.length > Constant.OFFLINE_DOWNLOAD_SIZE){
                //删除过时的离线图片和数据库数据
                ArrayList<FileInfoBean> fileInfos = FileUtil.getFileSortByTime(files);
                for(int i = Constant.OFFLINE_DOWNLOAD_SIZE; i < fileInfos.size(); i++){
                    FileUtil.deleteFile(new File(fileInfos.get(i).getPath()));
                    //删除数据库数据
                    final RealmLatestNewsBean realmLatestNewsBean = mRealm.where(RealmLatestNewsBean.class).equalTo("date", fileInfos.get(i).name).findFirst();
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                           for(int j = 0; j < realmLatestNewsBean.getStories().size(); j++){
                               RealmNewsContentBean realmNewsContentBean = mRealm.where(RealmNewsContentBean.class).equalTo("id", realmLatestNewsBean.getStories().get(j).getId()).findFirst();
                               realmNewsContentBean.deleteFromRealm();
                               realmLatestNewsBean.getStories().get(j).deleteFromRealm();
                           }
                            if(realmLatestNewsBean.getTop_stories() != null){
                                for(int m = 0; m < realmLatestNewsBean.getTop_stories().size(); m++){
                                    RealmNewsContentBean realmNewsContentBean = mRealm.where(RealmNewsContentBean.class).equalTo("id", realmLatestNewsBean.getTop_stories().get(m).getId()).findFirst();
                                    realmNewsContentBean.deleteFromRealm();
                                    realmLatestNewsBean.getStories().get(m).deleteFromRealm();
                                }
                            }
                            realmLatestNewsBean.deleteFromRealm();
                        }
                    });

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }
}
