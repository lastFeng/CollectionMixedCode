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
import com.springboot.o2o.dto.ShopExcution;
import com.springboot.o2o.entity.Area;
import com.springboot.o2o.entity.PersonInfo;
import com.springboot.o2o.entity.Shop;
import com.springboot.o2o.entity.ShopCategory;
import com.springboot.o2o.enums.ShopStateEnum;
import com.springboot.o2o.service.AreaService;
import com.springboot.o2o.service.ShopCategoryService;
import com.springboot.o2o.service.ShopService;
import com.springboot.o2o.util.CodeUtil;
import com.springboot.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springboot.o2o.dto.Result.errorModelMap;

;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 13:26
 *
 *
 * 相关外键，是通过session来进行获取的，而不是通过自己来设置，这样会出现严重的问题
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @GetMapping("/getshopmanagementinfo")
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        // request中没有该属性，从session中获取
        if (shopId <= 0) {
            Object currentObj = request.getSession().getAttribute("currentShop");
            // 均没有，重定向
            if (currentObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shop/shoplist");
            } else {
                Shop currentShop = (Shop) currentObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }

        return modelMap;
    }

    @PostMapping("/registershop")
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            errorModelMap(modelMap, false, "输入了错误的验证码");
            return modelMap;
        }

        // 接收信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;

        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            errorModelMap(modelMap, false, e.getMessage());
        }

        MultipartFile shopImg = null;
        MultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (MultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            errorModelMap(modelMap, false, "上传图片不能为空");
            return modelMap;
        }
        // 注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExcution shopExcution = shopService.addShop(shop, shopImg);

            if (shopExcution.getState() == ShopStateEnum.CHECK.getState()) {
                modelMap.put("success", true);
            } else {
                errorModelMap(modelMap, false, shopExcution.getStateInfo());
            }

        } else {
            errorModelMap(modelMap, false, "请输入店铺信息");
        }
        // 返回结果
        return modelMap;
    }

    @GetMapping("/getshopinitinfo")
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();

        try {
            // 店铺都存储在二级类别之下
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("success", true);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            errorModelMap(modelMap, false, e.getMessage());
        }
        return modelMap;
    }

    @GetMapping("/getshopbyid")
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");

        if (shopId > 0) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                errorModelMap(modelMap, false, e.getMessage());
            }
        } else {
            errorModelMap(modelMap, false, "Empty ShopId");
        }
        return modelMap;
    }

    @PostMapping("/modifyshop")
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 检测验证码

        // 接收修改的信息，以及修改的图片
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;

        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            errorModelMap(modelMap, false, e.getMessage());
        }
        CommonsMultipartResolver com = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartFile file = null;

        if (com.isMultipart(request)) {
            MultipartHttpServletRequest httpServletRequest = (MultipartHttpServletRequest) request;
            file = (MultipartFile) httpServletRequest.getFile("shopImg");
        }

        // 修改店铺
        if (shop != null && shop.getShopId() != null) {
            // 利用session来进行操作
            // Session TODO
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExcution excution = shopService.modifyShop(shop, file);

            if (excution.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                errorModelMap(modelMap, false, excution.getStateInfo());
            }
        }
        return modelMap;
    }

    /**
     * 获取商铺列表
     */
    @GetMapping("/getshoplist")
    @ResponseBody
    public Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExcution shopExcution = shopService.getShopList(shopCondition, 0, 10);
            modelMap.put("success", true);
            modelMap.put("shopList", shopExcution.getShopList());
            modelMap.put("count", shopExcution.getCount());
            modelMap.put("user", user);
        } catch (Exception e) {
            errorModelMap(modelMap, false, e.getMessage());
        }

        return modelMap;
    }
}