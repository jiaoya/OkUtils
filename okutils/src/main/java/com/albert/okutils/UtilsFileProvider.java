package com.albert.okutils;

import android.app.Application;

import androidx.core.content.FileProvider;

/**
 * <pre>
 *     author: jiaoya by blankj
 *     blog  : http://blankj.com
 *     time  : 2021/12/05
 *     desc  :
 * </pre>
 */
public class UtilsFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        Utils.init((Application) getContext().getApplicationContext());
        return true;
    }

}
