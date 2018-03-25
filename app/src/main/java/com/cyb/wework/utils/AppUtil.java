package com.cyb.wework.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by cyb on 2018/2/2.
 */

public class AppUtil {

    /**
     * 获取企业微信版本
     * @param context
     * @return
     */
    public static String getWeworkVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.tencent.wework", 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
