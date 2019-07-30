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

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/5 15:01
 */
public class RedisCookiesAndCache {
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 15;
    private static Jedis conn;

    public static void main(String[] args) throws InterruptedException {
        new RedisCookiesAndCache().run();
    }

    public void run() throws InterruptedException {
        // 连接
        conn = Chapter01.connectRedis();

        // 登录Cookie
        testLoginCookies(conn);

        // 购物车Cookie
        testShoppingCartCookies(conn);

        // 缓存
        testCacheRows();
        testCacheRequest();

        // 关闭
        Chapter01.closeRedis(conn);
    }

    /**
     * 登录Cookies的操作
     */
    public void testLoginCookies(Jedis conn) throws InterruptedException {
        System.out.println("============开始登录Cookies的操作===========");
        String token = UUID.randomUUID().toString();

        // 更新token
        updateToken(conn, token, "username", "itemX");

        System.out.println("给用户：'username' 刚刚登录或更新了Cookie：" + token);

        System.out.println("当我们使用token时，我们可以获得哪个用户？");
        String user = checkToken(conn, token);
        System.out.println(user);
        assert user != null;

        // 通过线程清理Cookie
        CleanSessionThread thread = new CleanSessionThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);

        if (thread.isAlive()) {
            throw new RuntimeException("清除session的线程还存活？？真的假的");
        }

        long loginLen = conn.hlen("login:");
        System.out.println("当前的可用会话有：" + loginLen);

        assert loginLen == 0;
    }

    private String checkToken(Jedis conn, String token) {
        return conn.hget("login:", token);
    }

    public void updateToken(Jedis conn, String token, String username, String item) {
        long timestamp = System.currentTimeMillis() / 1000;
        conn.hset("login:", token, username);
        conn.zadd("recent:", timestamp, token);

        if (item != null) {
            conn.zadd("viewed:" + token, timestamp, item);
            // 移除之前的cookie
            conn.zremrangeByRank("viewed:" + token, 0, -26);
            conn.zincrby("viewed:" + token, -1, item);
        }
    }

    /**
     * 清理会话的线程
     * 将会话中的所有session都清除了
     */
    private class CleanSessionThread extends Thread {
        private int limit;
        private boolean quit;

        public CleanSessionThread(int limit) {
            this.limit = limit;
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            while (!quit) {
                long size = conn.zcard("recent:");

                // 长度没有超限制，不处理
                if (size <= limit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                // 内容超出限制，清除, 不是一次性全删，而是多批次删除
                long endIndex = Math.min(size - limit, 100);
                Set<String> tokenSets = conn.zrange("recent:", 0, endIndex - 1);
                String[] tokens = tokenSets.toArray(new String[tokenSets.size()]);

                List<String> sessionsKey = new ArrayList<String>();
                for (String token : tokens) {
                    sessionsKey.add("viewed:" + token);
                }

                conn.del(sessionsKey.toArray(new String[sessionsKey.size()]));
                conn.hdel("login:", tokens);
                conn.zrem("recent:", tokens);
            }
        }
    }

    /**
     * 购物车Cookies的操作
     */
    private void testShoppingCartCookies(Jedis conn) throws InterruptedException {
        System.out.println("-------------开始购物车Cookies的操作---------");

        String token = UUID.randomUUID().toString();

        System.out.println("刷新会话");
        updateToken(conn, token, "username", "itemX");
        // 向购物车添加物品
        addToCart(token, "itemY", 3);

        // 获取当前购物车的所有物品和数量
        Map<String, String> items = conn.hgetAll("cart:" + token);

        // 打印
        System.out.println("当前购物车有：");
        for (Map.Entry<String, String> entry : items.entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }

        assert items.size() >= 1;

        System.out.println("清理会话和购物车：");
        CleanFullSessionsThread thread = new CleanFullSessionsThread(0);

        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("线程还存在，这不可能！");
        }

        items = conn.hgetAll("cart:" + token);
        System.out.println("当前购物车还剩：");
        for (Map.Entry<String, String> entry : items.entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }

        assert items.size() == 0;
    }

    public void addToCart(String token, String item, int count) {
        // 数量小于0，删除
        if (count <= 0) {
            conn.hdel("cart:" + token, item);
        }
        // 存不存在都可以直接增加吧
        else {
            conn.hincrBy("cart:" + token, item, count);
        }

    }

    private class CleanFullSessionsThread extends Thread {
        private int limit;
        private boolean quit;

        public CleanFullSessionsThread(int limit) {
            this.limit = limit;
            this.quit = false;
        }

        @Override
        public void run() {
            while (!quit) {
                long size = conn.zcard("recent:");
                if (size <= limit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                long endIndex = Math.min(size - limit, 100);

                // 获取从0-endIndex-1的内容
                Set<String> sessionSet = conn.zrange("recent:", 0, endIndex - 1);
                String[] sessions = sessionSet.toArray(new String[sessionSet.size()]);

                List<String> sessionKeys = new ArrayList<String>();
                for (String se : sessions) {
                    sessionKeys.add("viewed:" + se);
                    sessionKeys.add("cart:" + se);
                }

                conn.del(sessionKeys.toArray(new String[sessionKeys.size()]));
                conn.hdel("login:", sessions);
                conn.zrem("recent:", sessions);
            }
        }

        public void quit() {
            this.quit = true;
        }
    }

    public void testCacheRows() throws InterruptedException {
        System.out.println("------开始行缓存===========");
        System.out.println("首先每五秒中缓存：itemX");
        scheduleCacheRow("itemX", 5);

        System.out.println("我们的周期执行如下：");
        Set<Tuple> s = conn.zrangeWithScores("schedule:", 0, -1);
        for (Tuple tuple : s) {
            System.out.println(" " + tuple.getElement() + ": " + tuple.getScore());
        }
        assert s.size() != 0;

        System.out.println("使用缓存线程来缓存数据：");
        CacheRowThread thread = new CacheRowThread();

        thread.start();
        Thread.sleep(1000);
        System.out.println("缓存数据为：");
        String r = conn.get("inv:itemX");
        System.out.println(r);
        assert r != null;

        System.out.println("5秒之后再查看");
        Thread.sleep(5000);
        System.out.println("注意，数据已经发生了变化");
        String r2 = conn.get("inv:itemX");
        System.out.println(r2);
        assert r2 != null;
        assert !r.equals(r2);

        System.out.println("强制不适用缓存");
        scheduleCacheRow("itemX", -1);
        Thread.sleep(1000);
        r = conn.get("inv:itemX");
        System.out.println("缓存清楚了？" + (r == null));
        assert r != null;

        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("xxx");
        }
    }

    public void scheduleCacheRow(String item, int second) {
        conn.zadd("delay:", second, item);
        conn.zadd("schedule:", System.currentTimeMillis() / 1000, item);
    }

    private class CacheRowThread extends Thread {
        private boolean quit = false;

        @Override
        public void run() {
            Gson gson = new Gson();
            while (!quit) {
                Set<Tuple> range = conn.zrangeWithScores("schedule:", 0, 0);
                Tuple next = range.size() > 0 ? range.iterator().next() : null;
                long now = System.currentTimeMillis() / 1000;
                if (next == null || next.getScore() > now) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                String rowId = next.getElement();
                double delay = conn.zscore("delay:", rowId);
                if (delay <= 0) {
                    conn.zrem("delay:", rowId);
                    conn.zrem("schedule:", rowId);
                    conn.del("inv:" + rowId);
                    continue;
                }

                Inventory row = Inventory.get(rowId);
                conn.zadd("schedule:", now + delay, rowId);
                conn.set("inv:" + rowId, gson.toJson(row));
            }
        }

        public void quit() {
            this.quit = true;
        }
    }

    private static class Inventory {
        private String id;
        private String data;
        private long time;

        private Inventory(String id) {
            this.id = id;
            this.data = "data to cache...";
            this.time = System.currentTimeMillis() / 1000;
        }

        public static Inventory get(String id) {
            return new Inventory(id);
        }
    }

    public void testCacheRequest() throws InterruptedException {

    }
}