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

import com.springboot.o2o.dao.ShopDao;
import com.springboot.o2o.dto.ShopExcution;
import com.springboot.o2o.entity.Shop;
import com.springboot.o2o.enums.ShopStateEnum;
import com.springboot.o2o.exception.ShopOperationException;
import com.springboot.o2o.util.ImageUtil;
import com.springboot.o2o.util.PageCalculate;
import com.springboot.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 11:19
 */
@Service
public class ShopService {
    @Autowired
    private ShopDao shopDao;

    @Transactional
    public ShopExcution addShop(Shop shop, MultipartFile shopImg) {
        // 非空字段的验证
        if (shop == null || shop.getShopId() == null || shop.getArea() == null
            || shop.getShopCategory() == null || shop.getShopName() == null) {
            return new ShopExcution(ShopStateEnum.NULL_SHOP);
        }

        try {
            // 存储图片，返回图片地址
            addShopImg(shop, shopImg);
            // 1. 设置初始的店铺信息--状态，创建时间，修改时间，权限等
            shop.setLastEditTime(new Date());
            shop.setCreateTime(new Date());
            shop.setEnableStatus(0);

            // 插入店铺
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        // 没有问题，则店铺创建成功，当前处于审核中
        return new ShopExcution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, MultipartFile shopImg) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);

        shop.setShopImg(shopImgAddr);
    }

    public Shop getByShopId(Long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    public ShopExcution modifyShop(Shop shop, MultipartFile shopImg) throws ShopOperationException {

        if (shop == null || shop.getShopId() == null) {
            return new ShopExcution(ShopStateEnum.NULL_SHOP);
        }
        // 1 判断是否需要处理图片
        if (shopImg != null) {
            Shop tempShop = shopDao.queryByShopId(shop.getShopId());
            // 先删除图片
            if (tempShop.getShopImg() != null) {
                ImageUtil.deleteFileOrPath(tempShop.getShopImg());
            }
            // 新增图片 shop的图片信息
            addShopImg(shop, shopImg);
        }
        // 2 更新店铺信息
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        if (effectedNum < 1) {
            return new ShopExcution(ShopStateEnum.INNER_ERROR);
        } else {
            shop = shopDao.queryByShopId(shop.getShopId());
            return new ShopExcution(ShopStateEnum.SUCCESS, shop);
        }
    }

    public ShopExcution getShopList(Shop shopCondition, int pageIndex, int pagesize) {
        int rowIndex = PageCalculate.calculateRowIndex(pageIndex, pagesize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pagesize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExcution shopExcution = new ShopExcution();

        if (shopList != null) {
            shopExcution.setShopList(shopList);
            shopExcution.setCount(count);
        } else {
            shopExcution.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return shopExcution;
    }
}