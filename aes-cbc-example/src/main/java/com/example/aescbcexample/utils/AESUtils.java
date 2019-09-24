package com.example.aescbcexample.utils;

/**
 * @author guowf
 * @mail guowf_buaa@163.com
 * @description:
 * @data created in 2019-09-04 20:25
 */
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Arrays;
public class AESUtils {

    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final String AES_KEY = "aes_key_16_bytes";
    private static final String AES_MODE = "AES/CBC/PKCS5PADDING";

    public static void main(String[] args) throws Exception {

//        List<String> list = new ArrayList<>();
//        list.add("hello");
//
//        LData lData = new LData();
//        lData.setStatus("ok");
//        lData.setList(list);
//
//        String jsonString = JSON.toJSONString(lData);
////        System.out.println(xxx);
//
//        //String base64Cipher = encrypted(jsonString);

        //String base64Cipher = "Y7dJKaXGgrOAAJjiFxqQv8YcQ8ntcnivQwGxOCS88KXg0drfOgbZ1OKnOINDcQA7";
        String base64Cipher = "1hxri0ehkQ/bQn2a89P3KaGBLk/cX4fq37OqMQLeXsBdweWaMespXi3zpaePmBf0";
        String result = decrypted(base64Cipher);
        System.out.println(result);

        String encrypted = encrypted(result);
        System.out.println(encrypted);

        System.out.println(StringUtils.equals(base64Cipher, encrypted));
    }

    public static String encrypted(String target) throws Exception{
        byte[] keyBytes = AES_KEY.getBytes(ASCII);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(AES_MODE);

        byte[] iv = AES_KEY.getBytes(ASCII);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        target = AES_KEY + target;

        byte[] decrypted = cipher.doFinal(target.getBytes());

        return Base64.encodeBase64String(decrypted);
    }

    public static String decrypted(String base64Cipher) throws Exception {
        byte[] cipherBytes = Base64.decodeBase64(base64Cipher);
        byte[] iv = AES_KEY.getBytes(ASCII);
        byte[] keyBytes = AES_KEY.getBytes(ASCII);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] result = cipher.doFinal(cipherBytes);
        String plainText = new String(Arrays.copyOfRange(result, 16, result.length));
        //System.out.println(plainText);
        return plainText;
    }

    public static <T> T decrypted(String base64Cipher, Class<T> clazz) throws Exception {
        String plainText = decrypted(base64Cipher);

        return JSON.parseObject(plainText, clazz);
    }

}
