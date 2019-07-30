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
import com.springboot.o2o.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 14:02
 */
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testInsetProduct() {
        Product product = new Product();
        //product.setProductName("咖啡店");
        //product.setProductDesc("咖啡店的描述");
        //product.setImgAddr("咖啡店的图");
        //product.setNormalPrice("27.00");
        //product.setPromotionPrice("20.00");
        //product.setPriority(2);
        //product.setCreateTime(new Date());
        //product.setLastEditTime(new Date());
        //product.setEnableStatus(1);
        //product.setProductCategory(productCategoryDao.queryProductCategoryById(2L));
        //product.setShop(shopDao.queryByShopId(2L));
        //int effectedNum = productDao.insertProduct(product);
        //assertEquals(1, effectedNum);

        product.setProductName("水果店");
        product.setProductDesc("水果店的描述");
        product.setImgAddr("水果店的图");
        product.setNormalPrice("10.00");
        product.setPromotionPrice("8.00");
        product.setPriority(3);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);
        product.setProductCategory(productCategoryDao.queryProductCategoryById(2L));
        product.setShop(shopDao.queryByShopId(2L));
        int effectedNum = productDao.insertProduct(product);
        assertEquals(1, effectedNum);
    }
}