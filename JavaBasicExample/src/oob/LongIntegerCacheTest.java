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
 * @create: 2019/6/17 10:11
 * 基础类型的包装类，在推荐使用ValueOf()，能合理利用缓存，提升程序性能。所以在进行对象之间的比较时，全部使用equals()方法。
 *
 * Boolean：使用静态final变量定义，valueOf()就是返回这两个静态值
 * Byte：表示范围是-128~127，全部缓存
 * Short：表示范围是-23768~32767，缓存范围是-128~127
 * Character：表示范围是0~65535，缓存范围是0~127
 * Long: 缓存范围是-128~127
 * Integer：缓存范围是：-128~127。但它是唯一可以修改缓存范围的包装类，在VM Options加入参数 -XX:AutoBoxCacheMax=7777，
 * 即可设置最大缓存值为7777。
 * @Description：所有的POJO类属性必须使用包装数据类型、RPC方法的返回值和参数必须使用包装数据类型、所有局部变量推荐使用基本数据类型 StringBuffer:线程安全
 * StringBuilder：非线程安全
 */
public class LongIntegerCacheTest {
	public static void main(String[] args) {
		Long a = 127L;
		Long b = 127L;

		System.out.println("Long max cache value is 127, " + "and the result is: " + (a == b));

		Long a1 = 128L;
		long b1 = 128L;
		System.out.println("Long=128 cache is: " + (a1 == b1));

		Long c = -128L;
		Long d = -128L;
		System.out.println("Long min cached value is -128, " + "and the result is: " + (c == d));

		Long c1 = -129L;
		Long d1 = -129L;
		System.out.println("Long=-129 cache is: " + (c1 == d1));

		Long e = 1000L;
		Long f = 1000L;
		System.out.println("Long=1000 is: " + (e == f));

		// JVM AutoBoxCacheMax 只对Integer有效  JVM option: -XX:AutoBoxCacheMax=7777(在Integer的范围数值内均可)
		Integer x = 1000;
		Integer y = 1000;
		System.out.println("Integer=1000 is " + (x == y));
	}
}