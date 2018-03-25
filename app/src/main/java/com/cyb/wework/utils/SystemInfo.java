package com.cyb.wework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 设备信息，偏软件
 * Created by cyb on 2016/12/15 0015.
 */

public final class SystemInfo {

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 安卓系统版本，21,22,23
     * @return
     */
    public static int getSDKVersion(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 安卓系统版本代号,M,N,O
     * @return
     */
    public static String getSysVersionName(){
        return Build.VERSION.RELEASE;
    }

    public static String getAppName(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.applicationInfo.loadLabel(packageManager).toString();
        }catch (Exception e){
            return "";
        }
    }

    public static String getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }catch (Exception e){
            return "";
        }
    }

    public static String getAppPackageName(Context context){
        return context.getPackageName();
    }

    public static String getConnectType(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().getTypeName();
    }

    public static boolean isRooted(){
        // nexus 5x "/su/bin/"
        String[] paths = { "/system/xbin/", "/system/bin/", "/system/sbin/", "/sbin/", "/vendor/bin/", "/su/bin/" };
        try{
            for(int i = 0; i < paths.length; i++){
                String path = paths[i] + "su";
                if(new File(path).exists()){
                    String execResult = exec(new String[] { "ls", "-l", path });
                    return !(TextUtils.isEmpty(execResult) || execResult.indexOf("root") == execResult.lastIndexOf("root"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private static String exec(String[] exec){
        String ret = "";
        ProcessBuilder processBuilder = new ProcessBuilder(exec);
        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while( (line = bufferedReader.readLine()) != null){
                ret += line;
            }
            process.getInputStream().close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
