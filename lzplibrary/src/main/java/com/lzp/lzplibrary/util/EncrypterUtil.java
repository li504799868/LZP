package com.lzp.lzplibrary.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by li.zhipeng 2017.07.03
 *
 *      aes加密解密工具类
 */
public class EncrypterUtil {

    /**
     * 密匙
     * */
    private static final String SecurityKey = "iVszGHL32VjTd5Ux";

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";

    public static String AESEncrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
            return new String(Base64.encode(encrypt, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return plainText;
        }
    }

    /**
     * 加密
     * */
    public static String AESEncrypt(String plainText){
        return AESEncrypt(SecurityKey,plainText);
    }

    /**
     * 解密
     * */
    public static String AESDecrypt(String encryptData){
        return AESDecrypt(SecurityKey,encryptData);
    }

    public static String AESDecrypt(String keyStr, String encryptData) {
        byte[] decrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode64 = Base64.decode(encryptData.getBytes("UTF-8"), Base64.DEFAULT);
            decrypt = cipher.doFinal(decode64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decrypt).trim();
    }

    private static Key generateKey(String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}