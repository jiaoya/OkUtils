package com.albert.okutils;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/11/6.
 *      Desc         : 倒计时
 * </pre>
 */
public class CountDownUtils {

    /**
     * 发送消息标识
     */
    public static final int CTSES_MSG_TYPE = 60;
    public static final String DAY_TYPE = "day";
    public static final String HOUR_TYPE = "hour";
    public static final String MIUNTE_TYPE = "miunte";
    public static final String SECOND_TYPE = "second";

    private CountDownUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 倒计时 CountDownTimer方式
     *
     * @param millisInFuture    总时间 ms
     * @param countDownInterval 间隔时间 ms
     * @param countDownCallback 回调返回秒
     */
    public static CountDownTimer cDownTimer(long millisInFuture, long countDownInterval, final ICountDownCallback countDownCallback) {

        CountDownTimer countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            long day = 0;
            long hour = 0;
            long minute = 0;
            long second = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                initData(millisUntilFinished / 1000);
                countDownCallback.onStart(millisUntilFinished, day, hour, minute, second);
            }

            @Override
            public void onFinish() {
                cancel();
                countDownCallback.onFinish();
            }

            void initData(long totalSeconds) {
                if (totalSeconds > 0) {
                    second = totalSeconds;
                    if (second >= 60) {
                        minute = second / 60;
                        second = second % 60;
                        if (minute >= 60) {
                            hour = minute / 60;
                            minute = minute % 60;
                            if (hour > 24) {
                                day = hour / 24;
                                hour = hour % 24;
                            } else {
                                day = 0;
                            }
                        } else {
                            hour = 0;
                        }
                    } else {
                        minute = 0;
                    }
                } else {
                    second = 0;
                }
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }


    /**
     * 倒计时 scheduledExecutorService 方式
     *
     * @param millisInFuture    总时间 ms
     * @param countDownInterval 间隔时间 ms
     * @param mHandler          回调返回秒
     */
    public static ScheduledExecutorService cDownSes(final long millisInFuture, final long countDownInterval, final Handler mHandler) {

        final ScheduledExecutorService scheduled = new ScheduledThreadPoolExecutor(1);
        scheduled.scheduleWithFixedDelay(new Runnable() {
            long total = millisInFuture / 1000;
            long day = 0;
            long hour = 0;
            long minute = 0;
            long second = 0;

            @Override
            public void run() {
                if (total > 0) {
                    second = total;
                    if (second >= 60) {
                        minute = second / 60;
                        second = second % 60;
                        if (minute >= 60) {
                            hour = minute / 60;
                            minute = minute % 60;
                            if (hour > 24) {
                                day = hour / 24;
                                hour = hour % 24;
                            } else {
                                day = 0;
                            }
                        } else {
                            hour = 0;
                        }
                    } else {
                        minute = 0;
                    }
                } else {
                    second = 0;
                }

                Message msg = mHandler.obtainMessage(CTSES_MSG_TYPE);
                Bundle bundle = new Bundle();
                bundle.putLong(DAY_TYPE, day);
                bundle.putLong(HOUR_TYPE, hour);
                bundle.putLong(MIUNTE_TYPE, minute);
                bundle.putLong(SECOND_TYPE, second);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                total--;
                if (day == 0 && hour == 0 && minute == 0 && second == 0) {
                    scheduled.shutdown();
                }
            }
        }, 0, countDownInterval / 1000, TimeUnit.SECONDS);
        return scheduled;
    }

    public interface ICountDownCallback {
        void onStart(long currentTotleTime, long day, long hour, long minute, long second);

        void onFinish();
    }


}
