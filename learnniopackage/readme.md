#### 学习Java的NIO包中对文件和文件路径结合起来的使用

Java NIO(New IO)是一个可以替代标准Java IO APId的IO API，Java NIO提供了与标准IO不同的IO工作方式

----
##### 新版说明，不同之处
###### Java NIO：Channels and Buffers（通道和缓冲区）
标准的IO基于字节流和字符流进行操作的，而NIO是基于通道（Channel）和缓冲区（Buffer）进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。

###### Java NIO：Non-blocking IO（非阻塞IO）
例如：当线程从通道读取数据到缓冲区时，线程还是可以进行其他事情。当数据被写入到缓冲区时，线程可以继续处理他。

###### Java NIO：Selectors（选择器）
选择器用于监听多个通道的事件。所以单个的线程可以监听多个数据通道。

----
#### 概述：
###### Channel and Buffer
1. 主要Channel的实现：
    - FileChannel
    - DatagramChannel
    - SocketChannel
    - ServerSocketChannel

2. 主要的Buffer的实现
    - ByteBuffer
    - CharBuffer
    - DoubleBuffer
    - FloatBuffer
    - IntBuffer
    - LongBuffer
    - ShortBuffer

    Buffer这些覆盖了基本数据类型

###### Selector
Selector允许单线程处理多个Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。

要使用Selector，需要向Selector注册Channel，然后调用他的select()方法。这个方法会一直阻塞到某个注册的通道就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有：新连接、数据接收等。

----
#### Channel
Java NIO的通道类似流，但又有些不同：

      - 既可以从通道中读取数据，又可以写数据到通道。但是流的读写通常是单向的。
      - 通道可以异步地读写
      - 通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。

###### Channel实现
1. FileChannel： 从文件中读写数据
2. DatagramChannel：能通过UDP读写网络中的数据
3. SocketChannel：能通过TCP读写网络中的数据
4. ServerSocketChannel：可以监听新进来的TCP连接，像Web服务器哪像。对每一个新进来的连接都会创建一个SocketChannel

基本示例：com.springboot.learn.channel中

```java
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
     * */
    private static void useFileChannel(){
        RandomAccessFile accessFile = null;

        try {
            accessFile = new RandomAccessFile("data\\channel\\file\\SerialVersion.txt", "rw");

            // 利用FileChannel来读取数据
            FileChannel fileChannel = accessFile.getChannel();

            // 设置Buffer，将数据读入
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);

            int byteReader = fileChannel.read(byteBuffer);
            while (byteReader != -1){
                System.out.println("Read:" + byteReader);

                // filp()--> 首先读取数据到Buffer，然后反转Buffer
                byteBuffer.flip();

                // 还要判断buffer里面有没有残留
                while (byteBuffer.hasRemaining()){
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
        }finally {
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
```

----
#### Buffer
###### Buffer的基本用法
1. 写入数据到Buffer
2. 调用flip()方法
3. 从Buffer中读取数据
4. 调用clear()方法或者compact()方法清空

当向Buffer写入数据时，Buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入的所有数据。

一旦读完所有的数据，就需要清空缓冲区，让它可以再次被写入。

有两种模式清空缓冲区：clear或compact。clear会清空所有的内容，compact只会清空已读的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据放于未读数据的后面。

```java
 try {
            accessFile = new RandomAccessFile("data\\channel\\file\\SerialVersion.txt", "rw");

            // 利用FileChannel来读取数据
            FileChannel fileChannel = accessFile.getChannel();

            // 设置Buffer，将数据读入
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);

            int byteReader = fileChannel.read(byteBuffer);
            while (byteReader != -1){
                System.out.println("Read:" + byteReader);

                // filp()--> 首先读取数据到Buffer，然后反转Buffer
                byteBuffer.flip();

                // 还要判断buffer里面有没有残留
                while (byteBuffer.hasRemaining()){
                    System.out.print((char) byteBuffer.get());
                }
                System.out.println();
                // 清空
                byteBuffer.clear();

                // 继续读
                byteReader = fileChannel.read(byteBuffer);

            }
```
###### Buffer的capacity，position和limit三个属性
1. capacity容量 -- Buffer大小（Buffer单元，不是固定大小）
2. position位置 -- 读模式的位置
3. limit限制 -- 写模式的限制

###### Buffer的类型
1. ByteBuffer
2. IntBuffer
3. FloatBuffer
4. DoubleBuffer
5. ShortBuffer
6. LongBuffer
7. CharBuffer
8. MappedByteBuffer -- 内存映射缓存

###### Buffer的分配
可以通过类型（xxx）的allocate()方法进行分配，xxx.allocate(capacity)

###### 向Buffer中写入数据
通过put()方法写入，根据不同的要求使用不同的put()方法

###### flip()方法
flip()将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。

###### 从Buffer中读取数据
1. 从Buffer读取数据到Channel
```java
// 从buffer中读取数据到channel
int bytesWritten = channel.write(buffer);
```
2. 使用get()方法读取数据
```java
byte data = buffer.get();
```

###### rewind()
将position设回0，但是limit保持不变

###### make() and reset()
Buffer.make()可以标记一个特定position，之后调用Buffer.reset()可以恢复到这个position

###### equals() and compareTo()，两个Buffer的比较
equals()相等的条件:（剩余元素是指：position到limit之间的元素）

    1. 有相同的类型
    2. Buffer剩余的个数相等
    3. 所有剩余的数据都相同

compareTo()前者小于后者的条件：

    1. 第一个不相等的元素小于另一个Buffer中对应的元素
    2. 所有元素都相等，但是前者数量比后者小

----
#### Scatter() and Gather()

###### 从Channel中分散（scatter）读取
将数据从Channel的读模式中，写入到多个Buffer中

```java
// 利用Buffer数组，将Channel数据写入其中
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = {header, body};
// 写入方式：根据Buffer在数组中的顺序，依次写入，写入数据大小跟容量一致
channel.read(bufferArray);
```

###### 聚集（gather）写入一个Channel

将多个Buffer中的数据写入同一个Channel中

```java
// 利用Buffer数组，将Buffer数据写入Channel中
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = {header, body};
// 写入方式：根据Buffer在数组中的顺序，依次写入
channel.write(bufferArray);
```

----
#### 两个通道之间的数据传输
注意前提：其中一个通道是FileChannel

###### transferFrom()
传输到FileChannel中
```java
String fileFromPath = "xxx";
String fileToPath = "xxx";
String mode = "rw";
RadomAccessFile fromFile = new RadomAccessFile(fileFromPath, mode);
FileChannel fromChannel = fromFile.getChannel();

RadomAccessFile toFile = new RadomAccessFile(fileToPath, mode);
FileChannel toChannel = toFile.getChannel();

long position = 0;
long count = toChannel.size();

// start--end---src
fromChannel.transferTo(position, count, toChannel);
```

###### transferTo()
传输到另一个Channel中

```java
String fileFromPath = "xxx";
String fileToPath = "xxx";
String mode = "rw";
RadomAccessFile fromFile = new RadomAccessFile(fileFromPath, mode);
FileChannel fromChannel = fromFile.getChannel();

RadomAccessFile toFile = new RadomAccessFile(fileToPath, mode);
FileChannel toChannel = toFile.getChannel();

long position = 0;
long count = fromChannel.size();

// start--end---src
toChannel.transferFrom(position, count, fromChannel);
```

#### Selector

###### selector的创建：Selector.open()

###### 向Selector注册通道

```java
// 必须注册为非阻塞模式
channel.configureBlocking(false);
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```

registry()方法的第二个参数，这是一个“兴趣”集合类型，对什么事件感兴趣，有：
1. Connect ---- SelectionKey.OP_CONNECT
2. Accept --- SelectionKey.OP_ACCEPT
3. Read --- SelectionKey.OP_READ
4. Write --- SelectionKey.OP_WRITE
可以用& 或者 | 来多个使用
