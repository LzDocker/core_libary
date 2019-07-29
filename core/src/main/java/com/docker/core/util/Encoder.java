package com.docker.core.util;

import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 编码器
 *
 * @author SHOUSHEN LUAN
 */
public enum Encoder {
    base64 {// base64编码器

        @Override
        public byte[] encode(byte[] data) {
            return Base64.encode(data, Base64.NO_WRAP);
        }

        @Override
        public byte[] decode(byte[] data) {
            return Base64.decode(data, Base64.NO_WRAP);
        }

    },
    gzip {// 压缩与解压编码器

        @Override
        public byte[] encode(byte[] data) {
            return Gzip.zip(data);
        }

        @Override
        public byte[] decode(byte[] data) {
            return Gzip.unzip(data);
        }
    },
    encrypt {// 加密与解密编码器

        @Override
        public byte[] encode(byte[] data) throws Exception {
            return AES.getInstance().encrypt(data);
        }

        @Override
        public byte[] decode(byte[] data) throws Exception {
            return AES.getInstance().decrypt(data);
        }

        public String getKeyVersion() {
            return AES.KEY_SERSION;
        }
    };

    public boolean matches(String type) {
        return name().equalsIgnoreCase(type);
    }

    public abstract byte[] encode(byte[] data) throws Exception;

    public abstract byte[] decode(byte[] data) throws Exception;

    /**
     * 根据编码list进行编码处理
     *
     * @param data
     * @param list
     * @return
     * @throws Exception
     */
    public static byte[] encode(byte[] data, List<Encoder> list) throws Exception {
        for (Encoder type : list) {
            data = type.encode(data);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public static List<Encoder> parser(List<String> list) {
        if (list != null && list.size() > 0) {
            List<Encoder> result = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                boolean isMatches = false;
                DONE:
                for (Encoder type : values()) {
                    if (type.matches(list.get(i))) {
                        result.add(type);
                        isMatches = true;
                        break DONE;
                    }
                }
                if (!isMatches) {
                    Log.e("忽略不支持的编码类型:`{}`", list.get(i));
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    public static byte[] decode(byte[] data, List<String> list) throws Exception {
        List<Encoder> codings = parser(list);
        for (int i = codings.size() - 1; i >= 0; i--) {
            Encoder type = codings.get(i);
            data = type.decode(data);
        }
        return data;
    }
}