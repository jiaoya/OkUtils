package com.albert.okutils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import static com.albert.okutils.Utils.getApp;


/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/9/10.
 *      Desc         : uri工具类
 * </pre>
 */
public class UriUtils {

    private UriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Resource to uri.
     * <p>res2Uri([res type]/[res name]) -> res2Uri(drawable/icon), res2Uri(raw/icon)</p>
     * <p>res2Uri([resource_id]) -> res2Uri(R.drawable.icon)</p>
     *
     * @param resPath The path of res.
     * @return uri
     */
    public static Uri res2Uri(String resPath) {
        return Uri.parse("android.resource://" + Utils.getApp().getPackageName() + "/" + resPath);
    }

    /**
     * 判断是否为有效链接地址
     *
     * @param url
     * @return
     */
    public static boolean isHttpUrl(String url) {
        if (!NullUtil.notEmpty(url)) {
            return false;
        }
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            return true;
        }
        return false;
    }

    /**
     * 解码并获取参数值
     *
     * @param uri
     * @param key
     * @return
     */
    public static String getBase64EncodedParameter(@NonNull Uri uri, String key) {
        String queryParameter = uri.getQueryParameter(key);
        if (null == queryParameter) {
            return null;
        }
        return decodeString(queryParameter);
    }

    /**
     * 获取参数值并编码
     *
     * @param uri
     * @param key
     * @return
     */
    public static String getUriEncodedParameter(@NonNull Uri uri, String key) {
        String queryParameter = uri.getQueryParameter(key);
        if (null == queryParameter) {
            return null;
        }
        return URLDecoder.decode(queryParameter);
    }

    public static String getUriParameter(@NonNull Uri uri, String key) {
        String queryParameter = uri.getQueryParameter(key);
        if (null == queryParameter) {
            return null;
        }
        return queryParameter;
    }

    public static String getQueryParameter(Uri uri, String key) {
        String queryParameter = uri.getQueryParameter(key);
        return queryParameter;
    }

    /**
     * URL base64解码
     *
     * @param str
     * @return
     */
    public static String decodeString(@NonNull String str) {
        try {
            str = str.replace(' ', '-');//Base64.URL_SAFE
            str = str.replace('+', '-');//Base64.URL_SAFE
            str = str.replace('/', '_');//Base64.URL_SAFE
            str = str.replace("=", "");//Base64.NO_PADDING
            str = str.replace("\n", "");//Base64.NO_WRAP
            String result = new String(EncodeUtils.base64Decode(str.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * URL base64编码
     *
     * @param str
     * @return
     */
    public static String encodeString(@NonNull String str) {
        try {
            String result = EncodeUtils.base64Encode2String(str.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * www.baidu.com
     *
     * @return getScheme(www.baidu.com)=null
     */
    public static String getScheme(String url) {
        int endIndex = url.indexOf("://");
        if (endIndex == -1) {
            endIndex = url.indexOf(':');
            if (endIndex == -1)
                endIndex = 0;
        }
        return url.substring(0, endIndex);
    }

    /**
     * 包含端口：https://www.baidu.com:8888/search?key=English 返回 www.baidu.com:8888
     *
     * @param url
     * @return host+:+port
     */
    public static String getAuthority(String url) {
        int hostStart = url.indexOf("://") + 3;
        if (hostStart == 2) {// -1 + 3 = 2
            hostStart = 0;
        }
        int hostEnd = url.indexOf('/', hostStart);
        if (hostEnd == -1) hostEnd = url.indexOf('?', hostStart);
        if (hostEnd == -1) {
            return url.substring(hostStart);
        } else {
            if (hostEnd == hostStart) {
                return "";
            } else {
                return url.substring(hostStart, hostEnd);
            }
        }
    }

    /**
     * 获取uri的完整地址  ：https://www.baidu.com:8888/search?key=English 返回 ：https://www.baidu.com:8888/search
     *
     * @param uri
     * @return
     */
    public static String getUriFullPath(Uri uri) {
        if (NullUtil.notEmpty(uri.getPort())) {
            return uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
        } else {
            return uri.getScheme() + "://" + uri.getHost() + uri.getPath();
        }
    }

    public static String getUriFullPath(String uriData) {
        if (!NullUtil.notEmpty(uriData)) {
            return null;
        }
        return getUriFullPath(Uri.parse(uriData));
    }

    /**
     * 获取格式
     *
     * @param path
     * @return
     */
    public static String getPathFormat(String path) {
        if (!NullUtil.notEmpty(path)) {
            return "";
        }
        int lastSep = path.lastIndexOf(".");
        if (lastSep != -1) {
            return path.substring(lastSep + 1);
        } else {
            return "";
        }
    }

    /**
     * url添加参数，未编码
     *
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String appendParam(String url, String key, Object value) {
        if (null == value) return url;
        else if (!NullUtil.notEmpty(value.toString())) return url;
        else {
            StringBuilder stringBuilder = new StringBuilder(url);
            char appendChar = url.contains("?") ? '&' : '?';
            return stringBuilder.append(appendChar).append(key).append('=').append(value).toString();
        }
    }

    /**
     * url参数构建器
     */
    public static class ParamsBuilder {

        StringBuilder paramBuilder;

        public ParamsBuilder(String url) {
            if (NullUtil.notEmpty(url)) {
                paramBuilder = new StringBuilder(url);
            }
        }

        public ParamsBuilder append(String key, Object value) {
            if (paramBuilder != null) {
                char appendChar = paramBuilder.toString().contains("?") ? '&' : '?';
                paramBuilder.append(appendChar).append(key).append('=').append(value);
            }
            return this;
        }

        public ParamsBuilder append(String key, String value) {
            if (paramBuilder != null) {
                char appendChar = paramBuilder.toString().contains("?") ? '&' : '?';
                paramBuilder.append(appendChar).append(key).append('=').append(value);
            }
            return this;
        }

        public String build() {
            if (paramBuilder == null) {
                return "";
            }
            return paramBuilder.toString();
        }
    }

    /**
     * File to uri.
     *
     * @param file The file.
     * @return uri
     */
    public static Uri file2Uri(final File file) {
        if (!UtilsBridge.isFileExists(file)) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
            return FileProvider.getUriForFile(Utils.getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    public static File uri2File(final Uri uri) {
        if (uri == null) return null;
        File file = uri2FileReal(uri);
        if (file != null) return file;
        return copyUri2Cache(uri);
    }

    /**
     * Uri to file.
     *
     * @param uri        The uri.
     * @param columnName The name of the target column.
     *                   <p>e.g. {@link MediaStore.Images.Media#DATA}</p>
     * @return file
     */
    public static File uri2File(@NonNull final Uri uri, final String columnName) {
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        }
        CursorLoader cl = new CursorLoader(getApp());
        cl.setUri(uri);
        cl.setProjection(new String[]{columnName});
        Cursor cursor = null;
        try {
            cursor = cl.loadInBackground();
            int columnIndex = cursor.getColumnIndexOrThrow(columnName);
            cursor.moveToFirst();
            return new File(cursor.getString(columnIndex));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private static File uri2FileReal(final Uri uri) {
        Log.d("UriUtils", uri.toString());
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null) {
            String[] externals = new String[]{"/external/", "/external_path/"};
            File file = null;
            for (String external : externals) {
                if (path.startsWith(external)) {
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + path.replace(external, "/"));
                    if (file.exists()) {
                        Log.d("UriUtils", uri.toString() + " -> " + external);
                        return file;
                    }
                }
            }
            file = null;
            if (path.startsWith("/files_path/")) {
                file = new File(Utils.getApp().getFilesDir().getAbsolutePath()
                        + path.replace("/files_path/", "/"));
            } else if (path.startsWith("/cache_path/")) {
                file = new File(Utils.getApp().getCacheDir().getAbsolutePath()
                        + path.replace("/cache_path/", "/"));
            } else if (path.startsWith("/external_files_path/")) {
                file = new File(Utils.getApp().getExternalFilesDir(null).getAbsolutePath()
                        + path.replace("/external_files_path/", "/"));
            } else if (path.startsWith("/external_cache_path/")) {
                file = new File(Utils.getApp().getExternalCacheDir().getAbsolutePath()
                        + path.replace("/external_cache_path/", "/"));
            }
            if (file != null && file.exists()) {
                Log.d("UriUtils", uri.toString() + " -> " + path);
                return file;
            }
        }
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            if (path != null) return new File(path);
            Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
            return null;
        }// end 0
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(Utils.getApp(), uri)) {
            if ("com.android.externalstorage.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    StorageManager mStorageManager = (StorageManager) Utils.getApp().getSystemService(Context.STORAGE_SERVICE);
                    try {
                        Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                        Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                        Method getUuid = storageVolumeClazz.getMethod("getUuid");
                        Method getState = storageVolumeClazz.getMethod("getState");
                        Method getPath = storageVolumeClazz.getMethod("getPath");
                        Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
                        Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

                        Object result = getVolumeList.invoke(mStorageManager);

                        final int length = Array.getLength(result);
                        for (int i = 0; i < length; i++) {
                            Object storageVolumeElement = Array.get(result, i);
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);

                            final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue;

                            //Primary storage is already handled.
                            if ((Boolean) isPrimary.invoke(storageVolumeElement)
                                    && (Boolean) isEmulated.invoke(storageVolumeElement)) {
                                continue;
                            }

                            String uuid = (String) getUuid.invoke(storageVolumeElement);

                            if (uuid != null && uuid.equals(type)) {
                                return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                            }
                        }
                    } catch (Exception ex) {
                        Log.d("UriUtils", uri.toString() + " parse failed. " + ex.toString() + " -> 1_0");
                    }
                }
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                return null;
            }// end 1_0
            else if ("com.android.providers.downloads.documents".equals(authority)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (TextUtils.isEmpty(id)) {
                    Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return new File(id.substring(4));
                } else if (id.startsWith("msf:")) {
                    id = id.split(":")[1];
                }

                long availableId = 0;
                try {
                    availableId = Long.parseLong(id);
                } catch (Exception e) {
                    return null;
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/all_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);
                    try {
                        File file = getFileFromUri(contentUri, "1_1");
                        if (file != null) {
                            return file;
                        }
                    } catch (Exception ignore) {
                    }
                }

                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
                return null;
            }// end 1_1
            else if ("com.android.providers.media.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                    return null;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getFileFromUri(contentUri, selection, selectionArgs, "1_2");
            }// end 1_2
            else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                return getFileFromUri(uri, "1_3");
            }// end 1_3
            else {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
                return null;
            }// end 1_4
        }// end 1
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            return getFileFromUri(uri, "2");
        }// end 2
        else {
            Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
            return null;
        }// end 3
    }

    private static File getFileFromUri(final Uri uri, final String code) {
        return getFileFromUri(uri, null, null, code);
    }

    private static File getFileFromUri(final Uri uri,
                                       final String selection,
                                       final String[] selectionArgs,
                                       final String code) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
            if (!TextUtils.isEmpty(uri.getLastPathSegment())) {
                return new File(uri.getLastPathSegment());
            }
        } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                File fileDir = Environment.getExternalStorageDirectory();
                return new File(fileDir, path.substring("/QQBrowser".length(), path.length()));
            }
        } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                return new File(path.replace("/root", ""));
            }
        }

        final Cursor cursor = Utils.getApp().getContentResolver().query(
                uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor == null) {
            Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    return null;
                }
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            }
        } catch (Exception e) {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally {
            cursor.close();
        }
    }


    private static File copyUri2Cache(Uri uri) {
        Log.d("UriUtils", "copyUri2Cache() called");
        InputStream is = null;
        try {
            is = Utils.getApp().getContentResolver().openInputStream(uri);
            File file = new File(Utils.getApp().getCacheDir(), "" + System.currentTimeMillis());
            UtilsBridge.writeFileFromIS(file.getAbsolutePath(), is);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * uri 转 bytes
     *
     * @param uri The uri.
     * @return the input stream
     */
    public static byte[] uri2Bytes(Uri uri) {
        if (uri == null) return null;
        InputStream is = null;
        try {
            is = Utils.getApp().getContentResolver().openInputStream(uri);
            return UtilsBridge.inputStream2Bytes(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("UriUtils", "uri to bytes failed.");
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
