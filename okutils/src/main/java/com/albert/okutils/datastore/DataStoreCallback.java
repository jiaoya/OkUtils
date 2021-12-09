package com.albert.okutils.datastore;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2021.
 *      Author       : jiaoya.
 *      Created Time : 3/5/21.
 *      Desc         :
 * </pre>
 */
public interface DataStoreCallback<T> {
    /**
     * 获取数据回调
     *
     * @param value
     */
    void onValueCallback(T value);
}
