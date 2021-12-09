package com.albert.okutils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.albert.okutils.Utils.getApp;


/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/9/12.
 *      Desc         : 资源相关
 * </pre>
 */
public class ResourceUtils {

    private static final int BUFFER_SIZE = 8192;

    private ResourceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * assets 中拷贝文件
     * Copy the file from assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @param destFilePath   The path of destination file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean copyFileFromAssets(final String assetsFilePath, final String destFilePath) {
        boolean res = true;
        try {
            String[] assets = getApp().getAssets().list(assetsFilePath);
            if (assets.length > 0) {
                for (String asset : assets) {
                    res &= copyFileFromAssets(assetsFilePath + "/" + asset, destFilePath + "/" + asset);
                }
            } else {
                res = writeFileFromIS(
                        destFilePath,
                        getApp().getAssets().open(assetsFilePath),
                        false
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            res = false;
        }
        return res;
    }

    /**
     * assets 中读取字符串
     * Return the content of assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @return the content of assets
     */
    public static String readAssets2String(final String assetsFilePath) {
        return readAssets2String(assetsFilePath, null);
    }

    /**
     * assets 中读取字符串
     * Return the content of assets.
     *
     * @param assetsFilePath The path of file in assets.
     * @param charsetName    The name of charset.
     * @return the content of assets
     */
    public static String readAssets2String(final String assetsFilePath, final String charsetName) {
        InputStream is;
        try {
            is = getApp().getAssets().open(assetsFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] bytes = is2Bytes(is);
        if (bytes == null) return null;
        if (isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * assets 中按行读取字符串
     * Return the content of file in assets.
     *
     * @param assetsPath The path of file in assets.
     * @return the content of file in assets
     */
    public static List<String> readAssets2List(final String assetsPath) {
        return readAssets2List(assetsPath, null);
    }

    /**
     * assets 中按行读取字符串
     * Return the content of file in assets.
     *
     * @param assetsPath  The path of file in assets.
     * @param charsetName The name of charset.
     * @return the content of file in assets
     */
    public static List<String> readAssets2List(final String assetsPath,
                                               final String charsetName) {
        try {
            return is2List(getApp().getResources().getAssets().open(assetsPath), charsetName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * raw 中拷贝文件
     * Copy the file from raw.
     *
     * @param resId        The resource id.
     * @param destFilePath The path of destination file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean copyFileFromRaw(@RawRes final int resId, final String destFilePath) {
        return writeFileFromIS(
                destFilePath,
                getApp().getResources().openRawResource(resId),
                false
        );
    }

    /**
     * raw 中读取字符串
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @return the content of resource in raw
     */
    public static String readRaw2String(@RawRes final int resId) {
        return readRaw2String(resId, null);
    }

    /**
     * raw 中读取字符串
     * Return the content of resource in raw.
     *
     * @param resId       The resource id.
     * @param charsetName The name of charset.
     * @return the content of resource in raw
     */
    public static String readRaw2String(@RawRes final int resId, final String charsetName) {
        InputStream is = getApp().getResources().openRawResource(resId);
        byte[] bytes = is2Bytes(is);
        if (bytes == null) return null;
        if (isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * raw 中按行读取字符串
     * Return the content of resource in raw.
     *
     * @param resId The resource id.
     * @return the content of file in assets
     */
    public static List<String> readRaw2List(@RawRes final int resId) {
        return readRaw2List(resId, null);
    }

    /**
     * raw 中按行读取字符串
     * Return the content of resource in raw.
     *
     * @param resId       The resource id.
     * @param charsetName The name of charset.
     * @return the content of file in assets
     */
    public static List<String> readRaw2List(@RawRes final int resId,
                                            final String charsetName) {
        return is2List(getApp().getResources().openRawResource(resId), charsetName);
    }

    /**
     * 获取颜色资源
     *
     * @param id
     * @return
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getApp(), id);
    }

    /**
     * 获取颜色资源
     *
     * @param id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getString(getApp(), id);
    }

    /**
     * 获取颜色资源
     *
     * @param context
     * @param id
     * @return
     */
    public static String getString(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }

    /**
     * 获取drawable
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getApp(), id);
    }

    /**
     * 获取dimen资源
     *
     * @param context
     * @param id
     * @return
     */
    public static int getDimension(Context context, @DimenRes int id) {
        return (int) context.getResources().getDimension(id);
    }

    public static float getDimensionFloat(Context context, @DimenRes int id) {
        return context.getResources().getDimension(id);
    }

    /**
     * 获取dimen资源
     *
     * @param id
     * @return
     */
    public static int getDimension(@DimenRes int id) {
        return (int) getDimension(getApp(), id);
    }

    public static float getDimensionFloat(@DimenRes int id) {
        return getDimension(getApp(), id);
    }

    /**
     * 地址转换
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getApp().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static boolean writeFileFromIS(final String filePath,
                                           final InputStream is,
                                           final boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }

    private static boolean writeFileFromIS(final File file,
                                           final InputStream is,
                                           final boolean append) {
        if (!createOrExistsFile(file) || is == null) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(data, 0, BUFFER_SIZE)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static byte[] is2Bytes(final InputStream is) {
        if (is == null) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] b = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(b, 0, BUFFER_SIZE)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> is2List(final InputStream is,
                                        final String charsetName) {
        BufferedReader reader = null;
        try {
            List<String> list = new ArrayList<>();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(is));
            } else {
                reader = new BufferedReader(new InputStreamReader(is, charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
