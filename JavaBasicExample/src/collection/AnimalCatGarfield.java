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

import common.Animal;
import common.Cat;
import common.Garfield;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 16:55
 * 用动物的猫科与加菲猫的集成关系说明extends与super在集合中的意义
 * extends的场景是put功能受限（消费者--消费集合为主），super的场景是get功能受限（生产者--生成集合为主）
 */
public class AnimalCatGarfield {
	public static void main(String[] args) {
		// 声明三个依次集成的类的集合，Object>Animal>cat>Garfield
		List<Animal> animal = new ArrayList<>();
		List<Cat> cat = new ArrayList<>();
		List<Garfield> garfield = new ArrayList<>();

		animal.add(new Animal());
		cat.add(new Cat());
		garfield.add(new Garfield());

		// Test
		// 下行编译出错，值能赋值Cat或Cat的子类的集合
		//List<? extends Cat> extendsCatFromAnimal = animal;

		List<? super Cat> superCatFromAnimal = animal;

		List<? extends Cat> extendsCatFromCat = cat;
		List<? super Cat> superCatFromCat = cat;

		List<? extends Cat> extendsCatFromGarfield = garfield;
		// 下行编译出错
		//List<? super Cat> superCatFromGarfield = garfield;

		// 测试add方法
		// 下面三行将报错
		//extendsCatFromCat.add(new Animal());
		//extendsCatFromCat.add(new Cat());
		//extendsCatFromCat.add(new Garfield());

		// 编译出错，只能添加Cat或Cat的子类
		//superCatFromCat.add(new Animal());
		superCatFromCat.add(new Cat());
		superCatFromCat.add(new Garfield());

		// 测试get方法
		Object catExtends = extendsCatFromCat.get(0);
		Cat catExtends1 = extendsCatFromCat.get(0);

		// 下行将会出错, 因为会有类型擦除，所以不知道类型
		//Garfield garfield1 = extendsCatFromGarfield.get(0);
		Object garfield1 = extendsCatFromGarfield.get(0);
		System.out.println(garfield1);
	}
}