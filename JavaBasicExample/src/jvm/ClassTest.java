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
package jvm;

import java.lang.reflect.Field;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 14:09
 */
public class ClassTest {
	// 数组类型有一个魔法属性：length来获取数组长度
	private static int[] array = new int[3];
	private static int length = array.length;

	// 任何小写class定义的类，也有一个魔法属性：class，来获取此类的大写Class类对象
	private static Class<One> one = One.class;
	private static Class<Another> another = Another.class;

	public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
		// 通过newInstance方法来创建One和Another的类对象
		One oneObject = one.newInstance();
		oneObject.call();

		Another anotherObject = another.newInstance();
		anotherObject.speak();

		// 通过one这个大写的Class对象，获取私有成员属性对象Field
		Field privateFieldInOne = one.getDeclaredField("inner");

		// 设置私有对象可以访问和修改
		privateFieldInOne.setAccessible(true);

		privateFieldInOne.set(oneObject, "world changed");

		// 成功修改类的私有属性inner变量
		System.out.println(oneObject.getInner());
	}
}

class One{
	private String inner = "time files.";
	public void call(){
		System.out.println("hello world.");
	}
	public String getInner(){
		return inner;
	}
}

class Another{
	public void speak(){
		System.out.println("easy coding.");
	}
}