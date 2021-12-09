package com.albert.okutils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/8/21.
 *      Desc         : 时间常量
 * </pre>
 */
public final class TimeConstants {

    public static final int MSEC = 1;
    public static final int SEC = 1000;
    public static final int MIN = 60000;
    public static final int HOUR = 3600000;
    public static final int DAY = 86400000;

    @IntDef({MSEC, SEC, MIN, HOUR, DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
