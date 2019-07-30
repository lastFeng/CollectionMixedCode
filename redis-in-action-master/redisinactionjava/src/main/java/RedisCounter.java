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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import util.RedisUtil;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/8 16:22
 */
public class RedisCounter {
    private static final String DEBUG = "debug";
    private static final String INFO = "info";
    private static final String WARNING = "warning";
    private static final String ERROR = "error";
    private static final String CRITICAL = "critical";

    public static final Collator COLLATOR = Collator.getInstance();

    public static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("EEE MM dd HH:00:00 yyyy");
    public static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00");

    static {
        ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        new RedisCounter().run();
    }

    private void run() {
        Jedis conn = RedisUtil.connectRedis();

        /**
         * 记录最近的日志
         * */
        //testLogRecent(conn);

        /**
         * 记录常见日志
         * */
        testLogCommon(conn);

        RedisUtil.closeRedis(conn);
    }

    private void testLogCommon(Jedis conn) {
        System.out.println("写入一些消息");

        for (int i = 1; i < 1000; i++) {
            logCommon(conn, "test", "这是第x" + i + "条消息");
        }
    }

    private void logCommon(Jedis conn, String name, String message) {
        logCommon(conn, name, message, INFO, 50000);
    }

    private void logCommon(Jedis conn, String name, String message, String severity, int timeout) {
        String commonDest = "common:" + name + ":" + severity;
        String startKey = commonDest + ":start";

        long end = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < end) {
            conn.watch(startKey);
            String hourStart = ISO_FORMAT.format(new Date());
            String existing = conn.get(startKey);

            Transaction transaction = conn.multi();

            if (existing != null && COLLATOR.compare(existing, hourStart) < 0) {
                transaction.rename(commonDest, commonDest + ":last");
                transaction.rename(startKey, startKey + ":pstart");
                transaction.set(startKey, hourStart);
            }
            transaction.zincrby(commonDest, 1, message);

            String recentDest = "recent:" + name + ':' + severity;
            transaction.lpush(recentDest, TIMESTAMP.format(new Date()) + ' ' + message);
            transaction.ltrim(recentDest, 0, 99);
            List<Object> results = transaction.exec();

            if (results == null) {
                continue;
            }
            return;
        }
    }

    private void testLogRecent(Jedis conn) {
        System.out.println("开始记录最近日志");
        for (int i = 1; i < 1000; i++) {
            logRecent(conn, "test", "这是第" + i + "条消息");
        }

        // 查询最近的消息
        List<String> recents = conn.lrange("recent:test:info", 0, -1);
        System.out.println("最近消息有：" + recents.size());
    }

    /**
     * @param conn
     * @param name
     * @param message 添加最近日志消息
     */
    private void logRecent(Jedis conn, String name, String message) {
        logRecent(conn, name, message, INFO);
    }

    /**
     * @param conn
     * @param name
     * @param message
     * @param severity 分类型添加最近日志消息
     */
    private void logRecent(Jedis conn, String name, String message, String severity) {
        String destination = "recent:" + name + ":" + severity;
        Pipeline pipe = conn.pipelined();
        pipe.lpush(destination, TIMESTAMP.format(new Date()) + " " + message);
        pipe.ltrim(destination, 0, 99);
        pipe.sync();
    }
}