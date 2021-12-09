package com.albert.okutils.datastore;


import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.albert.okutils.AppUtils;
import com.albert.okutils.NullUtil;
import com.albert.okutils.Utils;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2021.
 *      Author       : jiaoya.
 *      Created Time : 2021/2/21.
 *      Desc         : 用来替代SharedPreferences 存储数据。
 *                     Preferences是用来存储基本数据类型,默认存储在“包名的目录下”，
 *                     如果要存储对象，请使用Proto写法
 * </pre>
 */
public class DataStoreUtils {

    private static RxDataStore dataStore;

    private static RxDataStore<Preferences> getDataStore() {
        if (dataStore == null) {
            dataStore = new RxPreferenceDataStoreBuilder(Utils.getAppContext(), /*name=*/ AppUtils.getAppName()).build();
        }
        return dataStore;
    }

    public static String getStringValue(String key) {
        return getStringValue(key, "");
    }

    /**
     * 同步获取string
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringValue(String key, String defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return "";
        }
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getStringValue(String key, DataStoreCallback<String> callback) {
        getStringValue(key, " ", callback);
    }

    /**
     * 根据Preferences 获取 string 值
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getStringValue(String key, String defaultValue, DataStoreCallback<String> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        getValue(counte, defaultValue, callback);
    }

    public static boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    /**
     * 同步获取boolean
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanValue(String key, Boolean defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return false;
        }
        Preferences.Key<Boolean> counte = PreferencesKeys.booleanKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getBooleanValue(String key, DataStoreCallback<Boolean> callback) {
        getBooleanValue(key, false, callback);
    }

    /**
     * 根据Preferences 获取 Boolean
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getBooleanValue(String key, Boolean defaultValue, DataStoreCallback<Boolean> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<Boolean> counte = PreferencesKeys.booleanKey(key);
        getValue(counte, defaultValue, callback);
    }

    public static int getIntValue(String key) {
        return getIntValue(key, -1);
    }

    /**
     * 同步获取int
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntValue(String key, Integer defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return -1;
        }
        Preferences.Key<Integer> counte = PreferencesKeys.intKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getIntValue(String key, DataStoreCallback<Integer> callback) {
        getIntValue(key, -1, callback);
    }

    /**
     * 根据Preferences 获取 int
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getIntValue(String key, Integer defaultValue, DataStoreCallback<Integer> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<Integer> counte = PreferencesKeys.intKey(key);
        getValue(counte, defaultValue, callback);
    }

    public static long getLongValue(String key) {
        return getLongValue(key, -1L);
    }

    /**
     * 同步获取 long
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLongValue(String key, Long defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return -1L;
        }
        Preferences.Key<Long> counte = PreferencesKeys.longKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getLongValue(String key, DataStoreCallback<Long> callback) {
        getLongValue(key, -1L, callback);
    }

    /**
     * 根据Preferences 获取 long
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getLongValue(String key, Long defaultValue, DataStoreCallback<Long> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<Long> counte = PreferencesKeys.longKey(key);
        getValue(counte, defaultValue, callback);
    }

    public static float getFloatValue(String key) {
        return getFloatValue(key, -1.0f);
    }

    /**
     * 同步获取float
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloatValue(String key, Float defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return -1.0f;
        }
        Preferences.Key<Float> counte = PreferencesKeys.floatKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getFloatValue(String key, DataStoreCallback<Float> callback) {
        getFloatValue(key, -1.0f, callback);
    }

    /**
     * 根据Preferences 获取 Float
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getFloatValue(String key, Float defaultValue, DataStoreCallback<Float> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<Float> counte = PreferencesKeys.floatKey(key);
        getValue(counte, defaultValue, callback);
    }

    public static double getDoubleValue(String key) {
        return getDoubleValue(key, -1.0);
    }

    /**
     * 同步获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getDoubleValue(String key, Double defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return -1.0;
        }
        Preferences.Key<Double> counte = PreferencesKeys.doubleKey(key);
        return getValue(counte, defaultValue);
    }

    public static void getDoubleValue(String key, DataStoreCallback<Double> callback) {
        getDoubleValue(key, -1.0, callback);
    }

    /**
     * 根据Preferences 获取 Double
     *
     * @param key
     * @param defaultValue
     * @param callback
     */
    public static void getDoubleValue(String key, Double defaultValue, DataStoreCallback<Double> callback) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return;
        }
        Preferences.Key<Double> counte = PreferencesKeys.doubleKey(key);
        getValue(counte, defaultValue, callback);
    }

    /**
     * 同步获取
     *
     * @param counte
     * @param defaultValue
     * @param <T>
     * @return
     */
    private static <T> T getValue(Preferences.Key<T> counte, T defaultValue) {
        T a = getDataStore().data().blockingFirst().get(counte);
        if (a == null) {
            return defaultValue;
        }
        return a;
    }

    /**
     * Preferences取值
     *
     * @param counte
     * @param defaultValue
     * @param callback
     * @param <T>
     */
    private static <T> void getValue(Preferences.Key<T> counte, T defaultValue, DataStoreCallback<T> callback) {
        getDataStore()
                .data()
                .map(new Function<Preferences, T>() {
                    @Override
                    public T apply(@NonNull Preferences preferences) {
                        T value = preferences.get(counte);
                        if (NullUtil.notEmpty(value)) {
                            return value;
                        } else {
                            return defaultValue;
                        }
                    }
                })
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T s) throws Exception {
                        if (callback != null) {
                            callback.onValueCallback(s);
                        }
                    }
                });
    }


    /*--------------------------------------------------------------------*/

    /**
     * Preferences存储 string
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        save(counte, value);
    }

    /**
     * Preferences存储 boolean
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(String key, Boolean value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<Boolean> counte = PreferencesKeys.booleanKey(key);
        save(counte, value);
    }


    /**
     * Preferences存储 int
     *
     * @param key
     * @param value
     */
    public static void saveInt(String key, Integer value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<Integer> counte = PreferencesKeys.intKey(key);
        save(counte, value);
    }

    /**
     * Preferences存储 long
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, Long value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<Long> counte = PreferencesKeys.longKey(key);
        save(counte, value);
    }

    /**
     * Preferences存储 float
     *
     * @param key
     * @param value
     */
    public static void saveFloat(String key, Float value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<Float> counte = PreferencesKeys.floatKey(key);
        save(counte, value);
    }

    /**
     * Preferences存储double
     *
     * @param key
     * @param value
     */
    public static void saveDouble(String key, Double value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        Preferences.Key<Double> counte = PreferencesKeys.doubleKey(key);
        save(counte, value);
    }

    /**
     * DataStore 的 Preferences 统一写入
     *
     * @param counte
     * @param value
     * @param <T>
     */
    private static <T> void save(Preferences.Key<T> counte, T value) {
        Single<Preferences> updateResult = getDataStore()
                .updateDataAsync(new Function<Preferences, Single<Preferences>>() {
                    @Override
                    public Single<Preferences> apply(@NonNull Preferences preferences) {
                        MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                        //T currentInt = preferences.get(counte);
                        mutablePreferences.set(counte, value);
                        return Single.just(mutablePreferences);
                    }
                });
    }

    /**
     * 删除
     *
     * @param counte
     * @param <T>
     */
    public static <T> void clear(Preferences.Key<T> counte) {
        getDataStore()
                .updateDataAsync(new Function<Preferences, Single<Preferences>>() {
                    @Override
                    public Single<Preferences> apply(@NonNull Preferences preferences) {
                        MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                        mutablePreferences.remove(counte);
                        return Single.just(mutablePreferences);
                    }
                });
    }


}
