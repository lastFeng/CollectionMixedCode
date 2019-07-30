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
package com.springboot.springswagger2.controller;

import com.springboot.springswagger2.domain.Book;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/30 9:14
 */

/**
 * 用户创建某本图书	POST	/books/
 * 用户修改对某本图书	PUT	/books/:id/
 * 用户删除对某本图书	DELETE	/books/:id/
 * 用户获取所有的图书 GET /books
 * 用户获取某一图书  GET /Books/:id
 * 官方文档：http://swagger.io/docs/specification/api-host-and-base-path/
 */
@RestController
@RequestMapping("/books")
public class SwaggerController {
    Map<Long, Book> books = Collections.synchronizedMap(new HashMap<>());

    @ApiOperation(value = "获取图书列表", notes = "获取图书列表")
    @GetMapping("/")
    public List<Book> getBook() {
        List<Book> book = new ArrayList<>(books.values());
        return book;
    }

    @ApiOperation(value = "创建图书", notes = "创建图书")
    @ApiImplicitParam(name = "book", value = "图书详细实体", required = true, dataType = "Book")
    @PostMapping("/")
    public String postBook(@RequestBody Book book) {
        books.put(book.getId(), book);
        return "success";
    }

    @ApiOperation(value = "获取图书详细信息", notes = "根据url的id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return books.get(id);
    }

    @ApiOperation(value = "更新信息", notes = "根据URL的id来更新指定图书信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "图书ID", required = true, dataType = "Long", paramType = "path"),
        @ApiImplicitParam(name = "book", value = "图书实体", required = true, dataType = "Book")
    })
    @PutMapping("/{id}")
    public String putBook(@PathVariable Long id, @RequestBody Book book) {
        Book old = books.get(id);
        if (old != null) {
            old.setName(book.getName());
            old.setPrice(book.getPrice());
            books.put(id, old);
            return "success";
        }
        return "Input book id is not exists!";
    }

    @ApiOperation(value = "删除图书", notes = "根据URL的id来指定删除的图书")
    @ApiImplicitParam(name = "id", value = "图书ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        books.remove(id);
        return "success";
    }

    // 使用该注解忽略这个API
    @ApiIgnore
    @GetMapping("/hi")
    public String jsonTest() {
        return "{'name': 'jsonTest', 'status': 'success'}";
    }
}