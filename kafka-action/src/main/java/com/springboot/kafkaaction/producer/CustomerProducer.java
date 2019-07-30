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
package com.springboot.kafkaaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/31 17:12
 */
public class CustomerProducer {
    public static void main(String[] args) {

        /**
         * Kafka配置信息
         * */
        Properties props = new Properties();
        // kafka集群
        props.put("bootstrap.servers", "localhost:9092");
        // 数据同步机制
        props.put("acks", "all");
        // 重试次数，0则是直接抛弃
        props.put("retries", 0);
        // 批量大小
        props.put("batch.size", 16384);
        // 提交延迟
        props.put("linger.ms", 1);
        // producer缓存数据大小
        props.put("buffer.memory", 33554432);
        // key的序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value的序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }
}