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
package async.futureCallback;

import async.benum.IEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 15:20
 */
public class CommonListenableCallBack<T> implements ListenableFutureCallback<T> {

	private IEnum type;
	private Map<IEnum, Object> resultValue;
	private volatile CountDownLatch latch;

	public CommonListenableCallBack(IEnum type, Map<IEnum, Object> resultValue, CountDownLatch latch) {
		this.type = type;
		this.resultValue = resultValue;
		this.latch = latch;
	}

	public void onFailure(Throwable ex) {
		latch.countDown();
	}

	public void onSuccess(T result) {
		ResponseEntity<T> re = (ResponseEntity<T>) result;

		if (re != null && re.getBody() != null) {
			T body = re.getBody();
			if (type != null) {
				resultValue.put(type, body);
			}
		}
		latch.countDown();
	}
}