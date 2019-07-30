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
 * @create: 2019/6/17 9:50
 * 基本数据类型：boolean、byte、char、short、int、long、float、double、refvar（引用变量）
 */
public class BasicDataType {
	// 对象头最小占用空间12个字节

	// 下方4个byte类型分配后(每个byte对象1个B)，对象占用大小是16个字节
	byte b1;
	byte b2;
	byte b3;
	byte b4;

	// 下方每个引用变量占用4个字节，共20字节
	Object object1;
	Object object2;
	Object object3;
	Object object4;

	// RefObject实例占用空间并不计算在对象内，依然只计算引用变量大小4个字节
	RefObjOther o1 = new RefObjOther();
	RefObjOther o2 = new RefObjOther();

	// 综上，BasicDataType对象占用： 12B + (1B * 4) + (4B * 5) + (4B * 2) = 44B --> 48B
	// 取8的倍数为48B

}

class RefObjOther {
	// double 类型占用8个字节，但此处是数组引用变量
	// 所以对象头 12B + 4B = 16B，并非8012B
	// 这个数组引用的是double[]类型，指向实际分配的数组空间首地址
	// 在new对象时，已经实际分配空间
	double[] d = new double[1000];
}