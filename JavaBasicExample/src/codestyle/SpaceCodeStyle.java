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
package codestyle;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/6/17 11:35
 *
 * 1. 任何二目、三目运算符的左右两边都必须加一个空格；
 * 2. 注释的双斜线与注释内容之间有且只有一个空格；
 * 3. 方法参数在定义和传入时，多个参数逗号后边必须加空格
 * 4. 没有必要增加空格使变量的赋值等号与上一行对应位置的等号对齐
 * 5. 如果是大括号内为空，则简洁写成{}，大括号中间无需换行和空格
 * 6. 左右小括号与括号内部的相邻字符之间不要出现空格
 * 7. 左大括号前需要加空格
 */
public class SpaceCodeStyle {
	// 没有必要增加若干空格使变量的赋值等号与上一行对应位置的等号对齐
	private static Integer one = 1;
	private static Long two = 2L;
	private static Float three = 3f;
	private static StringBuilder sb = new StringBuilder("code style:");

	// 缩进4个空格
	public static void main(String[] args) {
		// 继续缩进4个空格
		try {
			// 任何二目运算符的左右必须有一个空格
			int count = 0;
			// 任何三目运算符的左右必须有一个空格
			boolean condition = (count == 0) ? true : false;

			// 关键词if与左侧小括号之间必须有一个空格
			// 左括号内的字母c与左括号、字母n与右括号都不需要空格
			// 右括号与左括号前加空格且不换行，左大括号后必须换行
			if (condition){
				System.out.println("world");
				// else的前后都必须加空格
				// 右大括号前换行，右大括号后有else时，不换行
			} else {
				System.out.println("OK");
				// 右大括号后直接结束，必须换行
			}
			// 如果大括号内为空，简写
		} catch (Exception e){}

		// 在每个实参逗号之后必须有一个空格
		String result = getString(one, two, three, sb);
		System.out.println(result);
	}

	// 方法之间，通过空行进行隔断。在方法定义中，每个形参之后必须有一个空格
	private static String getString(Integer one, Long two, Float three, StringBuilder sb){
		Float temp = one + two + three;
		sb.append(temp);
		return sb.toString();
	}
}