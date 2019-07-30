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

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 10:30
 */
public class PathUtil {

    private static String SEPARATOR = System.getProperty("file.separator");

    /**
     * 获取图片的基础路径
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath;

        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/projectdev/image/";
        } else {
            basePath = "/home/xxx/image/";
        }
        basePath = basePath.replace("/", SEPARATOR);
        return basePath;
    }

    /**
     * 获取商铺图片地址
     *
     * @param shopId
     * @return
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "upload/items/shop/" + shopId + "/";
        imagePath = imagePath.replace("/", SEPARATOR);
        return imagePath;
    }
}