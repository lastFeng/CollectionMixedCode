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
package com.learn.java.chapter01;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: L.J
 * @Email: alieismy@gmail.com
 * @version: 1.0
 * @create: 2019/9/25 11:46
 */
public class Learn {
    /**
     * @since 1.8+
     * */
    public static void main(String[] args) {
        Stream<Character> combined = Stream.concat(characterStream("Hello "),
            characterStream("World!"));
        combined.forEach(System.out::print);
    }

    /**
     * @since 1.8+
     * */
    public static Stream<Character> characterStream(String s) {
        List<Character> result = new ArrayList<Character>();
        for (char c : s.toCharArray()) {
            result.add(c);
        }
        return result.stream();
    }
}