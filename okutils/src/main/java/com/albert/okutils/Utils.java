package com.albert.okutils;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * <pre>
 *      @copyright    : Copyright (c) 2018.
 *      @author       : jiaoya.
 *      @created Time : 2018/8/9.
 *      @desc         : 工具类基类
 * </pre>
 */
public class Utils {

    private static Application mApplication;

    /**
     * 初始化工具类
     *
     * @param app 上下文
     */
    public static void init(@NonNull final Application app) {
        if (mApplication == null) {
            mApplication = app;
        }
        if (mApplication.equals(app)) {
            return;
        }
        mApplication = app;
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getAppContext() {
        if (mApplication != null) return mApplication.getApplicationContext();
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取Application
     *
     * @return
     */
    public static Application getApp() {
        if (mApplication != null) return mApplication;
        throw new NullPointerException("u should init first");
    }

    public interface Consumer<T> {
        void accept(T t);
    }

    public abstract static class Task<Result> extends ThreadUtils.SimpleTask<Result> {

        private Consumer<Result> mConsumer;

        public Task(final Consumer<Result> consumer) {
            mConsumer = consumer;
        }

        @Override
        public void onSuccess(Result result) {
            if (mConsumer != null) {
                mConsumer.accept(result);
            }
        }
    }

}
