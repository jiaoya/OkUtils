package com.albert.okutils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;


import com.albert.okutils.datastore.DataStoreUtils;
import com.albert.okutils.file.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.SEND_SMS;

import static com.albert.okutils.Utils.getApp;
import static com.albert.okutils.Utils.getAppContext;

/**
 * <pre>
 *      copyright    : Copyright (c) 2018.
 *      author       : jiaoya.
 *      created Time : 2018/8/9.
 *      desc         : 设备相关
 * </pre>
 */
public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static String keyUuid = "key_uuid";
    private static String mUUID;

    /**
     * 判断设备是否是手机
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 判断是否是平板
     *
     * @return
     */
    public static boolean isTablet() {
        return isTablet();
    }

    /**
     * 获取uuid == 设备id>电话号码>随机生成
     *
     * @returns
     */
    public static String getUUID() {
        if (NullUtil.notEmpty(mUUID)) {
            return mUUID;
        }
        mUUID = OaidUtils.getOAID();
        if (NullUtil.notEmpty(mUUID)) {
            return mUUID;
        }
        mUUID = getDeviceId();
        if (NullUtil.notEmpty(mUUID)) {
            return mUUID;
        }
        mUUID = DataStoreUtils.getStringValue(keyUuid);
        if (NullUtil.notEmpty(mUUID)) {
            return mUUID;
        }
        if (!NullUtil.notEmpty(mUUID)) {
            mUUID = UUID.randomUUID().toString();
        }
        DataStoreUtils.saveString(keyUuid, mUUID);
        return mUUID;
    }

    public static String getOAID() {
        return OaidUtils.getOAID();
    }


    /**
     * 获取设备id
     * Return the unique device id.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(getAppContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // 大于10.0
                    return null;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 大于8.0
                    String imei = tm.getImei();
                    if (!TextUtils.isEmpty(imei)) return imei;
                    String meid = tm.getMeid();
                    return TextUtils.isEmpty(meid) ? "" : meid;
                } else {
                    return tm.getDeviceId();
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 获取电话号码-需要权限
     * <p>Must hold
     * * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" &&
     * uses-permission android:name="android.permission.READ_SMS" />}</p>
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(allOf = {READ_PHONE_STATE, READ_SMS})
    public static String getPhoneNumber() {
        TelephonyManager phoneMgr = (TelephonyManager) getAppContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getAppContext(), READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getAppContext(), READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        if (phoneMgr != null) {
            return phoneMgr.getLine1Number();
        } else {
            return "";
        }
    }

    /**
     * 获取序列号
     * Return the serial of device.
     *
     * @return the serial of device
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getSerial() {
        if (ActivityCompat.checkSelfPermission(getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
        }
        return "";
    }

    /**
     * 获取 IMEI 码
     * Return the IMEI.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMEI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI() {
        try {

            if (ActivityCompat.checkSelfPermission(getAppContext(), READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            TelephonyManager tm =
                    (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm != null ? tm.getImei() : "";
            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Method method = tm.getClass().getMethod("getImei", int.class);
//                String imei = (String) method.invoke(tm, 1);
//                if (imei != null) {
//                    return imei;
//                }
//                return tm != null ? tm.getImei() : "";
//            }
            return tm != null ? tm.getDeviceId() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取 MEID 码
     * Return the MEID.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the MEID
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getMEID() {
        if (ActivityCompat.checkSelfPermission(getAppContext(), READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm != null ? tm.getMeid() : "";
        } else {
            return tm != null ? tm.getDeviceId() : "";
        }
    }

    /**
     * 获取 IMSI 码
     * Return the IMSI.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the IMSI
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMSI() {
        if (ActivityCompat.checkSelfPermission(getAppContext(), READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSubscriberId() : "";
    }

    /**
     * 获取移动终端类型
     * Returns the current phone type.
     *
     * @return the current phone type
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM }</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA}</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP }</li>
     * </ul>
     */
    public static int getPhoneType() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    /**
     * 判断 sim 卡是否准备好
     * Return whether sim card state is ready.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSimCardReady() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * 获取 Sim 卡运营商名称
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : "";
    }

    /**
     * 获取 Sim 卡运营商名称
     * Return the sim operator using mnc.
     *
     * @return the sim operator
     */
    public static String getSimOperatorByMnc() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null) return null;
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
            case "46020":
                return "中国移动";
            case "46001":
            case "46006":
            case "46009":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return operator;
        }
    }

    /**
     * 获取手机状态信息
     * Return the phone status.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return DeviceId = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * PhoneType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getPhoneStatus() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) return "";
        String str = "";
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "PhoneType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    /**
     * 跳至拨号界面
     * Skip to dial.
     *
     * @param phoneNumber The phone number.
     */
    public static void dial(final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 拨打 phoneNumber
     * Make a phone call.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CALL_PHONE" />}</p>
     *
     * @param phoneNumber The phone number.
     */
    @RequiresPermission(CALL_PHONE)
    public static void call(final String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 跳至发送短信界面
     * Send sms.
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    public static void sendSms(final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 发送短信
     * Send sms silently.
     * <p>Must hold {@code <uses-permission android:name="android.permission.SEND_SMS" />}</p>
     *
     * @param phoneNumber The phone number.
     * @param content     The content.
     */
    @RequiresPermission(SEND_SMS)
    public static void sendSmsSilent(final String phoneNumber, final String content) {
        if (TextUtils.isEmpty(content)) return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(getApp(), 0, new Intent("send"), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }


    /**
     * 如果有读写权限-存储SD卡：如果有权限，放在内部存储的 android/包名/key_uuid，没有 用SharePreference
     *
     * @param uuid
     */
    private static void SaveUUID2Sd(String uuid) {
        //获取外部设备
        if (ActivityCompat.checkSelfPermission(getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                File fileDir = new File(Environment.getExternalStorageDirectory() + "/Android/" + getApp().getPackageName());
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory() + "/Android/" + getApp().getPackageName(), keyUuid);
                if (!file.exists()) {
                    file.createNewFile();
                    FileOutputStream outStream = new FileOutputStream(file);
//                //写入文件
//                outStream.write(uuid.getBytes());
//                outStream.close();
                    OutputStreamWriter oStreamWriter = new OutputStreamWriter(outStream, "utf-8");
                    oStreamWriter.append(uuid);
                    oStreamWriter.close();

                    DataStoreUtils.saveString(keyUuid, uuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
                DataStoreUtils.saveString(keyUuid, uuid);
            }
        } else {
            DataStoreUtils.saveString(keyUuid, uuid);
        }
    }

    /**
     * 获取uuid存储
     *
     * @return
     */
    private static String getUUIDFile() {
        if (ActivityCompat.checkSelfPermission(getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/Android/" + getApp().getPackageName(), keyUuid);
                FileInputStream is = new FileInputStream(file);
                byte[] b = new byte[is.available()];
                is.read(b);
                String uuid = new String(b, StandardCharsets.UTF_8);
                is.close();
                if (StringUtils.isMessyCode(uuid)) {
                    FileUtils.deleteDir(Environment.getExternalStorageDirectory() + "/Android/" + getApp().getPackageName());
                    return null;
                } else {
                    return uuid;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DataStoreUtils.getStringValue(keyUuid);
            }

        } else {
            return DataStoreUtils.getStringValue(keyUuid);
        }
    }

    /**
     * 判断设备是否rooted
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备系统版本号
     *
     * @return the version name of device's system
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备系统版本码
     *
     * @return version code of device's system
     */
    public static int getSDKVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备 AndroidID
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        String id = Settings.Secure.getString(
                getApp().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        return id == null ? "" : id;
    }

    /**
     * 获取设备型号
     *
     * @return version code of device's system
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * 获取设备 MAC 地址
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static String getMacAddress() {
        return getMacAddress((String[]) null);
    }

    /**
     * 获取设备 MAC 地址
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static String getMacAddress(final String... excepts) {
        String macAddress = getMacAddressByWifiInfo();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        return "";
    }

    private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (excepts == null || excepts.length == 0) {
            return !"02:00:00:00:00:00".equals(address);
        }
        for (String filter : excepts) {
            if (address.equals(filter)) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getMacAddressByWifiInfo() {
        try {
            WifiManager wifi = (WifiManager) getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备厂商
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取设备 ABIs
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    public static String[] getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS;
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return new String[]{Build.CPU_ABI};
        }
    }

    /**
     * 关机
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     */
    public static void shutdown() {
        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 重启
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"} in manifest.</p>
     */
    public static void reboot() {
        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        getApp().sendBroadcast(intent);
    }

    /**
     * 重启
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to
     *               request special boot modes, or null.
     */
    public static void reboot(final String reason) {
        PowerManager mPowerManager =
                (PowerManager) getApp().getSystemService(Context.POWER_SERVICE);
        try {
            if (mPowerManager == null) return;
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启到 recovery
     * <p>Requires root permission.</p>
     */
    public static void reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true);
    }

    /**
     * 重启到 bootloader
     * <p>Requires root permission.</p>
     */
    public static void reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true);
    }
}
