package com.albert.okutils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/11/5.
 *      Desc         : 数字处理，比如去零，保留两位小数等
 * </pre>
 */
public class NumberUtils {

    private NumberUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 字符串转int类型
     *
     * @param value
     * @return 失败返回-1
     */
    public static int str2Int(String value) {
        try {
            int a = Integer.parseInt(value);
            return a;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字符串转long类型
     *
     * @param value
     * @return 失败返回-1
     */
    public static long str2Long(String value) {
        try {
            long a = Long.parseLong(value);
            return a;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字符串转float类型
     *
     * @param value
     * @return 失败返回-1
     */
    public static float str2Float(String value) {
        try {
            float a = Float.parseFloat(value);
            return a;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字符串转double类型
     *
     * @param value
     * @return 失败返回-1
     */
    public static double str2Db(String value) {
        try {
            double a = Double.parseDouble(value);
            return a;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(double value) {
        if (value == 0) {
            return "0.00";
        }
        return String.format("%.2f", value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(float value) {
        if (value == 0) {
            return "0.00";
        }
        return String.format("%.2f", value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(int value) {
        if (value == 0) {
            return "0.00";
        }
        return String.format("%.2f", value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(long value) {
        if (value == 0) {
            return "0.00";
        }
        return String.format("%.2f", value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k2Dec(String value) {
        double a = str2Db(value);
        if (a == 0) {
            return "0.00";
        }
//        return String.format("%.2f", a);
        DecimalFormat df = new DecimalFormat("0.00");
        String a1 = df.format(a);
        return a1;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k1Dec(String value) {
        double a = str2Db(value);
        if (a == 0) {
            return "0.0";
        }
//        return String.format("%.2f", a);
        DecimalFormat df = new DecimalFormat("0.0");
        String a1 = df.format(a);
        return a1;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String k1Dec(double value) {
//        double a = str2Db(value+"");
        if (value == 0) {
            return "0.0";
        }
//        return String.format("%.2f", a);
        DecimalFormat df = new DecimalFormat("0.0");
        String a1 = df.format(value);
        return a1;
    }

    /**
     * 数字 去零
     *
     * @param value
     * @return
     */
    public static String removeZero(double value) {
        try {
            String a = BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            return a;
        } catch (Exception e) {
            return value + "";
        }
    }

    /**
     * 数字 去零
     *
     * @param value
     * @return
     */
    public static String removeZero(float value) {
        try {
            String a = BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            return a;
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 数字 去零
     *
     * @param value
     * @return
     */
    public static String removeZero(String value) {
        try {
            String a = BigDecimal.valueOf(str2Db(value)).stripTrailingZeros().toPlainString();
            return a;
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 保留两位小数，如果是小数点是0 则去掉
     *
     * @param value
     * @return
     */
    public static String k2DecAndRmZero(double value) {
        try {
            return removeZero(k2Dec(value));
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * 保留两位小数，如果是小数点是0 则去掉
     *
     * @param value
     * @return
     */
    public static String k2DecAndRmZero(String value) {
        try {
            return removeZero(k2Dec(value));
        } catch (Exception e) {
            return value + "";
        }
    }

    /**
     * 格式化手机号为3-4-4
     *
     * @param phone
     * @return
     */
    public static String formatPhone(String phone) {
        try {
            if (!TextUtils.isEmpty(phone) && phone.length() == 11)
                return phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(7, 11);
            return phone;
        } catch (Exception e) {
            return phone;
        }
    }

    /**
     * 格式化银行卡号，每四位空一格
     *
     * @param bankNum
     * @return
     */
    public static String formatBankNum(String bankNum) {
        try {
            String result = "";
            for (int i = 0; i < bankNum.length(); i++) {
                result += bankNum.charAt(i);
                if ((i + 1) % 4 == 0) {
                    result += " ";
                }
            }
            if (result.endsWith(" ")) {
                result = result.substring(0, result.length() - 1);
            }
            return result;
        } catch (Exception e) {
            return bankNum;
        }
    }

    /**
     * 身份证号，字段加密
     *
     * @param idNumber
     * @return
     */
    public static String formatIdEncrypt(String idNumber) {
        String aa = idNumber;
        aa = aa.substring(0, 4) + "**********" + aa.substring(aa.length() - 4, aa.length());
        return aa;
    }

    /**
     * 手机号加密
     *
     * @param idNumber
     * @return
     */
    public static String formatPhoneNumEncrypt(String idNumber) {
        if (idNumber.length() != 11) {
            return idNumber;
        }
        String aa = idNumber;
        aa = aa.substring(0, 3) + "****" + aa.substring(aa.length() - 4, aa.length());
        return aa;
    }


    /**
     * 校验手机号
     */
    private final static Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3456789]\\d{9}");

    public static boolean isPhone(String phone) {
        if (NullUtil.notEmpty(phone)) {
            Matcher m = MOBILE.matcher(phone);
            return m.matches();
        } else {
            return false;
        }
    }

}
