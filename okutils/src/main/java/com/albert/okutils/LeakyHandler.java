package com.albert.okutils;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/11/6.
 *      Desc         : 如果Handler是个内部类，那么它也会保持它所在的外部类的引用,
 *                     为了避免泄露这个外部类，应该将Handler声明为static嵌套类，
 *                     并且使用对外部类的弱应用。
 *                     这里是外部类，如果是内部类这样写：static class LeakyHandler...，接口去掉，其他代码不变
 * </pre>
 */
public class LeakyHandler extends Handler {

    WeakReference<Activity> mActivity;
    IHandleMessage mHandleMessage;

    public LeakyHandler(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    public LeakyHandler(Activity activity, final IHandleMessage iHandleMessage) {
        mActivity = new WeakReference<Activity>(activity);
        mHandleMessage = iHandleMessage;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = mActivity.get();
        if (activity != null) {
            if (mHandleMessage != null) {
                mHandleMessage.handleMessage(activity, msg);
            }
        }
    }

    public interface IHandleMessage {
        void handleMessage(Activity activity, Message msg);
    }

}
