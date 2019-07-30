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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;
import java.util.concurrent.Executors;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 14:00
 */
public class TestListenableFuture {

	final static ListeningExecutorService SERVICE = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		// task
		ListenableFuture<Boolean> booleanTask = SERVICE.submit(() -> true);

		Futures.addCallback(booleanTask, new FutureCallback<Boolean>() {
			@Override
			public void onSuccess(@Nullable Boolean result) {
				System.out.println("BooleanTask: " + result);
			}

			@Override
			public void onFailure(Throwable t) {

			}
		}, SERVICE);

		// task
		ListenableFuture<String> stringTask = SERVICE.submit(() -> "Hello World");
		Futures.addCallback(stringTask, new FutureCallback<String>() {
			@Override
			public void onSuccess(@Nullable String result) {
				System.out.println("StringTask: " + result);
			}

			@Override
			public void onFailure(Throwable t) {

			}
		}, SERVICE);

		// task
		ListenableFuture<Integer> integerTask = SERVICE.submit(() -> new Random().nextInt(100));
		Futures.addCallback(integerTask, new FutureCallback<Integer>() {
			@Override
			public void onSuccess(@Nullable Integer result) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("IntegerTask: " + result);
			}

			@Override
			public void onFailure(Throwable t) {

			}
		}, SERVICE);

		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end - start));
	}
}