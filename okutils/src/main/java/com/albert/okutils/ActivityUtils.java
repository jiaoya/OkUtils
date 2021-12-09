package com.albert.okutils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.albert.okutils.Utils.getApp;


/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/9/10.
 *      Desc         : Activity 相关
 * </pre>
 */
public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断 Activity 是否存在
     * Return whether the activity exists.
     *
     * @param pkg The name of the package.
     * @param cls The name of the class.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityExists(@NonNull final String pkg,
                                           @NonNull final String cls) {
        Intent intent = new Intent();
        intent.setClassName(pkg, cls);
        return !(getApp().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(getApp().getPackageManager()) == null ||
                getApp().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 根据上下文获取 Activity
     *
     * @param context The context.
     * @return the activity by context.
     */
    @Nullable
    public static Activity getActivityByContext(@NonNull Context context) {
        Activity activity = getActivityByContextInner(context);
        if (!isActivityAlive(activity)) return null;
        return activity;
    }

    /**
     * 通过 ContextInner 获取 Activity
     *
     * @param context
     * @return
     */
    @Nullable
    private static Activity getActivityByContextInner(@Nullable Context context) {
        if (context == null) return null;
        List<Context> list = new ArrayList<>();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            Activity activity = getActivityFromDecorContext(context);
            if (activity != null) return activity;
            list.add(context);
            context = ((ContextWrapper) context).getBaseContext();
            if (context == null) {
                return null;
            }
            if (list.contains(context)) {
                // loop context
                return null;
            }
        }
        return null;
    }

    /**
     * 从装饰上下文获取活动
     *
     * @param context
     * @return
     */
    @Nullable
    private static Activity getActivityFromDecorContext(@Nullable Context context) {
        if (context == null) return null;
        if (context.getClass().getName().equals("com.android.internal.policy.DecorContext")) {
            try {
                Field mActivityContextField = context.getClass().getDeclaredField("mActivityContext");
                mActivityContextField.setAccessible(true);
                // noinspection ConstantConditions,unchecked
                return ((WeakReference<Activity>) mActivityContextField.get(context)).get();
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * 判断 Activity 是否存活
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isActivityAlive(final Activity activity) {
        return activity != null && !activity.isFinishing()
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed());
    }


    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param clz      The activity class.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(), null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param clz      The activity class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     final Bundle options) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(), options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     final View... sharedElements) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity  The activity.
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(activity, null, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param clz      The activity class.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param clz      The activity class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     @NonNull final Bundle options) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(), options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     final View... sharedElements) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param activity  The activity.
     * @param clz       The activity class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final Class<? extends Activity> clz,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(activity, extras, activity.getPackageName(), clz.getName(),
                getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(activity, null, pkg, cls, null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     final Bundle options) {
        startActivity(activity, null, pkg, cls, options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     final View... sharedElements) {
        startActivity(activity, null, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity  The activity.
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(activity, null, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param extras   The Bundle of extras to add to this intent.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls) {
        startActivity(activity, extras, pkg, cls, null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras   The Bundle of extras to add to this intent.
     * @param activity The activity.
     * @param pkg      The name of the package.
     * @param cls      The name of the class.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     final Bundle options) {
        startActivity(activity, extras, pkg, cls, options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     final View... sharedElements) {
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras    The Bundle of extras to add to this intent.
     * @param pkg       The name of the package.
     * @param cls       The name of the class.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@NonNull final Bundle extras,
                                     @NonNull final Activity activity,
                                     @NonNull final String pkg,
                                     @NonNull final String cls,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Intent intent) {
        startActivity(intent, activity, null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity The activity.
     * @param intent   The description of the activity to start.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Intent intent,
                                     final Bundle options) {
        startActivity(intent, activity, options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity       The activity.
     * @param intent         The description of the activity to start.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Intent intent,
                                     final View... sharedElements) {
        startActivity(intent, activity, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity  The activity.
     * @param intent    The description of the activity to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivity(@NonNull final Activity activity,
                                     @NonNull final Intent intent,
                                     @AnimRes final int enterAnim,
                                     @AnimRes final int exitAnim) {
        startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              final Bundle options) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              final View... sharedElements) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              @AnimRes final int enterAnim,
                                              @AnimRes final int exitAnim) {
        startActivityForResult(activity, null, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, null);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              @NonNull final Bundle options) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, options);
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param clz            The activity class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              final View... sharedElements) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity
     * Start the activity.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param clz         The activity class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final Class<? extends Activity> clz,
                                              final int requestCode,
                                              @AnimRes final int enterAnim,
                                              @AnimRes final int exitAnim) {
        startActivityForResult(activity, extras, activity.getPackageName(), clz.getName(),
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final String pkg,
                                              @NonNull final String cls,
                                              final int requestCode) {
        startActivityForResult(activity, extras, pkg, cls, requestCode, null);
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param activity    The activity.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final String pkg,
                                              @NonNull final String cls,
                                              final int requestCode,
                                              final Bundle options) {
        startActivityForResult(activity, extras, pkg, cls, requestCode, options);
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param extras         The Bundle of extras to add to this intent.
     * @param activity       The activity.
     * @param pkg            The name of the package.
     * @param cls            The name of the class.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final String pkg,
                                              @NonNull final String cls,
                                              final int requestCode,
                                              final View... sharedElements) {
        startActivityForResult(activity, extras, pkg, cls,
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param extras      The Bundle of extras to add to this intent.
     * @param pkg         The name of the package.
     * @param cls         The name of the class.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@NonNull final Bundle extras,
                                              @NonNull final Activity activity,
                                              @NonNull final String pkg,
                                              @NonNull final String cls,
                                              final int requestCode,
                                              @AnimRes final int enterAnim,
                                              @AnimRes final int exitAnim) {
        startActivityForResult(activity, extras, pkg, cls,
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Intent intent,
                                              final int requestCode) {
        startActivityForResult(intent, activity, requestCode, null);
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options     Additional options for how the Activity should be started.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Intent intent,
                                              final int requestCode,
                                              final Bundle options) {
        startActivityForResult(intent, activity, requestCode, options);
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param activity       The activity.
     * @param intent         The description of the activity to start.
     * @param requestCode    if &gt;= 0, this code will be returned in
     *                       onActivityResult() when the activity exits.
     * @param sharedElements The names of the shared elements to transfer to the called
     *                       Activity and their associated Views.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Intent intent,
                                              final int requestCode,
                                              final View... sharedElements) {
        startActivityForResult(intent, activity,
                requestCode, getOptionsBundle(activity, sharedElements));
    }

    /**
     * 启动 Activity 为返回结果
     * Start the activity for result.
     *
     * @param activity    The activity.
     * @param intent      The description of the activity to start.
     * @param requestCode if &gt;= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param enterAnim   A resource ID of the animation resource to use for the
     *                    incoming activity.
     * @param exitAnim    A resource ID of the animation resource to use for the
     *                    outgoing activity.
     */
    public static void startActivityForResult(@NonNull final Activity activity,
                                              @NonNull final Intent intent,
                                              final int requestCode,
                                              @AnimRes final int enterAnim,
                                              @AnimRes final int exitAnim) {
        startActivityForResult(intent, activity,
                requestCode, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }


    /**
     * 启动多个 Activity
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     */
    public static void startActivities(@NonNull final Activity activity,
                                       @NonNull final Intent[] intents) {
        startActivities(intents, activity, null);
    }

    /**
     * 启动多个 Activity
     * Start activities.
     *
     * @param activity The activity.
     * @param intents  The descriptions of the activities to start.
     * @param options  Additional options for how the Activity should be started.
     */
    public static void startActivities(@NonNull final Activity activity,
                                       @NonNull final Intent[] intents,
                                       final Bundle options) {
        startActivities(intents, activity, options);
    }

    /**
     * 启动多个 Activity
     * Start activities.
     *
     * @param activity  The activity.
     * @param intents   The descriptions of the activities to start.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void startActivities(@NonNull final Activity activity,
                                       @NonNull final Intent[] intents,
                                       @AnimRes final int enterAnim,
                                       @AnimRes final int exitAnim) {
        startActivities(intents, activity, getOptionsBundle(activity, enterAnim, exitAnim));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 获取启动项 Activity
     * Return the name of launcher activity.
     *
     * @return the name of launcher activity
     */
    public static String getLauncherActivity() {
        return getLauncherActivity(getApp().getPackageName());
    }

    /**
     * 获取启动项 Activity
     * Return the name of launcher activity.
     *
     * @param pkg The name of the package.
     * @return the name of launcher activity
     */
    public static String getLauncherActivity(@NonNull final String pkg) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = getApp().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(pkg)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + pkg;
    }

    /**
     * 结束 Activity
     * Finish the activity.
     *
     * @param activity The activity.
     */
    public static void finishActivity(@NonNull final Activity activity) {
        finishActivity(activity, false);
    }

    /**
     * 结束 Activity
     * Finish the activity.
     *
     * @param activity   The activity.
     * @param isLoadAnim True to use animation for the outgoing activity, false otherwise.
     */
    public static void finishActivity(@NonNull final Activity activity, final boolean isLoadAnim) {
        activity.finish();
        if (!isLoadAnim) {
            activity.overridePendingTransition(0, 0);
        }
    }

    /**
     * 结束 Activity
     * Finish the activity.
     *
     * @param activity  The activity.
     * @param enterAnim A resource ID of the animation resource to use for the
     *                  incoming activity.
     * @param exitAnim  A resource ID of the animation resource to use for the
     *                  outgoing activity.
     */
    public static void finishActivity(@NonNull final Activity activity,
                                      @AnimRes final int enterAnim,
                                      @AnimRes final int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }


    /**
     * 获取activity的图标
     * Return the icon of activity.
     *
     * @param activity The activity.
     * @return the icon of activity
     */
    public static Drawable getActivityIcon(@NonNull final Activity activity) {
        return getActivityIcon(activity.getComponentName());
    }

    /**
     * 获取activity的图标
     * Return the icon of activity.
     *
     * @param clz The activity class.
     * @return the icon of activity
     */
    public static Drawable getActivityIcon(@NonNull final Class<? extends Activity> clz) {
        return getActivityIcon(new ComponentName(getApp(), clz));
    }

    /**
     * 获取activity的图标
     * Return the icon of activity.
     *
     * @param activityName The name of activity.
     * @return the icon of activity
     */
    public static Drawable getActivityIcon(@NonNull final ComponentName activityName) {
        PackageManager pm = getApp().getPackageManager();
        try {
            return pm.getActivityIcon(activityName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取activity的logo
     * Return the logo of activity.
     *
     * @param activity The activity.
     * @return the logo of activity
     */
    public static Drawable getActivityLogo(@NonNull final Activity activity) {
        return getActivityLogo(activity.getComponentName());
    }

    /**
     * 获取activity的logo
     * Return the logo of activity.
     *
     * @param clz The activity class.
     * @return the logo of activity
     */
    public static Drawable getActivityLogo(@NonNull final Class<? extends Activity> clz) {
        return getActivityLogo(new ComponentName(getApp(), clz));
    }

    /**
     * 获取activity的logo
     * Return the logo of activity.
     *
     * @param activityName The name of activity.
     * @return the logo of activity
     */
    public static Drawable getActivityLogo(@NonNull final ComponentName activityName) {
        PackageManager pm = getApp().getPackageManager();
        try {
            return pm.getActivityLogo(activityName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity
     * @return true:已销毁
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null ||
                mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }


    private static void startActivity(final Context context,
                                      final Bundle extras,
                                      final String pkg,
                                      final String cls,
                                      final Bundle options) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new ComponentName(pkg, cls));
        startActivity(intent, context, options);
    }

    private static void startActivity(final Intent intent,
                                      final Context context,
                                      final Bundle options) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options);
        } else {
            context.startActivity(intent);
        }
    }

    private static void startActivityForResult(final Activity activity,
                                               final Bundle extras,
                                               final String pkg,
                                               final String cls,
                                               final int requestCode,
                                               final Bundle options) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) intent.putExtras(extras);
        intent.setComponent(new ComponentName(pkg, cls));
        startActivityForResult(intent, activity, requestCode, options);
    }

    private static void startActivityForResult(final Intent intent,
                                               final Activity activity,
                                               final int requestCode,
                                               final Bundle options) {
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    private static void startActivities(final Intent[] intents,
                                        final Context context,
                                        final Bundle options) {
        if (!(context instanceof Activity)) {
            for (Intent intent : intents) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivities(intents, options);
        } else {
            context.startActivities(intents);
        }
    }

    private static Bundle getOptionsBundle(final Context context,
                                           final int enterAnim,
                                           final int exitAnim) {
        return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
    }

    private static Bundle getOptionsBundle(final Activity activity,
                                           final View[] sharedElements) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null;
        if (sharedElements == null) return null;
        int len = sharedElements.length;
        if (len <= 0) return null;
        @SuppressWarnings("unchecked")
        Pair<View, String>[] pairs = new Pair[len];
        for (int i = 0; i < len; i++) {
            pairs[i] = Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
        }
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
    }
}
