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
package com.springboot.learn.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/30 15:45
 */
public class UseFileChannel {
    /**
     * 将文件数据读取到Buffer中
     */
    private static void useFileChannel() {
        RandomAccessFile accessFile = null;

        try {
            accessFile = new RandomAccessFile("data\\channel\\file\\SerialVersion.txt", "rw");

            // 利用FileChannel来读取数据
            FileChannel fileChannel = accessFile.getChannel();

            // 设置Buffer，将数据读入
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);

            int byteReader = fileChannel.read(byteBuffer);
            while (byteReader != -1) {
                System.out.println("Read:" + byteReader);

                // filp()--> 首先读取数据到Buffer，然后反转Buffer
                byteBuffer.flip();

                // 还要判断buffer里面有没有残留
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                System.out.println();
                // 清空
                byteBuffer.clear();

                // 继续读
                byteReader = fileChannel.read(byteBuffer);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭文件
                accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        UseFileChannel.useFileChannel();
    }
}