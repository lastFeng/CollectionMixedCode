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
package com.springboot.o2o.dao;

import com.springboot.o2o.basetest.BaseTest;
import com.springboot.o2o.entity.ProductImg;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 13:50
 */
public class ProductImgDaoTest extends BaseTest {
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testBatchInsertProductImg() {
        List<ProductImg> list = new ArrayList<ProductImg>();
        ProductImg productImg = new ProductImg();
        productImg.setImgAddr("咖啡图");
        productImg.setImgDesc("咖啡店图片");
        productImg.setPriority(1);
        productImg.setCreateTime(new Date());
        productImg.setLastEditTime(new Date());
        productImg.setProductId(1L);
        list.add(productImg);

        ProductImg img = new ProductImg();
        img.setImgAddr("水果图");
        img.setImgDesc("水果店图片");
        img.setPriority(2);
        img.setCreateTime(new Date());
        img.setLastEditTime(new Date());
        img.setProductId(2L);
        list.add(img);

        int effectedNum = productImgDao.batchInsertProductImg(list);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testQueryProductImgList() {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(1L);
        assertEquals(1, productImgList.size());
        assertEquals("咖啡图", productImgList.get(0).getImgAddr());
    }
}