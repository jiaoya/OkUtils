package com.albert.okutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MODIFY_PHONE_STATE;
import static android.content.Context.WIFI_SERVICE;
import static com.albert.okutils.Utils.getApp;


/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/8/21.
 *      Desc         : 网络相关
 * </pre>
 */
public class NetworkUtils {

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings() {
        getApp().startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    /**
     * 判断网络是否连接
     * Return whether network is connected.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */
    @Deprecated
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isConnectedOld() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用,非常严格，容易造成超时
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     * <p>The default ping ip: 223.5.5.5</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing() {
        return isAvailableByPing(null);
    }

    /**
     * 判断网络是否可用，非常严格，容易造成超时
     * Return whether network is available using ping.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param ip The ip address.
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing(String ip) {
        if (ip == null || ip.length() <= 0) {
            ip = "223.5.5.5";// default ping ip
        }
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg);
        }
        if (result.successMsg != null) {
            Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg);
        }
        return ret;
    }

    /**
     * 判断移动数据是否打开
     * Return whether mobile data is enabled.
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */
    public static boolean getMobileDataEnabled() {
        try {
            TelephonyManager tm =
                    (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) return false;
            @SuppressLint("PrivateApi")
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     * Set mobile data enabled.
     * <p>Must hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @RequiresPermission(MODIFY_PHONE_STATE)
    public static void setMobileDataEnabled(final boolean enabled) {
        try {
            TelephonyManager tm =
                    (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) return;
            Method setMobileDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否是移动数据
     * Return whether using mobile data.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @Deprecated
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断网络是否是 4G
     * Return whether using 4G.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @Deprecated
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null
                && info.isAvailable()
                && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断 wifi 是否打开
     * Return whether wifi is enabled.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />}</p>
     *
     * @return {@code true}: enabled<br>{@code false}: disabled
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        return manager != null && manager.isWifiEnabled();
    }

    /**
     * 打开或关闭 wifi
     * Set wifi enabled.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    public static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        if (manager == null || enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }

    /**
     * 判断 wifi 是否连接状态
     * Return whether wifi is connected.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: connected<br>{@code false}: disconnected
     */
    @Deprecated
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断 wifi 数据是否可用
     * Return whether wifi is available.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return {@code true}: available<br>{@code false}: unavailable
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailableByPing();
    }

    /**
     * 获取移动网络运营商名称
     * Return the name of network operate.
     *
     * @return the name of network operate
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm =
                (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : "";
    }

    /**
     * 获取当前网络类型
     * Return type of network.
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return type of network
     * <ul>
     * <li>{@link NetworkType#NETWORK_ETHERNET} </li>
     * <li>{@link NetworkType#NETWORK_WIFI    } </li>
     * <li>{@link NetworkType#NETWORK_4G      } </li>
     * <li>{@link NetworkType#NETWORK_3G      } </li>
     * <li>{@link NetworkType#NETWORK_2G      } </li>
     * <li>{@link NetworkType#NETWORK_UNKNOWN } </li>
     * <li>{@link NetworkType#NETWORK_NO      } </li>
     * </ul>
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType() {
        NetworkType netType = NetworkType.NETWORK_NO;

        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                netType = NetworkType.NETWORK_ETHERNET;
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetworkType.NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取 IP 地址
     * Return the ip address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */
    @RequiresPermission(INTERNET)
    public static String getIPAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp() || ni.isLoopback()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) return hostAddress;
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取广播 IP 地址
     * Return the ip address of broadcast.
     *
     * @return the ip address of broadcast
     */
    public static String getBroadcastIpAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;
                List<InterfaceAddress> ias = ni.getInterfaceAddresses();
                for (int i = 0; i < ias.size(); i++) {
                    InterfaceAddress ia = ias.get(i);
                    InetAddress broadcast = ia.getBroadcast();
                    if (broadcast != null) {
                        return broadcast.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取域名 IP 地址
     * Return the domain address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @param domain The name of domain.
     * @return the domain address
     */
    @RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据 WiFi 获取网络 IP 地址
     * Return the ip address by wifi.
     *
     * @return the ip address by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getIpAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        if (wm == null) return "";
        return Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    /**
     * 根据 WiFi 获取网关 IP 地址
     * Return the gate way by wifi.
     *
     * @return the gate way by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getGatewayByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        if (wm == null) return "";
        return Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    /**
     * 根据 WiFi 获取子网掩码 IP 地址
     * Return the net mask by wifi.
     *
     * @return the net mask by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getNetMaskByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        if (wm == null) return "";
        return Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }

    /**
     * 根据 WiFi 获取服务端 IP 地址
     * Return the server address by wifi.
     *
     * @return the server address by wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getServerAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getApp().getSystemService(WIFI_SERVICE);
        if (wm == null) return "";
        return Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
    }

    /**
     * 获取连接WiFi的名称
     *
     * @return
     */
    public static String getWifiName() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiMgr = (WifiManager) getApp().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission")
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

    /**
     * 获取网络链接管理
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static ConnectivityManager getConnectivityManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getApp().getSystemService(ConnectivityManager.class);
        } else {
            return (ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static Network getNetWork() {
        return getConnectivityManager().getActiveNetwork();
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager manager = getConnectivityManager();
        if (manager == null) return null;
        return manager.getActiveNetworkInfo();
    }

    /**
     * 判断网络类型：移动网络
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileNet() {
        // 低版本判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return isMobileData();
        }
        // 大于等于 23
        ConnectivityManager cm = getConnectivityManager();
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }

    /**
     * 判断网络类型：Wi-Fi类型
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiNet() {
        // 低版本判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return isWifiConnected();
        }
        ConnectivityManager cm = getConnectivityManager();
        Network network = cm.getActiveNetwork();
        if (null == network) {
            return false;
        }
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        if (null == capabilities) {
            return false;
        }
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }

    /**
     * 判断网络是否连接
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ConnectivityManager cm = getConnectivityManager();
            Network network = cm.getActiveNetwork();
            if (null == network) {
                return false;
            }
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if (null == capabilities) {
                return false;
            }
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            return isConnectedOld();
        }
    }

    // 注册回调
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static void registerNetworkCallback(ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager cm = getConnectivityManager();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)        // wifi
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // 蜂窝移动
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)   // 以太网，网线
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN);       // vpn代理
        cm.registerNetworkCallback(builder.build(), callback);
    }


    // 注销回调
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static void unregisterNetworkCallback(ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager cm = getConnectivityManager();
        cm.unregisterNetworkCallback(callback);
    }


    /**
     * 注册网络监听回调
     *
     * @param listener
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static void registerNetworkListener(NetworkListener listener) {
        if (NET_LISTENER_S == null || listener == null) {
            NET_LISTENER_S = new HashMap<>();
        } else if (NET_LISTENER_S.containsKey(listener)) {
            return;
        }
        ConnectivityManager cm = getConnectivityManager();
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)        // wifi
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)   // 蜂窝移动
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)   // 以太网，网线
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN);       // vpn代理
        // 注册监听
        NetworkCallback networkCallback = new NetworkCallback(listener);
        NET_LISTENER_S.put(listener, networkCallback);
        cm.registerNetworkCallback(builder.build(), networkCallback);
    }


    /**
     * 注销网络监听回调
     *
     * @param listener
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static void unregisterNetworkListener(NetworkListener listener) {
        if (NET_LISTENER_S == null || listener == null) {
            return;
        }
        NetworkCallback networkCallback = NET_LISTENER_S.get(listener);
        if (networkCallback == null) {
            return;
        }
        ConnectivityManager cm = getConnectivityManager();
        cm.unregisterNetworkCallback(networkCallback);
        NET_LISTENER_S.remove(listener);
    }

    private static HashMap<NetworkListener, NetworkCallback> NET_LISTENER_S;

    private static class NetworkCallback extends ConnectivityManager.NetworkCallback {

        NetworkListener networkListener;

        public NetworkCallback(NetworkListener networkListener) {

            this.networkListener = networkListener;
        }

        /**
         * 当框架连接并声明新网络可供使用时调用。。
         *
         * @param network
         */
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            networkListener.onConnect(true);
        }

        /**
         * 失去链接
         *
         * @param network
         */
        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            networkListener.onConnect(false);
        }

        /**
         * 网络链接发生变化，如 断网链接时
         *
         * @param network
         * @param linkProperties
         */
        @Override
        public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
        }

        /**
         * 网络发生改版  移动网络、wifi切换时
         *
         * @param network
         * @param networkCapabilities
         */
        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                // 当前链接的时移动网络
                networkListener.onMobileNet();
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // 当前链接的时wifi
                networkListener.onWifiNet();
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                //  以太网，网线

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                // vpn代理

            }
        }

    }

    /**
     * 网络监听
     */
    public interface NetworkListener {

        /**
         * 链接状态
         *
         * @param isConnect
         */
        void onConnect(boolean isConnect);

        /**
         * 移动网络
         */
        default void onMobileNet() {

        }

        /**
         * wifi
         */
        default void onWifiNet() {

        }

    }


}
