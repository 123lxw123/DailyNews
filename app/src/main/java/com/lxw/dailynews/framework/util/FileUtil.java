package com.lxw.dailynews.framework.util;

import android.text.TextUtils;

import com.lxw.dailynews.framework.config.Constant;

import java.io.File;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public class FileUtil {
    public static boolean isFileExists(String path) {
        try {
            if (!TextUtils.isEmpty(Constant.PATH__SD_CARD)) {
                File file = new File(path);
                if (file.exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
