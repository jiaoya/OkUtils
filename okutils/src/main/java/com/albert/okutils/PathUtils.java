package com.albert.okutils;

import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/9/12.
 *      Desc         : 路径相关
 * </pre>
 */
public class PathUtils {

    private PathUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    //**********************以下是系统文件路径，需要权限！！！安卓10以后不推荐存储到应用的非专属目录*********************************

    /**
     * 获取根路径
     * Return the path of /system.
     *
     * @return the path of /system
     */
    public static String getRootPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取下载缓存路径
     * Return the path of /cache.
     *
     * @return the path of /cache
     */
    public static String getDownloadCachePath() {
        return Environment.getDownloadCacheDirectory().getAbsolutePath();
    }

    /**
     * 获取外存路径
     * Return the path of /storage/emulated/0.
     *
     * @return the path of /storage/emulated/0
     */
    public static String getExternalStoragePath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取外存音乐路径
     * Return the path of /storage/emulated/0/Music.
     *
     * @return the path of /storage/emulated/0/Music
     */
    public static String getExternalMusicPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取外存播客路径
     * Return the path of /storage/emulated/0/Podcasts.
     *
     * @return the path of /storage/emulated/0/Podcasts
     */
    public static String getExternalPodcastsPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
    }

    /**
     * 获取外存铃声路径
     * Return the path of /storage/emulated/0/Ringtones.
     *
     * @return the path of /storage/emulated/0/Ringtones
     */
    public static String getExternalRingtonesPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    }

    /**
     * 获取外存闹铃路径
     * Return the path of /storage/emulated/0/Alarms.
     *
     * @return the path of /storage/emulated/0/Alarms
     */
    public static String getExternalAlarmsPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    }

    /**
     * 获取外存通知路径
     * Return the path of /storage/emulated/0/Notifications.
     *
     * @return the path of /storage/emulated/0/Notifications
     */
    public static String getExternalNotificationsPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath();
    }

    /**
     * 获取外存图片路径
     * Return the path of /storage/emulated/0/Pictures.
     *
     * @return the path of /storage/emulated/0/Pictures
     */
    public static String getExternalPicturesPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获取外存影片路径
     * Return the path of /storage/emulated/0/Movies.
     *
     * @return the path of /storage/emulated/0/Movies
     */
    public static String getExternalMoviesPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    /**
     * 获取外存下载路径
     * Return the path of /storage/emulated/0/Download.
     *
     * @return the path of /storage/emulated/0/Download
     */
    public static String getExternalDownloadsPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取外存数码相机图片路径
     * Return the path of /storage/emulated/0/DCIM.
     *
     * @return the path of /storage/emulated/0/DCIM
     */
    public static String getExternalDcimPath() {
        if (isExternalStorageDisable()) return "";
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 获取外存文档路径
     * Return the path of /storage/emulated/0/Documents.
     *
     * @return the path of /storage/emulated/0/Documents
     */
    public static String getExternalDocumentsPath() {
        if (isExternalStorageDisable()) return "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }


    public static String getShortAppCachePath() {
        if (isExternalStorageDisable()) return "";
        //noinspection ConstantConditions
        return "/Android/data/com.xier.bckid/cache";
    }

    public static String getDownloadPath() {
        if (isExternalStorageDisable()) return "";
        //noinspection ConstantConditions
        return "Download";
    }

    /**
     * 获取外存应用 OBB 路径
     * Return the path of /storage/emulated/0/Android/obb/package.
     *
     * @return the path of /storage/emulated/0/Android/obb/package
     */
    public static String getExternalAppObbPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getObbDir().getAbsolutePath();
    }

    private static boolean isExternalStorageDisable() {
        return !SDCardUtils.isSDCardEnableByEnvironment();
    }


    //**********************以下是应用专属文件相关路径，不需要权限*********************************
    // 1、从内部存储空间访问，可以使用 getFilesDir() 或 getCacheDir() 方法。这两个目录适合存储隐私数据，其他应用不能访问

    // 2、从外部存储空间访问，可以使用 getExternalFilesDir() 或 getExternalCacheDir() 方法 这两个目录是应用外部存储的专属，其他应用需要权限才能访问

    /**
     * 获取数据路径
     * Return the path of /data.
     *
     * @return the path of /data
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 获取应用内 数据路径
     * Return the path of /data/data/package.
     *
     * @return the path of /data/data/package
     */
    public static String getInternalAppDataPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Utils.getApp().getApplicationInfo().dataDir;
        }
        return Utils.getApp().getDataDir().getAbsolutePath();
    }

    /**
     * 获取应用内 文件路径
     * Return the path of /data/data/package/files.
     *
     * @return the path of /data/data/package/files
     */
    public static String getInternalAppFilesPath() {
        return Utils.getApp().getFilesDir().getAbsolutePath();
    }

    /**
     * 获取应用内 缓存路径
     * Return the path of /data/data/package/cache.
     *
     * @return the path of /data/data/package/cache
     */
    public static String getInternalAppCachePath() {
        return Utils.getApp().getCacheDir().getAbsolutePath();
    }

    /**
     * 获取应用内 代码缓存路径
     * Return the path of /data/data/package/code_cache.
     *
     * @return the path of /data/data/package/code_cache
     */
    public static String getInternalAppCodeCacheDir() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return Utils.getApp().getApplicationInfo().dataDir + "/code_cache";
        }
        return Utils.getApp().getCodeCacheDir().getAbsolutePath();
    }

    /**
     * 获取应用内 数据库路径
     * Return the path of /data/data/package/databases.
     *
     * @return the path of /data/data/package/databases
     */
    public static String getInternalAppDbsPath() {
        return Utils.getApp().getApplicationInfo().dataDir + "/databases";
    }

    /**
     * 获取应用内 数据库路径
     * Return the path of /data/data/package/databases/name.
     *
     * @param name The name of database.
     * @return the path of /data/data/package/databases/name
     */
    public static String getInternalAppDbPath(String name) {
        return Utils.getApp().getDatabasePath(name).getAbsolutePath();
    }

    /**
     * 获取应用内 SP 路径
     * Return the path of /data/data/package/shared_prefs.
     *
     * @return the path of /data/data/package/shared_prefs
     */
    public static String getInternalAppSpPath() {
        return Utils.getApp().getApplicationInfo().dataDir + "shared_prefs";
    }

    /**
     * 获取应用内 未备份文件路径
     * Return the path of /data/data/package/no_backup.
     *
     * @return the path of /data/data/package/no_backup
     */
    public static String getInternalAppNoBackupFilesPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return Utils.getApp().getApplicationInfo().dataDir + "no_backup";
        }
        return Utils.getApp().getNoBackupFilesDir().getAbsolutePath();
    }

    /*-------以下是应用专属的外存-------*/

    /**
     * 获取应用专属的外存数据 根 路径
     * Return the path of /storage/emulated/0/Android/data/package.
     *
     * @return the path of /storage/emulated/0/Android/data/package
     */
    public static String getExternalAppDataPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalCacheDir().getParentFile().getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 缓存的根 路径
     * Return the path of /storage/emulated/0/Android/data/package/cache.
     *
     * @return the path of /storage/emulated/0/Android/data/package/cache
     */
    public static String getExternalAppCachePath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 文件的根路径
     * Return the path of /storage/emulated/0/Android/data/package/files.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files
     */
    public static String getExternalAppFilesPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(null).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存
     *
     * @return the path of /storage/emulated/0/Android/data/package/files
     */
    public static File getExternalAppFile() {
        if (isExternalStorageDisable()) return null;
        return Utils.getApp().getExternalFilesDir(null);
    }

    /**
     * 获取应用专属的外存 文档路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Documents.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Documents
     */
    public static String getExternalAppDocumentsPath() {
        if (isExternalStorageDisable()) return "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //noinspection ConstantConditions
            return Utils.getApp().getExternalFilesDir(null).getAbsolutePath() + "/Documents";
        }
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 音乐路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Music.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Music
     */
    public static String getExternalAppMusicPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 播客路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Podcasts.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Podcasts
     */
    public static String getExternalAppPodcastsPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 铃声路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Ringtones.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Ringtones
     */
    public static String getExternalAppRingtonesPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 闹铃路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Alarms.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Alarms
     */
    public static String getExternalAppAlarmsPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 通知路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Notifications.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Notifications
     */
    public static String getExternalAppNotificationsPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 图片路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Pictures.
     *
     * @return path of /storage/emulated/0/Android/data/package/files/Pictures
     */
    public static String getExternalAppPicturesPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Movies.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Movies
     */
    public static String getExternalAppMoviesPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 下载路径
     * Return the path of /storage/emulated/0/Android/data/package/files/Download.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Download
     */
    public static String getExternalAppDownloadPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取应用专属的外存 数码照片存储
     * Return the path of /storage/emulated/0/Android/data/package/files/DCIM.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/DCIM
     */
    public static String getExternalAppDcimPath() {
        if (isExternalStorageDisable()) return "";
        return Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }
}
