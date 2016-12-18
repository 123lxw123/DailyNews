package com.lxw.dailynews.app.bean;

/**
 * Created by Zion on 2016/12/18.
 */

public class FileInfoBean {
    public String name;
    public String path;
    public long lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
