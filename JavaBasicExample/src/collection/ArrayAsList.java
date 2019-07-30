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

import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 16:21
 */
public class ArrayAsList {
	public static void main(String[] args) {
		String[] stringArray = new String[3];
		stringArray[0] = "one";
		stringArray[1] = "two";
		stringArray[2] = "three";

		// 利用Arrays.asList来进行操作字符串的话，可以修改已存在的内容，但是不能进行元素个数的任何操作
		// 会出现运行时异常(UnsupportedOperationException异常)
		List<String> stringList = Arrays.asList(stringArray);

		// 修改已有数据的值，可以成功
		stringList.set(0, "oneList");
		System.out.println(stringArray[0]);

		// 以下是重点，以下编译正确，但是会抛出运行时异常
		// 由于asList的返回对象时一个Arrays内部类，其没有实现集合个数的相关修改方法！！！！！
		stringList.add("four");
		stringList.remove(2);
		stringList.clear();
	}
}