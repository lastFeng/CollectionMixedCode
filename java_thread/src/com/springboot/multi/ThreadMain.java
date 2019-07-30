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
package com.springboot.multi;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/10 17:14
 */
public class ThreadMain {
    public void m(){
        System.out.println("test");
    }
    public static void main(String[] args) {
        // extends Thread
        //ExtendsThread threadTest = new ExtendsThread();
        //threadTest.start();
        //
        //// implements Runnable
        //Thread thread = new Thread(new ImplementsRunable());
        //thread.start();
        //
        //Thread thread1 = new Thread(()->new ThreadMain().m());
        //thread1.start();
        String a = "abc";
        System.out.println(a + a.codePointAt(0));
        System.out.println(a + a.codePointBefore(2));

        scan: {
            for (int i = 0; i < 5; i++){
                for (int j = 0; j < 5; j++){
                    if (i % 2 ==0){
                        break scan;
                    }
                }
                return;
            }
        }
        System.out.println(1);

        StringBuilder stringBuilder = new StringBuilder("Abcd");

        String string = null;
        System.out.println(stringBuilder.append(string));

        int n = (4 - 2) >> 1;
        System.out.println(n);

    }
}