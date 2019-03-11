package com.leo.java8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.leo.java8.other.Data;
import com.leo.java8.other.domain.Employee;
import org.junit.Test;


public class StreamTest2 {
	
	// 2. 中间操作
	/*
	 * 映射
	 *   map —— 接收 Lambda ， 将元素转换成其他形式或提取信息。
	 *        接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
	 *   flatMap —— 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
	 */
	@Test
	public void test1(){
		Stream<String> nameStream = Data.EMPS.stream()
			.map(Employee::getName);
		nameStream.forEach(System.out::println);
		
		System.out.println("-------------------------------------------");
		
		List<String> strList = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");
		Stream<Stream<Character>> stream2 = strList.stream()
			   .map(StreamTest2::filterCharacter);
		stream2.forEach((sm) -> sm.forEach(System.out::println));
		
		System.out.println("---------------------------------------------");
		
		Stream<Character> stream3 = strList.stream()
			   .flatMap(StreamTest2::filterCharacter);
		stream3.forEach(System.out::println);
	}
	static Stream<Character> filterCharacter(String str){
		List<Character> list = new ArrayList<>();
		for (char ch : str.toCharArray()) {
			list.add(ch);
		}
		return list.stream();
	}
	
	/*
	 * sorted() —— 自然排序
	 * sorted(Comparator com) —— 定制排序
	 */
	@Test
	public void test2(){
		Data.EMPS.stream()
			.map(Employee::getName)
			.sorted()
			.forEach(System.out::println);
		
		System.out.println("------------------------------------");

		Data.EMPS.stream()
			.sorted((x, y) -> {
				if(x.getAge() == y.getAge()){
					return x.getName().compareTo(y.getName());
				}else{
					return Integer.compare(x.getAge(), y.getAge());
				}
			}).forEach(System.out::println);
	}
}
