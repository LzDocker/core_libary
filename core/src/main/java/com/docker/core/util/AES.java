package com.docker.core.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhangshaofang on 2017/8/15.
 */

public class AES {
    public static String KEY_SERSION = "android-v1.0.0";
    public static String KEY = "2c8c69d98acf43759fe1335f0b566a39";
    private Cipher cipher_encrypt;
    private Cipher cipher_decrypt;

    private AES(String key) {
        initEncryptCipher(key);
        initDecryptCipher(key);
    }

    public static AES getInstance() {
        return new AES(KEY);
    }

    private void initEncryptCipher(String key) {
        try {
            cipher_encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(HexAndByte.string2Byte(key), "AES");
            cipher_encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDecryptCipher(String key) {
        try {
            cipher_decrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(HexAndByte.string2Byte(key), "AES");
            cipher_decrypt.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] bytes) {
        try {
            byte[] result = cipher_encrypt.doFinal(bytes);
            return result;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] bytes) {
        try {
            byte[] result = cipher_decrypt.doFinal(bytes);
            return result;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
