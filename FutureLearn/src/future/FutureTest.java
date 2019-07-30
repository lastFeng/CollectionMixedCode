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
package future;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 13:48
 */
public class FutureTest {

	// 创建线程池
	final static ExecutorService SERVICE = Executors.newCachedThreadPool();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();

		// task
		Future<Boolean> booleanTask = SERVICE.submit(() -> true);

		while (true) {
			if (booleanTask.isDone() && !booleanTask.isCancelled()) {
				// 模拟耗时
				Thread.sleep(500);
				Boolean result = booleanTask.get();
				System.out.println("BooleanTask: " + result);
				break;
			}
		}

		// task
		Future<String> stringTask = SERVICE.submit(() -> "Hello World!");

		while (true) {
			if (stringTask.isDone() && !stringTask.isCancelled()) {
				String result = stringTask.get();
				System.out.println("StringTask: " + result);
				break;
			}
		}

		// task
		Future<Integer> integerTask = SERVICE.submit(() -> new Random().nextInt(100));

		while (true) {
			if (integerTask.isDone() && !integerTask.isCancelled()) {
				Integer result = integerTask.get();
				System.out.println("IntegerTask: " + result);
				break;
			}
		}

		// 执行时间
		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end - start));
	}
}