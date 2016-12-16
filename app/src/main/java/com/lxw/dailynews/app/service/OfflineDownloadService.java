package com.lxw.dailynews.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lxw.dailynews.R;
import com.lxw.dailynews.app.bean.NewsContentBean;
import com.lxw.dailynews.app.bean.RealmLatestNewsBean;
import com.lxw.dailynews.app.bean.RealmNewsContentBean;
import com.lxw.dailynews.app.bean.RealmNewsThemeBean;
import com.lxw.dailynews.app.bean.RealmOthersBean;
import com.lxw.dailynews.app.bean.RealmStoriesBean;
import com.lxw.dailynews.app.bean.RealmTopStoriesBean;
import com.lxw.dailynews.app.model.model.IMainModel;
import com.lxw.dailynews.app.model.model.INewsContentModel;
import com.lxw.dailynews.app.model.modelImp.MainModel;
import com.lxw.dailynews.app.model.modelImp.NewsContentModel;
import com.lxw.dailynews.app.ui.viewImp.MainActivity;
import com.lxw.dailynews.framework.config.Constant;
import com.lxw.dailynews.framework.http.HttpListener;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.HtmlUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class OfflineDownloadService extends Service {

    private List<RealmLatestNewsBean> latestNewsBeans;
    private RealmNewsThemeBean newsThemeBean;
    private Notification notification;
    private Realm mRealm;
    private int totalSize;//总的新闻条数
    private INewsContentModel newsContentModel;
    private String baseFilePath;//基础的保存路径
    private int count;//已离线下载的新闻条数
    private int doCount;//执行离线下载的次数（不一定成功）

    private RemoteViews rv;//通知栏通知的布局
    private NotificationManager nm;

    public OfflineDownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rv = new RemoteViews(getPackageName(), R.layout.layout_download_notifition);
        onShowNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = 0;
        totalSize = 0;
        latestNewsBeans = Constant.realmLatestNewsBeens;
        newsThemeBean = Constant.realmNewsThemeBeen;
        newsContentModel = new NewsContentModel();
        baseFilePath = Constant.PATH_OFFLINE_PICTURE;
        mRealm = Realm.getDefaultInstance();
        final RealmResults<RealmNewsThemeBean> realmNewsThemeBeans = OfflineDownloadService.this.mRealm.where(RealmNewsThemeBean.class).findAll();
        final RealmResults<RealmOthersBean> realmOthersBeans = OfflineDownloadService.this.mRealm.where(RealmOthersBean.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //把之前保存的日志主题删除
                realmNewsThemeBeans.deleteAllFromRealm();
                realmOthersBeans.deleteAllFromRealm();
                //保存最新内容
                for (int i = 0; i < latestNewsBeans.size(); i++) {
                    realm.copyToRealmOrUpdate(latestNewsBeans.get(i));
                    totalSize = totalSize + latestNewsBeans.get(i).getStories().size();
                    if(latestNewsBeans.get(i).getTop_stories() != null){
                        totalSize = totalSize + latestNewsBeans.get(i).getTop_stories().size();
                    }
                }
                realm.copyToRealm(newsThemeBean);
            }
        });

        for (int m = 0; m < latestNewsBeans.size(); m++) {
            final int latestNews_position = m;
            final List<RealmStoriesBean> storiesBeens = latestNewsBeans.get(m).getStories();
            for (int i = 0; i < storiesBeens.size(); i++) {
                final int position = i;
                final List<String> imgUrls = new ArrayList<>();
                //获取新闻具体内容
                newsContentModel.getNewsContent(storiesBeens.get(i).getId() + "", new HttpListener<NewsContentBean>() {
                    @Override
                    public void onSuccess(NewsContentBean response) {
                        final RealmNewsContentBean realmNewsContentBean = new RealmNewsContentBean(response);
                        //提取新闻内容包含的图片地址(包括新闻列表的图片地址)
                        imgUrls.add(storiesBeens.get(position).getImage());
                        imgUrls.add(response.getImage());
                        imgUrls.addAll(HtmlUtil.getImgUrlList(response.getBody()));

                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                //替换成本地图片地址
                                RealmNewsContentBean result = realmNewsContentBean;
                                result.setImage(Constant.getDownloadFilePath(latestNewsBeans.get(latestNews_position).getDate(),
                                        Constant.STORIES_TYPE_COMMON, storiesBeens.get(position).getId(), HtmlUtil.getFileName(result.getImage())));
                                String body = result.getBody();
                                for(int i = 0; i < imgUrls.size(); i++){
                                    body = body.replaceAll(imgUrls.get(i), Constant.getDownloadFilePath(latestNewsBeans.get(latestNews_position).getDate(),
                                            Constant.STORIES_TYPE_COMMON, storiesBeens.get(position).getId(), HtmlUtil.getFileName(imgUrls.get(i))));
                                }
                                result.setBody(body);
                                realm.copyToRealmOrUpdate(result);
                            }
                        });
                        Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                //根据提取的地址保存图片到本地
                                for (int j = 0; j < imgUrls.size(); j++) {
                                    ImageManager.getInstance().downloadImage(OfflineDownloadService.this, imgUrls.get(j),
                                            baseFilePath + latestNewsBeans.get(latestNews_position).getDate() + "/" + Constant.STORIES_TYPE_COMMON + storiesBeens.get(position).getId() + "/", HtmlUtil.getFileName(imgUrls.get(j)));
                                }
                                subscriber.onNext("");
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<String>() {
                                    @Override
                                    public void onNext(String str) {
                                        //更新进度
                                        count++;
                                        doCount++;
                                        updateProgressBar(count * 100 / totalSize);
                                        if (count == totalSize) {
//                                        notification.
                                        }
                                        Intent intent = new Intent();
                                        intent.setAction("offline_download_progress");
                                        intent.putExtra("progress", count * 100 / totalSize);
                                        sendBroadcast(intent);
                                    }

                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        //离线下载失败
                                        downloadFailure();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        //离线下载失败
                        downloadFailure();
                    }
                });
            }

            if(latestNewsBeans.get(m).getTop_stories() != null){
                final List<RealmTopStoriesBean> topStoriesBeens = latestNewsBeans.get(m).getTop_stories();
                for (int i = 0; i < topStoriesBeens.size(); i++) {
                    final int position = i;
                    final List<String> imgUrls = new ArrayList<>();
                    newsContentModel.getNewsContent(topStoriesBeens.get(i).getId() + "", new HttpListener<NewsContentBean>() {
                        @Override
                        public void onSuccess(NewsContentBean response) {
                            final RealmNewsContentBean realmNewsContentBean = new RealmNewsContentBean(response);

                            imgUrls.add(topStoriesBeens.get(position).getImage());
                            imgUrls.add(response.getImage());
                            imgUrls.addAll(HtmlUtil.getImgUrlList(response.getBody()));
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    //替换成本地图片地址
                                    RealmNewsContentBean result = realmNewsContentBean;
                                    result.setImage(Constant.getDownloadFilePath(latestNewsBeans.get(latestNews_position).getDate(),
                                            Constant.STORIES_TYPE_TOP, topStoriesBeens.get(position).getId(), HtmlUtil.getFileName(result.getImage())));
                                    String body = result.getBody();
                                    for(int i = 0; i < imgUrls.size(); i++){
                                        body = body.replaceAll(imgUrls.get(i), Constant.getDownloadFilePath(latestNewsBeans.get(latestNews_position).getDate(),
                                                Constant.STORIES_TYPE_TOP, topStoriesBeens.get(position).getId(), HtmlUtil.getFileName(imgUrls.get(i))));
                                    }
                                    result.setBody(body);
                                    realm.copyToRealmOrUpdate(result);
                                }
                            });
                            Observable.create(new Observable.OnSubscribe<String>() {
                                @Override
                                public void call(Subscriber<? super String> subscriber) {
                                    for (int j = 0; j < imgUrls.size(); j++) {
                                        ImageManager.getInstance().downloadImage(OfflineDownloadService.this, imgUrls.get(j),
                                                baseFilePath + latestNewsBeans.get(latestNews_position).getDate() + "/" + Constant.STORIES_TYPE_TOP + topStoriesBeens.get(position).getId() + "/", HtmlUtil.getFileName(imgUrls.get(j)));
                                    }
                                    subscriber.onNext("");
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onNext(String str) {
                                            count++;
                                            doCount++;
                                            updateProgressBar(count * 100 / totalSize);
                                            Intent intent = new Intent();
                                            intent.setAction("offline_download_progress");
                                            intent.putExtra("progress", count * 100 / totalSize);
                                            sendBroadcast(intent);
                                        }

                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            downloadFailure();
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            downloadFailure();
                        }
                    });
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    public void onShowNotification() {

        rv.setProgressBar(R.id.progressbar, 100, 0, false);
        rv.setTextViewText(R.id.txt_title, getString(R.string.drawer_download));
        rv.setTextViewText(R.id.txt_progress, "0%");
        //创建通知详细信息
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.drawer_download))
                .setContentText(String.format(getResources().getString(R.string.notification_downloading), 0 + "%"))
                .setContent(rv);
        //创建点击跳转Intent
        Intent intent = new Intent(this, MainActivity.class);
        //创建任务栈Builder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置跳转Intent到通知中
        mBuilder.setContentIntent(pendingIntent);
        //获取通知服务
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //构建通知
        notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;//自动清除
        //显示通知
        nm.notify(0, notification);
        //启动为前台服务
        startForeground(0, notification);
    }

    public void updateProgressBar(int progress) {
        if(progress == 100){
            notification.contentView.setTextViewText(R.id.txt_title, getString(R.string.download_success));
        }
        notification.contentView.setProgressBar(R.id.progressbar, 100, progress, false);
        notification.contentView.setTextViewText(R.id.txt_progress, progress + "%");
        nm.notify(0, notification);
    }

    public void downloadFailure(){
        doCount++;
        if (doCount == totalSize) {
            notification.contentView.setTextViewText(R.id.txt_title, getString(R.string.download_failure));
            notification.contentView.setTextColor(R.id.txt_title, getResources().getColor(R.color.color_F94D54));
            nm.notify(0, notification);
            Intent intent = new Intent();
            intent.setAction("offline_download_progress");
            intent.putExtra("progress", -1);
            sendBroadcast(intent);
        }
    }
}
