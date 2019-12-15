package com.zscat.mallplus.portal.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * @Date: 2019/12/15
 * @Description
 */
public class WechatDecryptDataUtil {

    public static void main(String[] args) {
        String result = decryptData(
                "rs4MdCtL+a2So93xkz+y6vFgSy9Xj//Gpe+IrsFJVdGgk02/2IgOQ/3A3QywCJvdxXNiZxdPe+wPLvxJsMZ8WlWktzV6Ay2Pkza8INSUkKAIldq7dAkoxgHt5yqacuMuXTUBYQYOlE/sFBdNacItYTd4lTJQcechGubxqwqUL+QIOjqOoPthJ7Mi5U7cKWo1SJdEAvKoKFmTqKR3Vccosw==",
                "YYg1kUeIHdgILxtLGh49NA==",
                "DLmLdMmC8WmKudgj4q6m7A=="
        );

        System.out.println("result = " + JSONObject.parseObject(result).get("phoneNumber"));
    }

    public static String getPhoneNo(String encryptDataB64, String sessionKeyB64, String ivB64){
        String result = decryptData(encryptDataB64,  sessionKeyB64,  ivB64);
        return  JSONObject.parseObject(result).get("phoneNumber") != null ? JSONObject.parseObject(result).get("phoneNumber").toString():null;
    }

    public static String decryptData(String encryptDataB64, String sessionKeyB64, String ivB64) {
        return new String(
                decryptOfDiyIV(
                        Base64.decode(encryptDataB64),
                        Base64.decode(sessionKeyB64),
                        Base64.decode(ivB64)
                )
        );
    }

    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;

    private static void init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @param ivs           自定义对称解密算法初始向量 iv
     * @return 解密后的字节数组
     */
    private static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }
}
