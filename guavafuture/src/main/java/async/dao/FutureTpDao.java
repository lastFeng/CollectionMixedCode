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
package async.dao;

import async.benum.IEnum;
import async.request.BaseRequest;
import async.template.AsynClientTemplate;
import async.wrapper.ConcreateWapper;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 15:24
 */
public class FutureTpDao {
	public AsynClientTemplate asyncHttpClient;

	public FutureTpDao() {
		asyncHttpClient = new AsynClientTemplate(null);
	}

	public FutureTpDao(AsynClientTemplate tp) {
		this.asyncHttpClient = tp;
	}

	// 获取数据
	public Map<IEnum, Object> getHttpData(ConcreateWapper wapper) {
		if (wapper == null) {
			return new HashMap<IEnum, Object>();
		}

		final CountDownLatch latch = new CountDownLatch(wapper.getWrapper().size());
		final Map<IEnum, Object> result = new HashMap<IEnum, Object>();

		if (wapper.getWrapper() != null) {
			for (final ConcreateWapper.Concreate wp: wapper.getWrapper()) {
				try {
					Map<BaseRequest, ?> requestMap = wp.getRequest();
					for (final BaseRequest tpRequestInfo: requestMap.keySet()) {
						getHttpData(wp, tpRequestInfo, latch, requestMap, result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return result;
	}

	// 发送http请求，获取请求结果
	private void getHttpData(ConcreateWapper.Concreate wp, BaseRequest tpRequestInfo,
	                         CountDownLatch latch, Map<BaseRequest,?> requestMap,
	                         Map<IEnum, Object> result) throws Exception {
		ListenableFuture<?> staResponse = null;

		if (requestMap.get(tpRequestInfo) instanceof ParameterizedTypeReference<?>) {
			ParameterizedTypeReference<?> responseType = (ParameterizedTypeReference<?>)
				requestMap.get(tpRequestInfo);
			staResponse = asyncHttpClient.getAsyncForObject(tpRequestInfo, responseType, wp.getVariables());

		}
		else if (requestMap.get(tpRequestInfo) instanceof Class<?>) {
			Class<?> responseType = (Class<?>) requestMap.get(tpRequestInfo);

			staResponse = asyncHttpClient.getAsyncForObject(tpRequestInfo, responseType);
		}
		else {
			throw new RuntimeException("requestType error...");
		}
		addCallBack(staResponse, wp.getBaseEnum(), latch, result);
	}

	// 增加回调
	private <T>void addCallBack(ListenableFuture<?> staResponse, IEnum baseEnum, CountDownLatch latch, Map<IEnum, Object> result) {
		if (staResponse != null) {
			// TODO: 增加回调，出现版本更新，API不一致
			//Futures.addCallback(staResponse, );
		}
	}
}