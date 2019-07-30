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
package util;

import redis.clients.jedis.Jedis;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/8 16:32
 */
public class RedisUtil {
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 15;
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SOCRE = 432;
    private static final int ARTICLES_PER_PAGE = 25;
    private static final String ARTICLE_PREFIX = "article:";
    private static final String REDIS_HOST = "localhost";
    private static final String REDIS_PASSWORD = "Abcd123456";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_DATABASE = 15;

    /**
     * @return redis 返回redis连接
     */
    public static Jedis connectRedis() {

        Jedis conn = new Jedis(REDIS_HOST, REDIS_PORT);
        conn.auth(REDIS_PASSWORD);
        conn.select(REDIS_DATABASE);
        return conn;
    }

    /**
     * 关闭连接
     */
    public static void closeRedis(Jedis conn) {
        conn.close();
    }
}