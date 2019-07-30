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
package async.wrapper;

import async.benum.IEnum;
import async.request.BaseRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/11 15:15
 */
public class ConcreateWapper {
	private List<Concreate> wrapper = new ArrayList<Concreate>();
	public ConcreateWapper(){}

	public void setParams(IEnum baseEnum, Map<String, ?> variables, Map<BaseRequest, ?> request) {
		wrapper.add(new Concreate(baseEnum, variables, request));
	}

	public List<Concreate> getWrapper() {
		return wrapper;
	}

	public static class Concreate{
		private IEnum baseEnum;
		private Map<String, ?> variables;
		private Map<BaseRequest, ?> request;

		public Concreate(IEnum baseEnum, Map<String, ?> variables, Map<BaseRequest, ?> request) {
			this.baseEnum = baseEnum;
			this.variables = variables;
			this.request = request;
		}

		public IEnum getBaseEnum() {
			return baseEnum;
		}

		public void setBaseEnum(IEnum baseEnum) {
			this.baseEnum = baseEnum;
		}

		public Map<String, ?> getVariables() {
			return variables;
		}

		public void setVariables(Map<String, ?> variables) {
			this.variables = variables;
		}

		public Map<BaseRequest, ?> getRequest() {
			return request;
		}

		public void setRequest(Map<BaseRequest, ?> request) {
			this.request = request;
		}
	}
}