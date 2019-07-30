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
package com.springboot.kafkaaction.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/31 17:22
 */
public class CustomerConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put( "group.id", "test");
        props.put( "enable.auto.commit", "true");
        props.put( "auto.commit.interval.ms", "1000");
        props.put( "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put( "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer <String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList( "foo", "bar"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String,String> record: records) {
                System.out.printf(" offset = %d, key = %s, value = %s % n ",
                    record.offset(), record.key(), record.value());
            }
        }
    }
}