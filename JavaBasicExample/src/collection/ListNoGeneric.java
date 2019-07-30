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
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 16:36
 */
public class ListNoGeneric {
	public static void main(String[] args) {
		// 泛型出现之前的集合定义方式
		List a1 = new ArrayList();
		a1.add(new Object());
		a1.add(new Integer(111));
		a1.add(new String("Hello World!"));

		// 把a1引用赋值给a2，注意a2与a1的区别是增加了泛型限制
		List<Object> a2 = a1;
		a2.add(new Object());
		a2.add(new Integer(111));
		a2.add(new String("Hello a2"));

		// 把a1引用赋值给a3，注意a3与a1的区别是增加了泛型<Integer>
		List<Integer> a3 = a1;
		a3.add(new Integer(333));
		// 下方两行编译出错
		//a3.add(new Object());
		//a3.add(new String("hello"));

		// 把a1引用赋值给a4，a1与a4的区别是增加了通配符
		List<?> a4 = a1;
		a4.remove(0);
		a4.clear();
		// 编译出错，不允许增加任何元素
		//a4.add(new Object());
	}
}