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
import redis.clients.jedis.ZParams;

import java.util.ArrayList;
import java.util.HashMap;
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
 * @create: 2019/5/5 11:49
 */
public class Chapter01 {
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

    public static void main(String[] args) {
        new Chapter01().run();
    }

    public void run() {
        // 连接数据库
        Jedis conn = connectRedis();

        // 提交文章
        String articleId = postArticle(conn, "username", "A Title", "http://www.google.com");

        // 提示一些信息
        System.out.println("我们发布了新文章，文章id为：" + articleId);
        System.out.println("该文章的Hash内容如下：");
        // 打印hash内容
        Map<String, String> articleData = conn.hgetAll("article:" + articleId);
        for (Map.Entry<String, String> entry : articleData.entrySet()) {
            System.out.println(" " + entry.getKey() + ": " + entry.getValue());
        }

        // 为文章投票
        voteArticle(conn, "otheruser", ARTICLE_PREFIX + articleId);
        // 打印投票信息
        String votes = conn.hget(ARTICLE_PREFIX + articleId, "votes");
        System.out.println("该文章的" + articleId + "投票数为：" + votes);
        assert Integer.valueOf(votes) > 1;

        // 获取投票最多的文章
        System.out.println("现在投票最多的文章有：");
        List<Map<String, String>> articles = getArticles(conn, 1);
        // 打印最多投票文章
        printArticles(articles);
        assert articles.size() >= 1;

        // 为文章增加组
        addGroup(conn, articleId, new String[]{"new-group"});
        System.out.println("将文章" + articleId + "加入组中，组内的其他文章有");
        articles = getGroupArticles(conn, "new-group", 1);

        // 打印组中文章信息
        printArticles(articles);
        assert articles.size() >= 1;

        closeRedis(conn);
    }

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
     * @param conn  redis连接
     * @param user  用户
     * @param title 文章名
     * @param link  文章超链接
     * @return articleId
     */
    public String postArticle(Jedis conn, String user, String title, String link) {
        // 获取文章id--自增型
        String articleId = String.valueOf(conn.incr(ARTICLE_PREFIX));
        // 设置投票用户
        String vote = "vote:" + articleId;
        conn.sadd(vote, user);

        // 设置投票的过期时间
        conn.expire(vote, ONE_WEEK_IN_SECONDS);

        // 当前文章时间
        long now = System.currentTimeMillis() / 1000;
        String article = ARTICLE_PREFIX + articleId;
        // 设置文章内容
        Map<String, String> articleData = new HashMap<String, String>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("user", user);
        articleData.put("now", String.valueOf(now));
        articleData.put("votes", "1");
        conn.hmset(article, articleData);

        // 设置文章相应的有序集合--便于查询
        conn.zadd("score:", now + VOTE_SOCRE, article);
        conn.zadd("time:", now, article);

        return articleId;
    }

    /**
     * @param conn    redis连接
     * @param user    投票人
     * @param article 在redis中保存的key值
     */
    public void voteArticle(Jedis conn, String user, String article) {
        // 获取key对应的数据，即判断有没有过期
        long cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS;
        if (conn.zscore("time:", article) < cutoff) {
            return;
        }

        // 有，就增加投票
        // score：中的值也要增加
        // 但是同一个用户只能投票一次
        //String articleId =article.split(":")[-1];  // 错误
        String articleId = article.substring(article.indexOf(':') + 1);
        if (conn.sadd("voted:" + articleId, user) == 1) {
            conn.zincrby("score:", VOTE_SOCRE, article);
            conn.hincrBy(article, "votes", 1);
        }
    }

    /**
     * @param conn
     * @param page 获取第page页的25个数据（一页有25个数据）
     * @return 返回多个article信息
     */
    public List<Map<String, String>> getArticles(Jedis conn, int page) {
        return getArticles(conn, page, "score:");
    }

    /**
     * @param conn
     * @param page  返回的页数
     * @param order zset中的key值
     * @return 返回多个article信息
     */
    private List<Map<String, String>> getArticles(Jedis conn, int page, String order) {
        // 计算该页的起始位置
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        // 获取范围内的所有article的key值
        Set<String> ids = conn.zrevrange(order, start, end);
        List<Map<String, String>> articles = new ArrayList<Map<String, String>>();
        // 根据key值，获取article的所有内容，同时将article也加入其中
        for (String id : ids) {
            Map<String, String> article = conn.hgetAll(id);
            article.put("article", id);
            articles.add(article);
        }

        return articles;
    }

    /**
     * 打印文章信息
     */
    public void printArticles(List<Map<String, String>> articles) {
        for (Map<String, String> article : articles) {
            System.out.println("Article:" + article.get("article"));
            for (Map.Entry<String, String> entry : article.entrySet()) {
                // 去掉article属性，外层已经打印了
                if (entry.getKey().equals("article")) {
                    continue;
                }
                System.out.println(" " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /**
     * 将文章加入到组中
     *
     * @param conn      redis连接
     * @param articleId 文章id
     * @param toAdd     加入的组名
     */
    public void addGroup(Jedis conn, String articleId, String[] toAdd) {
        String article = ARTICLE_PREFIX + articleId;
        for (String group : toAdd) {
            conn.sadd("group:" + group, article);
        }
    }

    /**
     * 获取第page页中的组内的文章
     *
     * @param conn  redis的连接
     * @param group 需要查询的组名
     * @param page  第page页的数据
     * @return 返回组名中第page页的文章信息
     */
    public List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page) {
        return getGroupArticles(conn, group, page, "score:");
    }

    /**
     * @param conn
     * @param group
     * @param page
     * @param order group中的有序集合key内容
     * @return
     */
    private List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page, String order) {
        String key = order + group;

        // 如果key不存在，那么就需要新建
        if (!conn.exists(key)) {
            ZParams params = new ZParams().aggregate(ZParams.Aggregate.MAX);
            // 求交集，将其放入到新建的键值对中
            conn.zinterstore(key, params, "group:" + group, order);
        }
        return getArticles(conn, page, key);
    }

    public static void closeRedis(Jedis conn) {
        conn.close();
    }
}