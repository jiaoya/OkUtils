package com.albert.okutils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2020.
 *      Author       : jiaoya.
 *      Created Time : 2020/3/3.
 *      Desc         : 角标处理
 * </pre>
 */
public class BadgeUtils {

    private static int notificationId = 0;

    /**
     * 设置角标数量
     *
     * @param count
     * @param context
     * @return
     */
    public static boolean setCount(final int count, final Context context) {
        return setCount(count, context, null);
    }

    /**
     * 隐藏角标
     */
    public static void hiddenBadge(final Context context) {
        setCount(0, context, null);
    }

    public static boolean setCount(final int count, final Context context, Notification notification) {
        if (count >= 0 && context != null) {
            switch (Build.BRAND.toLowerCase()) {
                case "xiaomi":
                    if (notification != null) {
                        setXiaomiBadge(count, notification);
                    }
                    return true;
                case "huawei":
                case "honor":
                    return setHuaweiBadge(count, context);
                case "samsung":
                    return setSamsungBadge(count, context);
                case "lenovo":
                    return setZukBadge(count, context);
                default:
                    return true;
            }
        } else {
            return false;
        }
    }

    public static boolean setNotificationBadge(int count, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0之后添加角标需要NotificationChannel
            NotificationChannel channel = new NotificationChannel("badge", "badge",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(context, "badge")
                .setContentTitle("应用角标")
                .setContentText("您有" + count + "条未读消息")
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_down))
                //.setSmallIcon(R.drawable.ic_arrow_down)
                .setAutoCancel(true)
                .setChannelId("badge")
                .setPriority(NotificationCompat.VISIBILITY_PRIVATE)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL).build();
        // 小米
        setXiaomiBadge(1, notification);
        notificationManager.notify(notificationId++, notification);
        return true;
    }


    /**
     * 设置小米手机桌面的角标
     *
     * @param count
     * @param notification
     */
    public static void setXiaomiBadge(int count, Notification notification) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置华为手机桌面的角标
     *
     * @param count
     * @param context
     * @return
     */
    private static boolean setHuaweiBadge(int count, Context context) {
        try {
            String launchClassName = getLauncherClassName(context);
            if (!NullUtil.notEmpty(launchClassName)) {
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", count);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean setSamsungBadge(int count, Context context) {
        try {
            String launcherClassName = getLauncherClassName(context);
            if (!NullUtil.notEmpty(launcherClassName)) {
                return false;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean setZukBadge(int count, Context context) {
        try {
            Bundle extra = new Bundle();
            ArrayList<String> ids = new ArrayList<>();
            // 以列表形式传递快捷方式id，可以添加多个快捷方式id
//        ids.add("custom_id_1");
//        ids.add("custom_id_2");
            extra.putStringArrayList("app_shortcut_custom_id", ids);
            extra.putInt("app_badge_count", count);
            Uri contentUri = Uri.parse("content://com.android.badge/badge");
            Bundle bundle = context.getContentResolver().call(contentUri, "setAppBadgeCount", null,
                    extra);
            return bundle != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    private static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }
}
