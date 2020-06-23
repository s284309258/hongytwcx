package com.platform.util;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES加密128位CBC模式工具类
 */
public class AESUtils {

    //解密密钥(自行随机生成)
    public static final String KEY = "qxhzngy266a186ke";//密钥key
    public static final String IV  = "1ci5crnda6ojzgtr";//向量iv

    //认证密钥(自行随机生成)
    public static final String AK = "s2ip9g3y3bjr5zz7ws6kjgx3ysr82zzw";//AccessKey
    public static final String SK = "uv8zr0uen7aim8m7umcuooqzdv8cbvtf";//SecretKey

    //加密
    public static String Encrypt(String content) throws Exception {
        byte[] raw = KEY.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }

    //解密
    public static String Decrypt(String encryptedData,String WXKEY,String WXIV) throws Exception {
        try {
            // 被加密的数据
            byte[] dataByte = Base64.getDecoder().decode(encryptedData);
            // 加密秘钥
            byte[] keyByte = Base64.getDecoder().decode(WXKEY);
            // 偏移量
            byte[] ivByte = Base64.getDecoder().decode(WXIV);

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

//            byte[] raw = WXKEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encryptedData);
            try {
                byte[] original = cipher.doFinal(dataByte);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }





//            byte[] raw = WXKEY.getBytes("utf-8");
//            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            IvParameterSpec ips = new IvParameterSpec(WXIV.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encryptedData);
//            try {
//                byte[] original = cipher.doFinal(encrypted1);
//                String originalString = new String(original);
//                return originalString;
//            } catch (Exception e) {
//                System.out.println(e.toString());
//                return null;
//            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}