package com.albert.okutils;

import android.content.res.Resources;

import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.util.regex.Pattern.compile;

/**
 * <pre>
 *      Copyright    : Copyright (c) 2018.
 *      Author       : jiaoya.
 *      Created Time : 2018/8/9.
 *      Desc         : 字符串相关
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为 null: null/""/长度0
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(final String s) {
        return s == null || s.isEmpty() || s.equals("null");
    }

    /**
     * 判断字符串是否为 null 或长度为 0
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为 null 或全为空格
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    /**
     * null 转为长度为 0 的字符串
     *
     * @param s The string.
     * @return {@code ""} if string equals null
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s The string.
     * @return the length of string
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s The string.
     * @return the string with first letter upper.
     */
    public static String upperFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s The string.
     * @return the string with first letter lower.
     */
    public static String lowerFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }


    /**
     * 反转字符串
     *
     * @param s The string.
     * @return the reverse string.
     */
    public static String reverse(final String s) {
        if (s == null) return "";
        int len = s.length();
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s The string.
     * @return the DBC string
     */
    public static String toDBC(final String s) {
        if (s == null || s.length() == 0) return "";
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s The string.
     * @return the SBC string
     */
    public static String toSBC(final String s) {
        if (s == null || s.length() == 0) return "";
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 替换、过滤特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) {
        //替换中文标号
        try {
            str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");
            // 清除掉特殊字符
            String regEx = "[『』]";
            Pattern p = compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            return str;
        }
    }


    /**
     * 获取两个相同字符串中间的字符
     *
     * @param data 数据
     * @param k    相同字符串
     * @return
     */
    public static String getAmongString(String data, String k) {
        int a = data.indexOf(k);
        data = data.substring(a + 1, data.length());
        int b = data.indexOf(k);
        data = data.substring(0, b);
        return data;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 比较两个字符串是否相等，无序比较
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean compareDisorder(String s1, String s2) {
        //String[] s2 = "321ACB654321".split(";");
        boolean flag = true;
        for (char c : s1.toCharArray()) {
            if (!s2.contains(String.valueOf(c))) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    public static String getEncoding(String str) {
        String encode = "gbk";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是gbk
                return encode; //是的话，返回“gbk“
            }
        } catch (Exception exception) {
        }
        encode = "utf-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是utf-8
                return encode; //是的话，返回“utf-8“
            }
        } catch (Exception exception2) {
        }
        return ""; //非约定的编码格式。
    }


    /**
     * 格式化json字符串
     *
     * @param text
     * @return
     */
    public static String formatJsonString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

    /**
     * 检测是否为乱码
     *
     * @param strName
     * @return
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查字符是否为中文
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 中文转换编码
     *
     * @param msg
     * @return
     */
    public static String toChinese(String msg) {
        if (isMessyCode(msg)) {
            try {
                return new String(msg.getBytes("ISO8859-1"), "UTF-8");
            } catch (Exception e) {
            }
        }
        return msg;
    }

    /**
     * 手机号脱敏处理
     *
     * @param phone
     * @return
     */
    public static String phoneEncyp(String phone) {
        if (!NullUtil.notEmpty(phone)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(phone);
        stringBuilder.replace(3, phone.length() - 4, "****");
        return stringBuilder.toString();
    }


    /**
     * Return the string value associated with a particular resource ID.
     *
     * @param id The desired resource identifier.
     * @return the string value associated with a particular resource ID.
     */
    public static String getString(@StringRes int id) {
        return getString(id, (Object[]) null);
    }

    /**
     * Return the string value associated with a particular resource ID.
     *
     * @param id         The desired resource identifier.
     * @param formatArgs The format arguments that will be used for substitution.
     * @return the string value associated with a particular resource ID.
     */
    public static String getString(@StringRes int id, Object... formatArgs) {
        try {
            return format(Utils.getApp().getString(id), formatArgs);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return String.valueOf(id);
        }
    }

    /**
     * Return the string array associated with a particular resource ID.
     *
     * @param id The desired resource identifier.
     * @return The string array associated with the resource.
     */
    public static String[] getStringArray(@ArrayRes int id) {
        try {
            return Utils.getApp().getResources().getStringArray(id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return new String[]{String.valueOf(id)};
        }
    }

    /**
     * Format the string.
     *
     * @param str  The string.
     * @param args The args.
     * @return a formatted string.
     */
    public static String format(@Nullable String str, Object... args) {
        String text = str;
        if (text != null) {
            if (args != null && args.length > 0) {
                try {
                    text = String.format(str, args);
                } catch (IllegalFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

}
