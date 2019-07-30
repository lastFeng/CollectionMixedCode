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

import com.springboot.o2o.dao.ProductDao;
import com.springboot.o2o.dao.ProductImgDao;
import com.springboot.o2o.dto.ProductExcution;
import com.springboot.o2o.entity.Product;
import com.springboot.o2o.entity.ProductImg;
import com.springboot.o2o.enums.ProductStateEnum;
import com.springboot.o2o.exception.ProductOperationException;
import com.springboot.o2o.util.ImageUtil;
import com.springboot.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 14:21
 */
@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    /**
     * @param product        要添加的商品
     * @param img            缩略图文件
     * @param productImgList 商品详情图片文件列表
     * @return 操作结果
     */
    @Transactional
    public ProductExcution addProduct(Product product, MultipartFile img,

                                      List<MultipartFile> productImgList) throws ProductOperationException {
        if (img.isEmpty() || productImgList.size() == 0 || productImgList == null) {
            return new ProductExcution(ProductStateEnum.NOT_IMG);
        }
        // 1. 缩略图处理
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String imgAddr = ImageUtil.generateThumbnail(img, dest);
        product.setImgAddr(imgAddr);

        // 商品初始值设置
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        // 新增操作
        try {
            // 3. 新增商品
            int effectNum = productDao.insertProduct(product);
            if (effectNum < 1) {
                throw new ProductOperationException("添加商品失败");
            }
            // 2. 批量增加商品图片列表
            List<ProductImg> productImgs = new ArrayList<ProductImg>();
            for (MultipartFile file : productImgList) {
                String productImgAddr = ImageUtil.generateThumbnail(file, dest);
                ProductImg productImg = new ProductImg();
                productImg.setImgAddr(productImgAddr);
                productImg.setPriority(product.getPriority());
                productImg.setCreateTime(new Date());
                productImg.setLastEditTime(new Date());
                productImg.setProductId(product.getProductId());
                productImgs.add(productImg);
            }

            effectNum = productImgDao.batchInsertProductImg(productImgs);
            if (effectNum < 1) {
                throw new ProductOperationException("添加商品图片失败");
            }

            return new ProductExcution(ProductStateEnum.SUCCESS);
        } catch (Exception e) {
            throw new ProductOperationException(e.getMessage());
        }
    }
}