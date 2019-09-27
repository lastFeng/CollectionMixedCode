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
package com.main;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: L.J
 * @Email: alieismy@gmail.com
 * @version: 1.0
 * @create: 2019/9/26 15:35
 */
public class Test {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();

        RequestConfig config = RequestConfig.DEFAULT;

        String result = "";
        HttpPost httpPost = new HttpPost("http://192.168.8.117:80/cgi-bin/webui?op=SM_recover_config");

        httpPost.setConfig(config);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        File file = new File("/usr/local/policy/back-up/839b5323d37211e9831200ffd7be9494/fwconfig");
        multipartEntityBuilder.addBinaryBody("file", file);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity r = response.getEntity();

        if (r != null) {
            result = EntityUtils.toString(r, Charset.forName("UTF-8"));
        }

        EntityUtils.consume(r);

        if (response != null) {
            response.close();
        }

        System.out.println(result);
    }


}