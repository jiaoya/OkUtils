package com.albert.okutils.datastore;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.albert.okutils.AppUtils;
import com.albert.okutils.NullUtil;
import com.albert.okutils.Utils;
import com.albert.okutils.json.GsonUtils;
import com.google.gson.Gson;

import java.lang.reflect.Type;

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

    /**
     * 同步获取string
     *
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        return getValue(counte, null);
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
            return null;
        }
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 string 值
     *
     * @param key
     * @param callback
     */
    public static void getStringValue(String key, DataStoreCallback<String> callback) {
        getStringValue(key, null, callback);
    }

    /**
     * 异步 获取 string 值
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

    public static Boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    /**
     * 同步获取boolean
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Boolean getBooleanValue(String key, Boolean defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return false;
        }
        Preferences.Key<Boolean> counte = PreferencesKeys.booleanKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 Boolean
     *
     * @param key
     * @param callback
     */
    public static void getBooleanValue(String key, DataStoreCallback<Boolean> callback) {
        getBooleanValue(key, false, callback);
    }

    /**
     * 异步 获取 Boolean
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

    /**
     * 同步获取int
     *
     * @param key
     * @return
     */
    public static Integer getIntValue(String key) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        Preferences.Key<Integer> counte = PreferencesKeys.intKey(key);
        return getValue(counte, null);
    }

    /**
     * 同步获取int
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Integer getIntValue(String key, Integer defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return null;
        }
        Preferences.Key<Integer> counte = PreferencesKeys.intKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 int
     *
     * @param key
     * @param callback
     */
    public static void getIntValue(String key, DataStoreCallback<Integer> callback) {
        getIntValue(key, -1, callback);
    }

    /**
     * 异步 获取 int
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

    /**
     * 同步获取 long
     *
     * @param key
     * @return
     */
    public static Long getLongValue(String key) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        Preferences.Key<Long> counte = PreferencesKeys.longKey(key);
        return getValue(counte, null);
    }

    /**
     * 同步获取 long
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Long getLongValue(String key, Long defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return null;
        }
        Preferences.Key<Long> counte = PreferencesKeys.longKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 long
     *
     * @param key
     * @param callback
     */
    public static void getLongValue(String key, DataStoreCallback<Long> callback) {
        getLongValue(key, null, callback);
    }

    /**
     * 异步 获取 long
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

    /**
     * 同步获取float
     *
     * @param key
     * @return
     */
    public static Float getFloatValue(String key) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        Preferences.Key<Float> counte = PreferencesKeys.floatKey(key);
        return getValue(counte, null);
    }

    /**
     * 同步获取float
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Float getFloatValue(String key, Float defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return null;
        }
        Preferences.Key<Float> counte = PreferencesKeys.floatKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 Float
     *
     * @param key
     * @param callback
     */
    public static void getFloatValue(String key, DataStoreCallback<Float> callback) {
        getFloatValue(key, null, callback);
    }

    /**
     * 异步 获取 Float
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

    /**
     * 同步获取
     *
     * @param key
     * @return
     */
    public static Double getDoubleValue(String key) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        Preferences.Key<Double> counte = PreferencesKeys.doubleKey(key);
        return getValue(counte, null);
    }

    /**
     * 同步获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static Double getDoubleValue(String key, Double defaultValue) {
        if (!NullUtil.notEmpty(key) || defaultValue == null) {
            return null;
        }
        Preferences.Key<Double> counte = PreferencesKeys.doubleKey(key);
        return getValue(counte, defaultValue);
    }

    /**
     * 异步 获取 Double
     *
     * @param key
     * @param callback
     */
    public static void getDoubleValue(String key, DataStoreCallback<Double> callback) {
        getDoubleValue(key, null, callback);
    }

    /**
     * 异步 获取 Double
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
     * 通过 json去存储对象 非必要不推荐使用
     *
     * @param key
     * @param value
     */
    public static void saveObject(String key, Object value) {
        if (!NullUtil.notEmpty(key) || value == null) {
            return;
        }
        String data = GsonUtils.getGson().toJson(value);
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        save(counte, data);
    }

    /**
     * 通过json获取对象，非必要不推荐使用
     *
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T getObject(String key, Class<T> classOfT) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        String data = getStringValue(key);
        if (!NullUtil.notEmpty(data)) {
            return null;
        }
        return GsonUtils.getGson().fromJson(data, classOfT);
    }

    /**
     * 通过json获取对象,非必要不推荐使用
     *
     * @param key
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T getObject(String key, Type obj) {
        if (!NullUtil.notEmpty(key)) {
            return null;
        }
        String base64 = getStringValue(key);
        if (NullUtil.notEmpty(base64)) {
            return new Gson().fromJson(base64, obj);
        } else {
            return null;
        }
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
     * 异步 Preferences取值
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

    /**
     * 删除
     *
     * @param key
     * @param <T>
     */
    public static <T> void clear(String key) {
        Preferences.Key<String> counte = PreferencesKeys.stringKey(key);
        clear(counte);
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

    /**
     * 清楚所有
     */
    public static void clearAll() {
        getDataStore()
                .updateDataAsync(new Function<Preferences, Single<Preferences>>() {
                    @Override
                    public Single<Preferences> apply(@NonNull Preferences preferences) {
                        MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                        mutablePreferences.clear();
                        return Single.just(mutablePreferences);
                    }
                });
    }

    //
    // MutablePreferences : 获取 Preferences 的可变副本，其中包含此 Preferences 中的所有首选项。
    // 这可用于更新您的首选项，而无需在 DataStore.updateData 中从头开始构建新的 Preferences 对象
    //
}
