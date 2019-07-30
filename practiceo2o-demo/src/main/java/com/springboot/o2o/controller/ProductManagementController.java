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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.o2o.dto.ProductExcution;
import com.springboot.o2o.entity.Product;
import com.springboot.o2o.enums.ProductStateEnum;
import com.springboot.o2o.exception.ProductOperationException;
import com.springboot.o2o.service.ProductService;
import com.springboot.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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
 * @create: 2019/5/9 14:52
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;

    private static final int IMAGE_MAX_COUNT = 6;

    @PostMapping("/addproduct")
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 验证码，验证 TODO

        // 接收数据
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "product");

        MultipartHttpServletRequest multipartHttpServletRequest = null;
        MultipartFile productImg = null;
        List<MultipartFile> productImgs = new ArrayList<MultipartFile>(IMAGE_MAX_COUNT);
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            // 存在文件流，取出
            if (resolver.isMultipart(request)) {
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                productImg = multipartHttpServletRequest.getFile("img");
                for (int i = 1; i <= IMAGE_MAX_COUNT; i++) {
                    MultipartFile file = multipartHttpServletRequest.getFile("productImg" + i);
                    if (file != null) {
                        productImgs.add(file);
                    } else {
                        break;
                    }
                }
                if (productImgs == null || productImgs.size() <= 0) {
                    errorModelMap(modelMap, false, "请添加图片");
                    return modelMap;
                }
                // 保存
                ProductExcution productExcution = productService.addProduct(product, productImg, productImgs);
                if (productExcution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    successModelMap(modelMap, true, productExcution);
                } else {
                    errorModelMap(modelMap, false, productExcution.getStateInfo());
                }
            } else {
                errorModelMap(modelMap, false, "请添加图片");
            }
        } catch (Exception e) {
            throw new ProductOperationException(e.getMessage());
        }

        return modelMap;
    }

}