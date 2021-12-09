package com.albert.okutils.demo;

import android.app.Application;

import com.albert.okutils.LogUtils;
import com.albert.okutils.OaidUtils;
import com.albert.okutils.Utils;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2021.
 *      Author       : jiaoya.
 *      Created Time : 12/9/21.
 *      Desc         :
 * </pre>
 */
public class MyAPP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        new OaidUtils(this, null);
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);

        // GsonUtils.setGsonDelegate();

    }
}