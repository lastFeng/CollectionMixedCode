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
import com.springboot.o2o.entity.ProductCategory;
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
 * @create: 2019/5/9 9:18
 */
public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testQueryProductCategoryList() {
        List<ProductCategory> shopCategoryList = productCategoryDao.queryProductCategoryList();
        assertEquals(2, shopCategoryList.size());
    }

    @Test
    public void testQueryProductCategoryById() {
        Long shopCategoryId = 2L;
        ProductCategory productCategory = productCategoryDao.queryProductCategoryById(shopCategoryId);

        assertEquals("咖啡", productCategory.getProductCategoryName());
    }

    @Test
    public void testBatchInsertProductCategory() {
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setShopId(1L);
        productCategory.setProductCategoryName("水果");
        productCategory.setCreateTime(new Date());
        productCategory.setLastEditTime(new Date());
        productCategory.setPriority(3);

        productCategoryList.add(productCategory);

        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setShopId(1L);
        productCategory1.setProductCategoryName("饮料");
        productCategory1.setCreateTime(new Date());
        productCategory1.setLastEditTime(new Date());
        productCategory1.setPriority(2);
        productCategoryList.add(productCategory1);

        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2, effectedNum);
    }

    @Test
    public void testDeleteProductCategory() {
        Long productCategoryId = 8L;
        Long shopId = 1L;

        int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
        assertEquals(1, effectedNum);
    }
}