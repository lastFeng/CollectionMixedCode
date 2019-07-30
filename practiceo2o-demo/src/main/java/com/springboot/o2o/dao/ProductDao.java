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

import com.springboot.o2o.entity.Product;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: G.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 13:25
 */
public interface ProductDao {
    /**
     * 新增商品
     */
    int insertProduct(Product product);

    /**
     * 通过商品ID查询商品
     */
    List<Product> queryProductById(Long productId);

    /**
     * 修改商品信息
     */
    int updateProduct(Product product);

    /**
     * 删除商品
     */
    int deleteProductByProductId(Long productId);
}
