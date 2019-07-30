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
package com.springboot.o2o.controller;

import com.springboot.o2o.dto.ProductCategoryExecution;
import com.springboot.o2o.entity.ProductCategory;
import com.springboot.o2o.entity.Shop;
import com.springboot.o2o.enums.ProductCategoryStateEnum;
import com.springboot.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.o2o.dto.Result.errorModelMap;
import static com.springboot.o2o.dto.Result.successModelMap;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/9 9:39
 */
@Controller
@RequestMapping("/productcategory")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/getproductcategorylist")
    @ResponseBody
    public Map<String, Object> getProductCategoryList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        try {
            productCategoryList = productCategoryService.getProductCategoryList();
            successModelMap(modelMap, true, productCategoryList);
        } catch (Exception e) {
            errorModelMap(modelMap, false, e.getMessage());
        }

        return modelMap;
    }

    @GetMapping("/getproductcategorybyid")
    @ResponseBody
    public Map<String, Object> getProductCategoryById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long productCategoryId = (Long) request.getSession().getAttribute("prodcateId");
        ProductCategory productCategory = new ProductCategory();

        if (productCategoryId <= 0) {
            errorModelMap(modelMap, false, "输入的店铺类别Id不正确");
            return modelMap;
        } else {
            try {
                productCategory = productCategoryService.getProductCategoryById(productCategoryId);
                successModelMap(modelMap, true, productCategory);
            } catch (Exception e) {
                errorModelMap(modelMap, false, e.getMessage());
            }
        }
        return modelMap;
    }

    @PostMapping("/batchaddproductcategorys")
    @ResponseBody
    public Map<String, Object> batchAddProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
                                                        HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 从当前登录的shop中设置shopid
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }

        // 空值判断
        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution execution = productCategoryService.batchAddProductCategory(productCategoryList);
                if (execution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    successModelMap(modelMap, true, "");
                } else {
                    errorModelMap(modelMap, false, execution.getStateInfo());
                }
            } catch (Exception e) {
                errorModelMap(modelMap, false, e.getMessage());
            }
        }

        return modelMap;
    }

    @PostMapping("/deleteproductcategory")
    @ResponseBody
    public Map<String, Object> deleteProductCategory(@RequestBody Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            ProductCategoryExecution execution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());

            if (execution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                successModelMap(modelMap, true, "");
            } else {
                errorModelMap(modelMap, false, execution.getStateInfo());
            }
        } else {
            errorModelMap(modelMap, false, "请选择一个商品类别");
        }

        return modelMap;
    }
}