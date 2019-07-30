/*
 * Copyright 2001-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springboot.o2o.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/13 9:04
 * 使用DES加密
 */
public class DesUtil {
    private static Key key;
    private static String KEY_STR = "myKey";
    private static String ALGORITHM = "DES";
    private static String CHARSETNAME = "UTF-8";

    static {
        try {
            // 生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // 运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");

            // 设置上密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());

            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成密钥对象
            key = generator.generateKey();
            generator = null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密信息
     */
    public static String getEncryptString(String str) {
        // 基于BASE64进行加密
        BASE64Encoder base64Encoder = new BASE64Encoder();

        try {
            // 按UTF8编码
            byte[] bytes = str.getBytes(CHARSETNAME);

            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            // 返回
            return base64Encoder.encode(doFinal);

        } catch (Exception e) {
            // TODO handle exception
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取解密信息
     */
    public static String getDecryptString(String str) {
        // 基于Base64进行解密
        BASE64Decoder base64Decoder = new BASE64Decoder();

        try {
            // 将字符串decode成byte
            byte[] bytes = base64Decoder.decodeBuffer(str);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] doFinal = cipher.doFinal(bytes);

            return new String(doFinal, CHARSETNAME);

        } catch (Exception e) {
            // TODO handle exception
            throw new RuntimeException(e);
        }
    }

    /**
     * 进行加密
     */
    public static void main(String[] args) {
        System.out.println(getEncryptString("wsp"));
        System.out.println(getEncryptString("Abcd123456"));
    }
}