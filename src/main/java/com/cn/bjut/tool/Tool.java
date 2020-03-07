package com.cn.bjut.tool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

	public static DecimalFormat dfFive = new DecimalFormat("0.0####");
	
	
	/**
	 * 下列参数为可调节参数：
	 */
	
	//最近邻用户数
	public static int NUser =  30;
	
	//计算总相似度时属性相似度的比例：TOTALSIMILARITY1，信任度的比例：TOTALSIMILARITY2
	public static double TOTALSIMILARITY1 = 0.1d;
	public static double TOTALSIMILARITY2 = 0.9d;
	
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
