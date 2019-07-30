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
package async.template;

import async.request.BaseRequest;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 14:54
 */
public interface Template {
	<T>ListenableFuture<ResponseEntity<T>> getAsyncForObject(BaseRequest baseRequest, Class<T> responseType)
		throws Exception;

	<T> ListenableFuture<ResponseEntity<T>> getAsyncForObject(BaseRequest baseRequest,
	                                                          ParameterizedTypeReference<T> responseType) throws Exception;

	<T> ListenableFuture<ResponseEntity<T>> getAsyncForObject(BaseRequest baseRequest, Class<T> responseType,
	                                                          Map<String, ?> uriVariable) throws Exception;

	<T> ListenableFuture<ResponseEntity<T>> getAsyncForObject(BaseRequest baseRequest,
	                                                          ParameterizedTypeReference<T> responseType,
	                                                          Map<String, ?> uriVariables) throws Exception;
}