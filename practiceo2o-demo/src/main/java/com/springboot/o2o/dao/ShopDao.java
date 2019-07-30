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

import com.springboot.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: G.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 9:19
 */
public interface ShopDao {

    /**
     * 新增店铺
     *
     * @param shop
     * @return 返回店铺id
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     */
    int updateShop(Shop shop);

    /**
     * 通过店铺ID获取店铺信息
     */
    Shop queryByShopId(Long shopId);

    /**
     * 分页查询店铺
     * 店铺名（模糊查询）、店铺状态、店铺类别、区域id， owner
     *
     * @param shopCondition
     * @param rowIndex      第几行开始取
     * @param pageSize      返回的条数
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * @param shopCondition 返回查询条件下的所有条数
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
