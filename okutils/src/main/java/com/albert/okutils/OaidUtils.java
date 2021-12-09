package com.albert.okutils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2021.
 *      Author       : jiaoya.
 *      Created Time : 2021/3/3.
 *      Desc         : 国内设备唯一标识
 * </pre>
 */
public class OaidUtils implements IIdentifierListener {

    /**
     * 匿名设备标识符（OAID)：可以连接所有应用数据的标识符，移动智能终端系统首次启动后立即生成，可用于广告业务
     */
    private static String OAID;
    /**
     * 应用匿名设备标识符（AAID）：第三方应用获取的匿名设备标识，可在应用安装时产生，可用于用户统计等
     */
    private static String AAID;
    /**
     * 开发者匿名设备标识符（VAID）：用于开放给开发者的设备标识符，可在应用安装时产生，可用于同一开发者不同应用之间的推荐
     */
    private static String VAID;

    private AppIdsUpdater listener;

    public OaidUtils(Context cxt, AppIdsUpdater callback) {
        listener = callback;
        getDeviceIds(cxt);
    }

    private void getDeviceIds(Context cxt) {
        // 方法调用
        int nres = CallFromReflect(cxt);
        String error = "";
        if (nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {
            error = "不支持的设备";
        } else if (nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {
            error = "加载配置文件出错";
        } else if (nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {
            error = "不支持的设备厂商";
        } else if (nres == ErrorCode.INIT_ERROR_RESULT_DELAY) {
            error = "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程";
        } else if (nres == ErrorCode.INIT_HELPER_CALL_ERROR) {
            error = "反射调用出错";
        }
        LogUtils.e(getClass().getSimpleName(), "return value: " + nres + "=" + error);
    }

    /**
     * 方法调用
     */
    private int CallFromReflect(Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

//    @Override
//    public void onSupport(IdSupplier idSupplier) {
//        OAID = idSupplier.getOAID();
//        AAID = idSupplier.getAAID();
//        VAID = idSupplier.getVAID();
//        if (listener != null) {
//            listener.OnIdsAvalid(OAID, AAID, VAID);
//        }
//    }

    @Override
    public void OnSupport(boolean b, IdSupplier idSupplier) {
        if (idSupplier == null) {
            return;
        }
        OAID = idSupplier.getOAID();
        AAID = idSupplier.getAAID();
        VAID = idSupplier.getVAID();
        if (listener != null) {
            listener.OnIdsAvalid(OAID, AAID, VAID);
        }
    }


    public interface AppIdsUpdater {
        void OnIdsAvalid(@NonNull String OAID, String AAID, String VAID);
    }


    /**
     * 匿名设备标识符（OAID）
     *
     * @return
     */
    public static String getOAID() {
        return OAID;
    }

    /**
     * 应用匿名设备标识符（AAID）：
     *
     * @return
     */
    public static String getAAID() {
        return AAID;
    }


    /**
     * 开发者匿名设备标识符（VAID）
     *
     * @return
     */
    public static String getVAID() {
        return VAID;
    }
}
