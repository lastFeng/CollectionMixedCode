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
import redis.clients.jedis.Tuple;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/6 10:03
 */
public class RedisTransactionPipeline {
    private static Jedis conn;

    public static void main(String[] args) {
        new RedisTransactionPipeline().run();
    }

    static {
        conn = Chapter01.connectRedis();
    }

    public void run() {
        // 事务非流水线型操作
        testListItem(false);

        testPurchaseItem();

        testBenchmarkUpdateToken();

        Chapter01.closeRedis(conn);
    }

    public void testListItem(boolean nested) {
        if (!nested) {
            System.out.println("=---==测试商品列表==----");
        }

        String seller = "userX";
        String item = "itemX";
        // 添加包裹信息--包裹:卖家--物品
        conn.sadd("inventory:" + seller, item);

        // 获取某个卖家包裹中的所有物品
        Set<String> items = conn.smembers("inventory:" + seller);

        System.out.println("卖家" + seller + "能卖的物品有");
        for (String i : items) {
            System.out.print(i + "===");
        }
        System.out.println();
        assert items.size() > 0;

        // 卖家是否有物品可以卖,将可卖物品放入market列表中
        boolean l = listItem(item, seller, 10);
        System.out.println("商品：" + item + "放入market列表成功：" + l);
        assert l;

        // market列表中有多少物品正在已经售出
        Set<Tuple> sells = conn.zrangeByScoreWithScores("market:", 0, -1);
        System.out.println("已卖出商品");
        for (Tuple tuple : sells) {
            System.out.println(" " + tuple.getElement() + ": " + tuple.getScore());
        }

        assert sells.size() > 0;
    }

    public boolean listItem(String itemId, String sellerId, double price) {
        String inventory = "inventory:" + sellerId;
        String item = itemId + "." + sellerId;

        long end = System.currentTimeMillis() + 5000;
        // 五秒中内需要检查包裹中是否还有物品
        while (System.currentTimeMillis() < end) {
            // 监控包裹，事务型
            conn.watch(inventory);
            // 物品已经卖出，退出监控
            if (!conn.sismember(inventory, itemId)) {
                conn.unwatch();
                return false;
            }

            // 使用事务，multi
            Transaction trans = conn.multi();

            // 需要执行的事务
            trans.zadd("market:", price, item);
            // 删除卖家包裹列表中的itemId
            trans.srem(inventory, itemId);

            // 提交获得结果
            List<Object> results = trans.exec();

            // 没结果继续等待
            if (results == null) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * 用户的资金能不能买得起商品？
     */
    public void testPurchaseItem() {
        System.out.println("---====--开始事务型流水线--====");
        // market:列表中设置相应的数据
        testListItem(true);
        // 设置相应内容
        System.out.println("设置相应必须内容");
        // 用户资金
        conn.hset("users:userY", "funds", "125");
        // 打印用户资金信息
        Map<String, String> users = conn.hgetAll("users:userY");
        for (Map.Entry<String, String> entry : users.entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }
        assert users.size() > 0;
        assert users.get("funds") != null;

        // 用户要买东西了
        System.out.println("购买商品：");
        boolean isBuy = purchaseItem("userY", "itemX", "userX", 10);

        // 查看用户买完之后还剩多少钱
        users = conn.hgetAll("users:userY");
        for (Map.Entry<String, String> entry : users.entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }
        assert users.size() > 0;

        // 查看买家包裹里的物品
        String buyer = "userY";
        Set<String> inventorys = conn.smembers("inventory:" + buyer);
        System.out.println("买家包裹物品：");
        for (String good : inventorys) {
            System.out.print(good + " ");
        }
        System.out.println();
        assert inventorys.size() > 0;
        assert inventorys.contains("itemX");

        // market:列表中的商品已经不存在了
        assert conn.zscore("market:", "itemX.userX") == null;
    }

    public boolean purchaseItem(String buyerId, String itemId, String sellerId, double lprice) {
        // 构建买家、卖家、买家包裹信息，在10秒内进行查询
        String buyer = "users:" + buyerId;
        String seller = "users:" + sellerId;
        String inventory = "inventory:" + buyerId;
        String item = itemId + "." + sellerId;

        long end = System.currentTimeMillis() + 10000;

        while (System.currentTimeMillis() < end) {
            // 监控对应的key
            conn.watch("market:", buyer);
            // 获取所需信息
            double price = conn.zscore("market:", item);
            double funds = Double.parseDouble(conn.hget(buyer, "funds"));
            // 如果物品价格有变或者买家资金不足，返回false
            if (price != lprice || funds < price) {
                conn.unwatch();
                return false;
            }
            // 开启事务
            Transaction trans = conn.multi();
            // 事务中需要操作的内容--- 卖家加钱，买家扣钱，买家包裹增加物品，删除market:列表中的相应数据
            trans.hincrBy(seller, "funds", (long) price);
            trans.hincrBy(buyer, "funds", (long) (-1 * price));
            trans.sadd(inventory, itemId);
            trans.srem("market:", item);
            // 判断结果
            List<Object> results = trans.exec();
            if (results == null) {
                continue;
            }
            return true;
        }
        return false;
    }

    public void testBenchmarkUpdateToken() {
        System.out.println("================");
        benchmarkUpdateToken(5);
    }

    /**
     * 使用反射进行多方法调用
     */
    public void benchmarkUpdateToken(int duration) {
        try {
            Class[] args = new Class[]{
                String.class, String.class, String.class};

            Method[] methods = new Method[]{
                this.getClass().getDeclaredMethod("updateToken", args),
                this.getClass().getDeclaredMethod("updateTokenPipeline", args)};

            for (Method method : methods) {
                int count = 0;
                long start = System.currentTimeMillis();
                long end = System.currentTimeMillis() + (duration * 1000);
                while (System.currentTimeMillis() < end) {
                    count++;
                    method.invoke(this, "token", "user", "item");
                }

                long delta = (System.currentTimeMillis() - start) / 1000;
                System.out.println(
                    method.getName() + " " +
                        count + " " +
                        delta + " " + (count / delta));
            }
        } catch (Exception e) {
            throw new RuntimeException("==========RuntimeException=========");
        }
    }

    public void updateToken(String token, String user, String item) {
        long timestamp = System.currentTimeMillis() / 1000;

        conn.hset("login:", token, user);
        conn.zadd("recent:", timestamp, token);

        if (item != null) {
            conn.zadd("viewed:" + token, timestamp, item);
            conn.zremrangeByRank("viewed:" + token, 0, -26);
            conn.zincrby("viewed:" + token, -1, item);
        }
    }

    public void updateTokenPipeline(String token, String user, String item) {
        long timestamp = System.currentTimeMillis() / 1000;

        Pipeline pipeline = conn.pipelined();
        // 开启多事务
        pipeline.multi();
        // 执行事务
        pipeline.hset("login:", token, user);
        pipeline.zadd("recent:", timestamp, token);

        if (item != null) {
            pipeline.zadd("viewed:" + token, timestamp, item);
            pipeline.zremrangeByRank("viewed:" + token, 0, -26);
            pipeline.zincrby("viewed:" + token, -1, item);
        }

        pipeline.exec();
    }
}