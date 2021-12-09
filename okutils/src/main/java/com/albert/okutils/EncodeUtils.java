package com.albert.okutils;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <pre>
 *      copyright    : Copyright (c) 2018.
 *      author       : jiaoya.
 *      created Time : 2018/8/9.
 *      desc         : 编码解码相关
 * </pre>
 */
public class EncodeUtils {

    private EncodeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static int BASE64_TYPE = Base64.NO_WRAP;

    public static void setBase64Type(int base64Type) {
        BASE64_TYPE = base64Type;
    }

    /**
     * URL 编码(UTF-8)
     *
     * @param input The input.
     * @return the urlencoded string
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, UTF8);
    }

    /**
     * URL 编码
     *
     * @param input       The input.
     * @param charsetName 编码型号-列如：UTF-8
     * @return the urlencoded string
     */
    public static String urlEncode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            return URLEncoder.encode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * URL 解码(UTF-8)
     *
     * @param input The input.
     * @return the string of decode urlencoded string
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * URL 解码
     *
     * @param input       The input.
     * @param charsetName 编码型号-列如：UTF-8
     * @return the string of decode urlencoded string
     */
    public static String urlDecode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            return URLDecoder.decode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Base64 编码
     *
     * @param input The input.字符串
     * @return Base64-encode bytes
     */
    public static byte[] base64Encode(final String input) {
        return base64Encode(input.getBytes());
    }

    /**
     * Base64 编码
     *
     * @param input The input.字节
     * @return Base64-encode bytes
     */
    public static byte[] base64Encode(final byte[] input) {
        if (input == null || input.length == 0) return new byte[0];
        return base64Encode2Byte(input, BASE64_TYPE);
    }

    /**
     * Base64 编码
     *
     * @param input The input.
     * @return Base64-encode string
     */
    public static String base64Encode2String(final byte[] input) {
        if (input == null || input.length == 0) return "";
        return Base64.encodeToString(input, BASE64_TYPE);
    }

    /**
     * Base64 编码
     *
     * @param input The input.
     * @return Base64-encode string
     */
    public static String base64Encode2String(final byte[] input, int type) {
        if (input == null || input.length == 0) return "";
        return Base64.encodeToString(input, type);
    }

    /**
     * Base64 编码
     *
     * @param input
     * @param type  编码类型
     * @return
     */
    public static String base64Encode2String(final String input, int type) {
        if (NullUtil.notEmpty(input)) {
            return "";
        }
        return Base64.encodeToString(input.getBytes(), type);
    }

    /**
     * Base64 编码
     *
     * @param input
     * @param type  编码类型
     * @return
     */
    public static byte[] base64Encode2Byte(final byte[] input, int type) {
        if (input == null || input.length == 0) return new byte[0];
        return Base64.encode(input, type);
    }


    /**
     * Base64 解码
     *
     * @param input The input.
     * @return the string of decode Base64-encode string
     */
    public static byte[] base64Decode(final String input) {
        if (input == null || input.length() == 0) return new byte[0];
        return base64Decode(input, BASE64_TYPE);
    }

    /**
     * Base64 解码
     *
     * @param input The input.
     * @return the bytes of decode Base64-encode bytes
     */
    public static byte[] base64Decode(final byte[] input) {
        if (input == null || input.length == 0) return new byte[0];
        return base64Decode(input, BASE64_TYPE);
    }

    /**
     * Base64 解码
     *
     * @param input
     * @param type  解码类型
     * @return
     */
    public static byte[] base64Decode(final byte[] input, int type) {
        if (input == null || input.length == 0) return new byte[0];
        return Base64.decode(input, type);
    }

    /**
     * Base64 解码
     *
     * @param input
     * @param type  解码类型
     * @return
     */
    public static byte[] base64Decode(final String input, int type) {
        if (input == null || input.length() == 0) return new byte[0];
        return Base64.decode(input, type);
    }

    /**
     * Base64 解码
     *
     * @param input
     * @param type
     * @return
     */
    public static String base64Decode2String(final byte[] input, int type) {
        if (input == null || input.length == 0) {
            return "";
        }
        return new String(Base64.decode(input, type));
    }

    /**
     * Base64 解码
     *
     * @param input
     * @return
     */
    public static String base64Decode2String(final byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        return new String(Base64.decode(input, BASE64_TYPE));
    }

    /**
     * Base64 解码
     *
     * @param input
     * @return
     */
    public static String base64Decode2String(final String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        return new String(Base64.decode(input, BASE64_TYPE));
    }

    /**
     * Base64 解码
     *
     * @param input
     * @param type
     * @return
     */
    public static String base64Decode2String(final String input, int type) {
        if (input == null || input.length() == 0) {
            return "";
        }
        return new String(Base64.decode(input, type));
    }

    /**
     * Html 编码
     *
     * @param input The input.
     * @return html-encode string
     */
    public static String htmlEncode(final CharSequence input) {
        if (input == null || input.length() == 0) return "";
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0, len = input.length(); i < len; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;"); //$NON-NLS-1$
                    break;
                case '>':
                    sb.append("&gt;"); //$NON-NLS-1$
                    break;
                case '&':
                    sb.append("&amp;"); //$NON-NLS-1$
                    break;
                case '\'':
                    //http://www.w3.org/TR/xhtml1
                    // The named character reference &apos; (the apostrophe, U+0027) was
                    // introduced in XML 1.0 but does not appear in HTML. Authors should
                    // therefore use &#39; instead of &apos; to work as expected in HTML 4
                    // user agents.
                    sb.append("&#39;"); //$NON-NLS-1$
                    break;
                case '"':
                    sb.append("&quot;"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Html 解码
     *
     * @param input The input.
     * @return the string of decode html-encode string
     */
    public static CharSequence htmlDecode(final String input) {
        if (input == null || input.length() == 0) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }

    /**
     * 二进制编码
     *
     * @param input The input.
     * @return binary string
     */
    public static String binaryEncode(final String input) {
        if (input == null || input.length() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (char i : input.toCharArray()) {
            sb.append(Integer.toBinaryString(i)).append(" ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 二进制解码
     *
     * @param input binary string
     * @return UTF-8 String
     */
    public static String binaryDecode(final String input) {
        if (input == null || input.length() == 0) return "";
        String[] splits = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String split : splits) {
            sb.append(((char) Integer.parseInt(split, 2)));
        }
        return sb.toString();
    }
}
