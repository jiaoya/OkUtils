package com.albert.okutils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/8/21.
 *      Desc         : 内存常量
 * </pre>
 */
public final class MemoryConstants {
    public static final int BYTE = 1;
    public static final int KB = 1024;
    public static final int MB = 1048576;
    public static final int GB = 1073741824;

    @IntDef({BYTE, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
