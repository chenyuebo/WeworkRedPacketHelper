package com.cyb.wework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by cyb on 2017/6/26.
 */
public class StatusBarUtil {

    public static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        try{
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(id);
        }catch(Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 设置状态栏透明与字体颜色
    public static void setStatusBarTranslucent(Activity activity, boolean isLightStatusBar) {
        Window window = activity.getWindow();
        setStatusBarTranslucent(window, isLightStatusBar);
    }

    // 设置状态栏透明与字体颜色
    public static void setStatusBarTranslucent(Window window, boolean isLightStatusBar) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT && VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && VERSION.SDK_INT < VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);

        } else if (VERSION.SDK_INT >= VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }

        if (isXiaomi()) {
            setXiaomiStatusBar(window, isLightStatusBar);
        } else if (isMeizu()) {
            setMeizuStatusBar(window, isLightStatusBar);
        }
    }


    // 设置状态栏透明与字体颜色
    public static void setStatusBarTranslucentWithFullScreen(Activity activity, boolean isLightStatusBar) {
        Window window = activity.getWindow();
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT && VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && VERSION.SDK_INT < VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (VERSION.SDK_INT >= VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        if (isXiaomi()) {
            setXiaomiStatusBar(window, isLightStatusBar);
        } else if (isMeizu()) {
            setMeizuStatusBar(window, isLightStatusBar);
        }
    }

    // 是否是小米手机
    private static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    // 设置小米状态栏
    private static void setXiaomiStatusBar(Window window, boolean isLightStatusBar) {
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isLightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 是否是魅族手机
    private static boolean isMeizu() {
        try {
            Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    // 设置魅族状态栏
    private static void setMeizuStatusBar(Window window, boolean isLightStatusBar) {
        WindowManager.LayoutParams params = window.getAttributes();
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(params);
            if (isLightStatusBar) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(params, value);
            window.setAttributes(params);
            darkFlag.setAccessible(false);
            meizuFlags.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
