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
package com.springboot.o2o.cache;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

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
 * @create: 2019/5/13 10:04
 */
public class JedisUtil {
    /**
     * Redis连接池对象
     */
    private JedisPool jedisPool;
    private final int expire = 6000;

    /**
     * 操作key的方法
     */
    public Keys keys;

    /**
     * 操作String类型的方法
     */
    public Strings strings;

    /**
     * 操作Set类型的方法
     */
    public Sets sets;

    /**
     * 操作List类型的方法
     */
    public Lists lists;

    /**
     * 操作Hash类型的方法
     */
    public Hash hash;

    /**
     * 获取Redis连接池
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 设置JedisPool连接池
     */
    public void setJedisPool(JedisPoolWriper jedisPool) {
        this.jedisPool = jedisPool.getJedisPool();
    }

    /**
     * 获取Jedis
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * @param key
     * @param seconds 设置过期时间
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }

        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        jedis.close();
    }

    /**
     * @param key 设置默认过期时间
     */
    public void expire(String key) {
        expire(key, expire);
    }

    class Keys {
        public Keys(JedisUtil jedisUtil) {

        }

        /**
         * 清空所有key
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String status = jedis.flushAll();
            jedis.close();
            return status;
        }

        /**
         * 重命名key
         *
         * @param oldKey
         * @param newKey
         * @return
         */
        public String rename(String oldKey, String newKey) {
            return rename(SafeEncoder.encode(oldKey), SafeEncoder.encode(newKey));
        }

        /**
         * 重命名key，仅当新key不存在时执行
         *
         * @param oldKey
         * @param newKey
         * @return
         */
        public long renamenx(String oldKey, String newKey) {
            Jedis jedis = getJedis();
            long status = jedis.renamenx(oldKey, newKey);
            jedis.close();
            return status;
        }

        /**
         * 重命名key
         *
         * @param oleKey
         * @param newKey
         * @return
         */
        public String rename(byte[] oleKey, byte[] newKey) {
            Jedis jedis = getJedis();
            String status = jedis.rename(oleKey, newKey);
            jedis.close();
            return status;
        }

        /**
         * 设置过期时间, 已秒为单位
         *
         * @param key
         * @param seconds
         * @return
         */
        public long expired(String key, int seconds) {
            Jedis jedis = getJedis();
            long count = jedis.expire(key, seconds);
            jedis.close();
            return count;
        }

        /**
         * 设置过期时间，它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
         *
         * @param key
         * @param timestamp
         * @return
         */
        public long expiredAt(String key, long timestamp) {
            Jedis jedis = getJedis();
            long count = jedis.expireAt(key, timestamp);
            jedis.close();
            return count;
        }

        /**
         * 查询key的过期时间
         *
         * @param key
         * @return
         */
        public long ttl(String key) {
            Jedis jedis = getJedis();
            long len = jedis.ttl(key);
            jedis.close();
            return len;
        }

        /**
         * 取消对key过期时间的设置
         *
         * @param key
         */
        public long persist(String key) {
            Jedis jedis = getJedis();
            long count = jedis.persist(key);
            jedis.close();
            return count;
        }

        /**
         * 删除多个String类型的key
         *
         * @param keys
         * @return
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 删除多个byte数组类型的key
         *
         * @param keys
         * @return
         */
        public long del(byte[]... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断key是否存在
         *
         * @param key
         * @return
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            boolean flag = jedis.exists(key);
            jedis.close();
            return flag;
        }

        /**
         * 根据key进行排序，但是集合数据过多不应使用
         *
         * @param key
         * @return List<String>
         */
        public List<String> sort(String key) {
            Jedis jedis = getJedis();
            List<String> list = jedis.sort(key);
            jedis.close();
            return list;
        }

        /**
         * 排序或limit
         *
         * @param key
         * @param params 定义排序类型或limit的起止位置.
         * @return List<String>
         */
        public List<String> sort(String key, SortingParams params) {
            Jedis jedis = getJedis();
            List<String> list = jedis.sort(key, params);
            jedis.close();
            return list;
        }

        /**
         * 返回指定key的存储类型
         *
         * @param key
         * @return String string|list|set|zset|hash
         */
        public String type(String key) {
            Jedis jedis = getJedis();
            String type = jedis.type(key);
            jedis.close();
            return type;
        }

        /**
         * 查找所有匹配模式的键信息
         *
         * @param pattern
         * @return Set<String>
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> keys = jedis.keys(pattern);
            jedis.close();
            return keys;
        }
    }

    class Strings {

        public Strings(JedisUtil jedisUtil) {

        }

        /**
         * 根据key获取字符串
         *
         * @param key key
         * @return value
         */
        public String get(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 添加有过期属性的记录
         *
         * @param key     key
         * @param seconds time
         * @param value   value
         * @return Status
         */
        public String set(String key, int seconds, String value) {
            Jedis jedis = getJedis();
            String status = jedis.setex(key, seconds, value);
            jedis.close();
            return status;
        }

        /**
         * 添加一条可以覆盖数据的记录
         *
         * @param key   key
         * @param value value
         * @return status
         */
        public String set(String key, String value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }

        /**
         * 添加一条不存在的数据才成功
         *
         * @param key   key
         * @param value value
         * @return long
         */
        public long setnx(String key, String value) {
            Jedis jedis = getJedis();
            long status = jedis.setnx(key, value);
            jedis.close();
            return status;
        }

        /**
         * 从指定位置offset添加可覆盖数据的记录
         *
         * @param key    key
         * @param offset offset
         * @param value  value
         * @return long
         */
        public long setRange(String key, long offset, String value) {
            Jedis jedis = getJedis();
            long len = jedis.setrange(key, offset, value);
            jedis.close();
            return len;
        }

        /**
         * 在指定的key中追加value
         *
         * @param key   key
         * @param value value
         * @return long
         */
        public long append(String key, String value) {
            Jedis jedis = getJedis();
            long len = jedis.append(key, value);
            jedis.close();
            return len;
        }

        /**
         * 减少指定的数值
         *
         * @param key    key
         * @param number number
         * @return long
         */
        public long decrBy(String key, long number) {
            Jedis jedis = getJedis();
            long value = decrBy(key, number);
            jedis.close();
            return value;
        }

        /**
         * 增加指定的数值
         *
         * @param key    key
         * @param number number
         * @return long
         */
        public long incrBy(String key, long number) {
            Jedis jedis = getJedis();
            long value = jedis.incrBy(key, number);
            jedis.close();
            return value;
        }

        /**
         * 对指定的key进行截取 [start, end]
         *
         * @param key         key
         * @param startOffset start
         * @param endOffset   end
         * @return subString
         */
        public String getRange(String key, long startOffset, long endOffset) {
            Jedis jedis = getJedis();
            String str = jedis.getrange(key, startOffset, endOffset);
            jedis.close();
            return str;
        }

        /**
         * 获取并设置指定key的value值
         * 如果key不存在会返回null，否则返回原key的value值
         *
         * @param key   key
         * @param value newValue
         * @return string
         */
        public String getSet(String key, String value) {
            Jedis jedis = getJedis();
            String val = jedis.getSet(key, value);
            jedis.close();
            return val;
        }

        /**
         * 批量获取keys的记录，用list保存
         *
         * @param keys keys
         * @return list
         */
        public List<String> mget(String... keys) {
            Jedis jedis = getJedis();
            List<String> values = jedis.mget(keys);
            jedis.close();
            return values;
        }

        /**
         * 批量存储key-value
         *
         * @param keysvalues key1,value1,key2,value2...keyn,valuen
         * @return string
         */
        public String mset(String... keysvalues) {
            Jedis jedis = getJedis();
            String status = jedis.mset(keysvalues);
            jedis.close();
            return status;
        }

        /**
         * 获取key对应值的长度
         *
         * @param key key
         * @return length of value
         */
        public long strlen(String key) {
            Jedis jedis = getJedis();
            long len = jedis.strlen(key);
            jedis.close();
            return len;
        }
    }

    class Sets {
        public Sets(JedisUtil jedisUtil) {

        }

        /**
         * 向Set中添加记录，如果记录已经存在，那么返回0，成功则返回插入成功数量
         *
         * @param key
         * @param members
         * @return
         */
        public long sadd(String key, String... members) {
            Jedis jedis = getJedis();
            long status = jedis.sadd(key, members);
            jedis.close();
            return status;
        }

        /**
         * 向Set中添加二进制数组的记录
         *
         * @param keys
         * @param members
         * @return
         */
        public long sadd(byte[] keys, byte[]... members) {
            Jedis jedis = getJedis();
            long status = jedis.sadd(keys, members);
            jedis.close();
            return status;
        }

        /**
         * 查询给定key中的member的数量
         *
         * @param key
         * @return size
         */
        public long scard(String key) {
            Jedis jedis = getJedis();
            long len = jedis.scard(key);
            jedis.close();
            return len;
        }

        /**
         * 查询给定key的member的数量
         *
         * @param key
         * @return length
         */
        public long scard(byte[] key) {
            Jedis jedis = getJedis();
            long len = jedis.scard(key);
            jedis.close();
            return len;
        }

        /**
         * 差集计算， 第一组和给定多个组的差集计算
         *
         * @param keys
         * @return set
         */
        public Set<String> sdiff(String... keys) {
            Jedis jedis = getJedis();
            Set<String> diff = jedis.sdiff(keys);
            jedis.close();
            return diff;
        }

        /**
         * 差集计算，同时将差集的结果存储起来
         *
         * @param dstKey 目的存储的key
         * @return newKey's size
         * @parem keys
         */
        public long sdiffstore(String dstKey, String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.sdiffstore(dstKey, keys);
            jedis.close();
            return count;
        }

        /**
         * 交集
         *
         * @param keys
         * @return set
         */
        public Set<String> sinter(String... keys) {
            Jedis jedis = getJedis();
            Set<String> inter = jedis.sinter(keys);
            jedis.close();
            return inter;
        }

        /**
         * 交集存储
         *
         * @param dstKey
         * @param keys
         * @return len
         */
        public long sinterstore(String dstKey, String... keys) {
            Jedis jedis = getJedis();
            long len = jedis.sinterstore(dstKey, keys);
            jedis.close();
            return len;
        }

        /**
         * 并集
         *
         * @param keys
         * @return set
         */
        public Set<String> sunion(String... keys) {
            Jedis jedis = getJedis();
            Set<String> union = jedis.sunion(keys);
            jedis.close();
            return union;
        }

        /**
         * 并集结果存储
         *
         * @param dstKey
         * @param keys
         * @return len
         */
        public long sunionstore(String dstKey, String... keys) {
            Jedis jedis = getJedis();
            long len = jedis.sunionstore(dstKey, keys);
            jedis.close();
            return len;
        }

        /**
         * 是否存在member
         *
         * @param key    set的key
         * @param member 成员member
         * @return true | fase
         */
        public boolean sismember(String key, String member) {
            Jedis jedis = getJedis();
            boolean success = jedis.sismember(key, member);
            jedis.close();
            return success;
        }

        /**
         * set下的所有members
         *
         * @param key set key
         * @return all members
         */
        public Set<String> smembers(String key) {
            Jedis jedis = getJedis();
            Set<String> members = jedis.smembers(key);
            jedis.close();
            return members;
        }

        public Set<byte[]> smembers(byte[] keys) {
            Jedis jedis = getJedis();
            Set<byte[]> bytes = jedis.smembers(keys);
            jedis.close();
            return bytes;
        }

        /**
         * 将成员从源集合移到目标集合
         *
         * @param srcKey 源key
         * @param dstKey 目标key
         * @param member 成员
         * @return status
         */
        public long smove(String srcKey, String dstKey, String member) {
            Jedis jedis = getJedis();
            long status = jedis.smove(srcKey, dstKey, member);
            jedis.close();
            return status;
        }

        /**
         * 从集合中删除成员
         */
        public String spop(String key) {
            Jedis jedis = getJedis();
            String member = jedis.spop(key);
            jedis.close();
            return member;
        }

        /**
         * 删除指定成员
         *
         * @param key
         * @param members
         * @return 返回删除的个数（不存在的member会跳过）
         */
        public long srem(String key, String... members) {
            Jedis jedis = getJedis();
            long len = jedis.srem(key, members);
            jedis.close();
            return len;
        }
    }

    class Lists {
        public Lists(JedisUtil jedisUtil) {
        }

        /**
         * list的长度
         *
         * @param key key
         * @return length of key
         */
        public long llen(String key) {
            Jedis jedis = getJedis();
            long len = jedis.llen(key);
            jedis.close();
            return len;
        }

        /**
         * 在指定位置增加list
         *
         * @param key   key
         * @param index index
         * @param value value
         * @return status
         */
        public String lset(String key, int index, String value) {
            Jedis jedis = getJedis();
            String status = jedis.lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
            jedis.close();
            return status;
        }

        /**
         * 在value的相对位置插入
         *
         * @param key   key
         * @param value value
         * @param where 在前面还是后面
         * @param pivot 相对位置内容
         * @return status
         */
        public long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
            Jedis jedis = getJedis();
            long status = jedis.linsert(key, where, pivot, value);
            jedis.close();
            return status;
        }

        /**
         * 获取指定index的值
         *
         * @param key   key
         * @param index index
         * @return value
         */
        public String lindex(String key, long index) {
            Jedis jedis = getJedis();
            String value = jedis.lindex(key, index);
            jedis.close();
            return value;
        }

        /**
         * 将list中的第一条值移出
         *
         * @param key key
         * @return firstValue
         */
        public String lpop(String key) {
            Jedis jedis = getJedis();
            String value = jedis.lpop(key);
            jedis.close();
            return value;
        }

        /**
         * 移出list中最后一条值
         *
         * @param key key
         * @return value
         */
        public String rpop(String key) {
            Jedis jedis = getJedis();
            String value = jedis.rpop(key);
            jedis.close();
            return value;
        }

        /**
         * 向list头部添加值
         *
         * @param key    key
         * @param values values
         * @return count
         */
        public long lpush(String key, String... values) {
            Jedis jedis = getJedis();
            long count = jedis.lpush(key, values);
            jedis.close();
            return count;
        }

        /**
         * 向list尾部添加值
         *
         * @param key    key
         * @param values values
         * @return count
         */
        public long rpush(String key, String... values) {
            Jedis jedis = getJedis();
            long count = jedis.rpush(key, values);
            jedis.close();
            return count;
        }

        /**
         * 获取指定范围内的数据
         *
         * @param key   key
         * @param start start
         * @param end   end
         * @return list
         */
        public List<String> lrange(String key, long start, long end) {
            Jedis jedis = getJedis();
            List<String> values = jedis.lrange(key, start, end);
            jedis.close();
            return values;
        }

        /**
         * 删除指定条数的value值
         *
         * @param key   key
         * @param count -1 ：从后往前删， >0: 从前往后删
         * @param value value
         * @return count 实际删除个数
         */
        public long lrem(String key, long count, String value) {
            Jedis jedis = getJedis();
            long num = jedis.lrem(key, count, value);
            jedis.close();
            return num;
        }

        /**
         * 保留[start,end]的数据，
         *
         * @param key   key
         * @param start start
         * @param end   end
         * @return status
         */
        public String ltrim(String key, long start, long end) {
            Jedis jedis = getJedis();
            String status = jedis.ltrim(key, start, end);
            jedis.close();
            return status;
        }
    }

    class Hash {

        public Hash(JedisUtil jedisUtil) {
        }

        /**
         * 从hash中删除指定的存储
         *
         * @param key
         * @param fields
         * @return
         */
        public long hdel(String key, String... fields) {
            Jedis jedis = getJedis();
            long status = jedis.hdel(key, fields);
            jedis.close();
            return status;
        }

        /**
         * 删除key下的所有存储
         *
         * @param key
         * @return
         */
        public long hdel(String key) {
            Jedis jedis = getJedis();
            long status = jedis.del(key);
            jedis.close();
            return status;
        }

        /**
         * hash是否存在指定域
         *
         * @param key
         * @param field
         * @return
         */
        public boolean hexists(String key, String field) {
            Jedis jedis = getJedis();
            boolean flag = jedis.hexists(key, field);
            jedis.close();
            return flag;
        }

        /**
         * 获取hash指定存储位置的值
         *
         * @param key
         * @param field
         * @return
         */
        public String hget(String key, String field) {
            Jedis jedis = getJedis();
            String value = jedis.hget(key, field);
            jedis.close();
            return value;
        }

        public byte[] hget(byte[] key, byte[] field) {
            Jedis jedis = getJedis();
            byte[] bytes = jedis.hget(key, field);
            jedis.close();
            return bytes;
        }

        /**
         * 以Map形式，返回key下所有的存储
         *
         * @param key
         * @return map
         */
        public Map<String, String> hgetAll(String key) {
            Jedis jedis = getJedis();
            Map<String, String> map = jedis.hgetAll(key);
            jedis.close();
            return map;
        }

        /**
         * 添加一个hash
         *
         * @param key
         * @param filed
         * @param value
         * @return long
         */
        public long hset(String key, String filed, String value) {
            Jedis jedis = getJedis();
            long status = jedis.hset(key, filed, value);
            jedis.close();
            return status;
        }

        /**
         * 在不存在的field中，添加存储，存在则返回0
         *
         * @param key
         * @param filed
         * @param value
         * @return long
         */
        public long hsetnx(String key, String filed, String value) {
            Jedis jedis = getJedis();
            long status = jedis.hsetnx(key, filed, value);
            jedis.close();
            return status;
        }

        /**
         * 获取hash中values的值
         *
         * @param key
         * @return list
         */
        public List<String> hvals(String key) {
            Jedis jedis = getJedis();
            List<String> values = jedis.hvals(key);
            jedis.close();
            return values;
        }

        /**
         * 在指定位置增加指定的数值，该value需要为数字型字符串
         *
         * @param key
         * @param filed
         * @param value
         * @return long
         */
        public long hincrby(String key, String filed, long value) {
            Jedis jedis = getJedis();
            long status = jedis.hincrBy(key, filed, value);
            jedis.close();
            return status;
        }

        /**
         * 返回key中所有的fields名
         *
         * @param key
         * @return set
         */
        public Set<String> hkeys(String key) {
            Jedis jedis = getJedis();
            Set<String> fields = jedis.hkeys(key);
            jedis.close();
            return fields;
        }

        /**
         * 获取key的size
         *
         * @param key
         * @return long
         */
        public long hlen(String key) {
            Jedis jedis = getJedis();
            long len = jedis.hlen(key);
            jedis.close();
            return len;
        }

        /**
         * 根据多个fields获取多个values
         *
         * @param key
         * @param fields
         * @return list
         */
        public List<String> hmget(String key, String... fields) {
            Jedis jedis = getJedis();
            List<String> values = jedis.hmget(key, fields);
            jedis.close();
            return values;
        }

        /**
         * 设置多个fields和values
         *
         * @param key  key
         * @param hash map
         * @return string
         */
        public String hmset(String key, Map<String, String> hash) {
            Jedis jedis = getJedis();
            String result = jedis.hmset(key, hash);
            jedis.close();
            return result;
        }
    }
}