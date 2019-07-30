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
package com.springboot.springfileupload.service.impl;

import com.springboot.springfileupload.exception.StorageException;
import com.springboot.springfileupload.exception.StorageFileNotFoundException;
import com.springboot.springfileupload.property.StorageProperties;
import com.springboot.springfileupload.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/30 13:05
 */
@Service
public class FileSystemStorageService implements StorageService {
    /**
     * 文件保存的路径
     */
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * 保存文件
     */
    @Override
    public void store(MultipartFile file) {
        try {
            // 先判断文件是否为空
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            // 通过文件的工具Files，利用拷贝保存
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    /**
     * 加载路径下的一级所有文件文件
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            // 遍历文件夹，统计文件大小
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    /**
     * 通过名称来加载文件
     */
    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * 将文件作为uri资源进行加载
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            // 只是相对于项目的相对路径
            Path file = load(filename);
            // 将文件转化为URL资源，并且是绝对路径
            Resource resource = new UrlResource(file.toUri());
            //System.out.println(file.toString()+ ">>>>>>>>>>>>>>>>>>>>>>>>>1");
            //System.out.println(file.toUri()+ ">>>>>>>>>>>>>>>>>>>>>>>>>2");
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * 递归删除路径下的文件夹包含其所有文件
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * 创建文件夹
     */
    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}