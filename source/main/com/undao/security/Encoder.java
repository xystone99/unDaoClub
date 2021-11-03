package com.undao.security;

import java.util.Arrays;
import java.util.Scanner;

public class Encoder {

	public static String encry(String password, String in) {
		Scanner console = new Scanner(System.in);
		//System.out.println("输入密码：");
		//String password = console.nextLine();
		double pw = 0;
		for(int i=0; i<password.length(); i++){
			pw += (double)(password.charAt(i));
		}
		int  check = (int)pw % 5;//由输入的密码计算出一个校验位进行加密
		//System.out.println(check);
		//System.out.println("输入需加密的中文字：");
		//String in = console.nextLine();
		int[] chinaInt = new int[]{};//将中文转成int型放到数组中
		int[] china = new int[5];//将单个中文的5位编码取出
		//System.out.println("加密后：");
		for(int i=0; i<in.length(); i++){
			chinaInt=Arrays.copyOf(chinaInt,chinaInt.length+1);
			chinaInt[chinaInt.length - 1] = (int)in.charAt(i);
		}
		
		//对转成int型的中文字5位编码，每位加上check，再对10取余数
		StringBuilder buf = new StringBuilder();
		for(int i=0; i<chinaInt.length; i++){
			china[0] = (chinaInt[i] / 10000 + check) % 10;
			china[1] = ((chinaInt[i] / 1000) % 10 + check) % 10;
			china[2] = ((chinaInt[i] / 100) % 10 + check) % 10;
			china[3] = ((chinaInt[i] / 10) % 10 + check) % 10;
			china[4] = (chinaInt[i] % 10 + check) % 10;
			for(int j=0; j<5; j++) {
				buf.append( china[j] );
			}
		}
		return buf.toString();
	}

	public static String decry(String password, String in) {
		Scanner console = new Scanner(System.in);
		//System.out.println("输入密码：");
		//String password = console.nextLine();
		double pw = 0;
		for (int i = 0; i < password.length(); i++) {
			pw += (double) (password.charAt(i));
		}
		int check = (int) pw % 5;// 由输入的密码计算出一个校验位进行解密
		//System.out.println(check);
		//System.out.println("输入密文：");
		//String in = console.nextLine();
		int[] x = new int[5];
		//System.out.print("解密后：");
		
		StringBuilder buf = new StringBuilder();
		for (int j = 0; j < in.length() / 5; j++) {
			int one = 0;// 一个中文的密文
			for (int i = 0; i < 5; i++) {// 取出5个
				int judge = in.charAt(i + 5 * j) - '0';
				if (judge >= 0 && judge < check)
					one = (((in.charAt(i + 5 * j) - '0') + 10) - check) + one
							* 10;
				else
					one = ((in.charAt(i + 5 * j) - '0') - check) + one * 10;
			}
			buf.append( (char)one );
			//System.out.print((char) one);
		}
		return buf.toString( );
	}

	/**
	 * main test
	 */
	public static void main(String[] args) {
		System.out.println( encry("abc123", ","));

		// 加密的方法
		System.out.println( decry("abc123", "444014445744400"));	//解密的方法
	}
	
}
