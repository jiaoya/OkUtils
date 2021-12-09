package com.albert.okutils;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/8/9.
 *      Desc         : 判断各种对象是否为空
 * </pre>
 */
public class NullUtil {

    private NullUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断 集合是否为空
     *
     * @param collection
     * @return true:非空
     */
    public static boolean notEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    /**
     * 判断Sring 对象是否为空,"null"被视为空，如果null不视为空请用：StringUtils.isEmpty(str)
     *
     * @param str
     * @return true:非空
     */
    public static boolean notEmpty(String str) {
        return str != null && !str.isEmpty() && !str.equals("null");
    }

    /**
     * 判断Sring 对象是否为空
     *
     * @param strs
     * @return true:非空
     */
    public static boolean notEmpty(String... strs) {
        for (String str : strs) {
            if (!notEmpty(str)) return false;
        }
        return true;
    }

    /**
     * 对象
     *
     * @param objects
     * @return true:非空
     */
    public static boolean notEmpty(Object... objects) {
        if (null == objects) {
            return false;
        }
        for (Object o : objects) {
            if (null == o) return false;
        }
        return true;
    }

    /**
     * 判断是否是正确的颜色
     *
     * @param color
     * @return
     */
    public static boolean isColor(String color) {
        if (color.matches("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|[0-9a-fA-F]{8})$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断 map是否为空
     *
     * @param map
     * @return true:非空
     */
    public static boolean notEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean notEmpty(Double data) {
        return null != data && data > 0;
    }

    public static boolean notEmpty(Integer data) {
        return null != data && data > 0;
    }

    public static boolean notEmpty(Float data) {
        return null != data && data > 0;
    }
}
