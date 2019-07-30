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
package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 16:30
 */
public class ListToArray {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("one");
		list.add("two");
		list.add("three");

		// 泛型丢失，无法使用String[]接收无参方法返回的结果
		Object[] array = list.toArray();
		System.out.println(array);

		// array2数组长度小于元素个数
		String[] array2 = new String[2];
		list.toArray(array2);
		System.out.println(Arrays.asList(array2));

		// array3 数组长度大于等于元素个数
		String[] array3 = new String[4];
		list.toArray(array3);
		System.out.println(Arrays.asList(array3));
	}
}