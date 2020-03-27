package com.cn.bjut.tool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

	public static DecimalFormat dfFive = new DecimalFormat("0.0####");
	
	
	/**
	 * 下列参数为可调节参数：
	 */
	
	//年龄计算参数，以5岁为分界
	public static int range = 5;
	
	//属性相似度 = a*年龄相似度+b*性别相似度+c*职业相似度
	public static double a = 0.1d;
	public static double b = 0.5d;
	//那么参数  c = 1-a-b;
	
	//职业坐标系的斜对角，即最远距离
	public static double maxLength = Math.sqrt(Math.pow(2d, 2)+Math.pow(8d, 2));
		
		
	//最近邻用户数
	public static int NUser =  30;
	
	//计算总相似度时属性相似度的比例：TOTALSIMILARITY1，信任度的比例：TOTALSIMILARITY2
	public static double TOTALSIMILARITY1 = 0.2d;
	public static double TOTALSIMILARITY2 = 0.8d;
	
	
	/**
	 * 以上为动态调节参数
	 */
	
	/**
	 * 
	 * 将传入的Object对象转化为Double对象
	 * @param object
	 * @return
	 */
	public static double ctd(Object obj) {
		return Double.parseDouble(obj.toString());
	}
	
	public static double df5(double a){
		return Double.parseDouble(dfFive.format(a));
	}
	public static String timeFormate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
