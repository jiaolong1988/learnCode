package collection.Deque;

import java.util.ArrayDeque;

public class ArrayDequeStack
{
	public static void main(String[] args)
	{
		ArrayDeque stack = new ArrayDeque();
		// 依次将三个元素push入"栈"
		stack.push("1");
		stack.push("2");
		stack.push("3");


		System.out.println(stack);
		// 访问第一个元素，但并不将其pop出"栈"，输出：3
		System.out.println(stack.peek());
		// 依然输出：
		System.out.println(stack);
		// pop出第一个元素，输出：3
		System.out.println(stack.pop());
		// 输出：
		System.out.println(stack);
	}
}