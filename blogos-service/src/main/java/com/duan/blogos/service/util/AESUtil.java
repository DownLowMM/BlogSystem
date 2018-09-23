package com.duan.blogos.service.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created on 2018/5/30.
 *
 * @author DuanJiaNing
 */
public class AESUtil {

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            random.setSeed(20);
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException |
                IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            random.setSeed(20);
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();

            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化

            byte[] result = cipher.doFinal(content);
            return result; // 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }

        return result;
    }

    /**
     * 另外一种加密方式--这种加密方式有两种限制
     * 1、密钥必须是16位的
     * 2、待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
     * at com.sun.crypto.provider.SunJCE_f.a(DashoA13*..)
     * at com.sun.crypto.provider.SunJCE_f.b(DashoA13*..)
     * at com.sun.crypto.provider.SunJCE_f.b(DashoA13*..)
     * at com.sun.crypto.provider.AESCipher.engineDoFinal(DashoA13*..)
     * at javax.crypto.Cipher.doFinal(DashoA13*..)
     * 要解决如上异常，可以通过补全传入加密内容等方式进行避免。
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     */
    public static byte[] encrypt2(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String aesEncrypt(String s, String key) {
        return parseByte2HexStr(encrypt(s, key));
    }

    public static String aesDecrypt(String token, String key) {
        byte[] decryptFrom = parseHexStr2Byte(token);
        return new String(decrypt(decryptFrom, key));// 解码
    }
}
