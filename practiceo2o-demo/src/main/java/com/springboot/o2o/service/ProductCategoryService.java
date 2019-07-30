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

import com.springboot.o2o.dao.ProductCategoryDao;
import com.springboot.o2o.dto.ProductCategoryExecution;
import com.springboot.o2o.entity.ProductCategory;
import com.springboot.o2o.enums.ProductCategoryStateEnum;
import com.springboot.o2o.exception.ProductCategoryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 9:35
 */
@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryDao.queryProductCategoryList();
    }

    public ProductCategory getProductCategoryById(Long productCategoryId) {
        return productCategoryDao.queryProductCategoryById(productCategoryId);
    }

    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
        throws ProductCategoryOperationException {

        if (productCategoryList != null && productCategoryList.size() > 0) {
            int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
            if (effectedNum <= 0) {
                throw new ProductCategoryOperationException("店铺类别批量创建失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    /**
     * 商品类别下的商品的类别id赋值为空
     */
    @Transactional
    public ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId)
        throws ProductCategoryOperationException {
        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);

            if (effectedNum != 1) {
                throw new ProductCategoryOperationException("删除店铺类别失败");
            }
            // 商品类别下的商品的类别id赋值为空 TODO

        } catch (Exception e) {
            new ProductCategoryOperationException(e.getMessage());
        }
        return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
    }
}