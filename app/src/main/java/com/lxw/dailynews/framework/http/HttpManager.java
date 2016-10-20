package com.lxw.dailynews.framework.http;

import android.app.AlertDialog;
import android.content.Context;

import com.lxw.dailynews.framework.common.application.BaseApplication;

import dmax.dialog.SpotsDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public abstract class HttpManager<T> {
    private Observable<T> observable;
    private Subscriber<T> subscriber;
    private AlertDialog dialog;
    public HttpManager(){
        this(null);
    }
    public HttpManager(Context context){
        if(context != null ){
            dialog = new SpotsDialog(context);
        }
        createObservable();
        subscriber = new Subscriber<T>(){

            @Override
            public void onCompleted() {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
                onFailure();
            }

            @Override
            public void onNext(T t) {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
                onSuccess();
            }
        };
        doSubscribe(observable, subscriber);
    }
    public abstract void createObservable();
    public abstract void onSuccess();
    public abstract void onFailure();

    public void doSubscribe(Observable observable, Subscriber subscriber){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0(){

                    @Override
                    public void call() {
                        if(dialog != null && !dialog.isShowing()){
                            dialog.show();
                        }
                    }
                })
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
