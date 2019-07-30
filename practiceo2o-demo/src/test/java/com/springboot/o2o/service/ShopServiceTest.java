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
package com.springboot.o2o.service;

import com.springboot.o2o.basetest.BaseTest;
import com.springboot.o2o.dto.ShopExcution;
import com.springboot.o2o.entity.Shop;
import com.springboot.o2o.entity.ShopCategory;
import com.springboot.o2o.exception.ShopOperationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 12:35
 */
public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() {
        Shop shop = new Shop();
        File imgAddr = new File("xxxx");
    }

    @Test
    public void testModifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = shopService.getByShopId(1L);
        assertEquals("测试的店铺", shop.getShopName());

        shop.setShopName("I Love AYZ");

        ShopExcution shopExcution = shopService.modifyShop(shop, null);
        assertEquals(1, shopExcution.getState());
    }

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);

        ShopExcution shopExcution = shopService.getShopList(shopCondition, 0, 5);
        assertEquals(5, shopExcution.getShopList().size());
        assertEquals(29, shopExcution.getCount());
    }
}