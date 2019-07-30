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
package oob;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 9:18
 * 泛型定义
 */
public class GenericDefinitionDemo<T> {
	/**
	 * 定义说明：
	 * 1. 尖括号里的每个元素都指代一种未知类型。
	 *
	 * @param string  是指的是泛型的名称，而非字符串String类
	 * @param alibaba 泛型的另一个名称
	 *                2. 尖括号的位置非常讲，必须在类名之后或方法放回之前
	 *                3. 泛型在定义处只具备执行Object方法的能力（就只能调用Object中定义的方法，其他方法不能调用）
	 *
	 *                泛型的优势：
	 *                1. 类型安全、2.提升可读性、3.代码重用
	 */
	static <String, T, Alibaba> String get(String string, Alibaba alibaba) {
		return string;
	}

	public static void main(String[] args) {
		Integer first = 222;
		Long second = 333L;
		Integer result = get(first, second);

		System.out.println(result);
	}
}