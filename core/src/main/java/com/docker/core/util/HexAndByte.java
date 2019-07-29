package com.docker.core.util;

/**
 * Created by zhangshaofang on 2017/8/15.
 */
public class HexAndByte {
    public static String byte2String(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            sb.append(codes[((b & 0xF0) >>> 4)]);
            sb.append(codes[(b & 0xF)]);
        }
        return sb.toString();
    }

    public static byte[] string2Byte(String str) {
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < result.length; i++) {
            byte b = (byte) ((getStrIndex(str.charAt(2 * i)) & 0xF) << 4 | getStrIndex(str.charAt(2 * i + 1)) & 0xF);
            result[i] = b;
        }
        return result;
    }

    public static int getStrIndex(char c) {
        if (c > '9') {
            return 10 + (c - 'a');
        }
        return c - '0';
    }

    private static char[] codes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
