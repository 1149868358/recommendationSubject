package com.cn.bjut.tool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

	public static DecimalFormat dfFive = new DecimalFormat("0.0####");
	
	
	/**
	 * ���в���Ϊ�ɵ��ڲ�����
	 */
	
	//������û���
	public static int NUser =  30;
	
	//���������ƶ�ʱ�������ƶȵı�����TOTALSIMILARITY1�����ζȵı�����TOTALSIMILARITY2
	public static double TOTALSIMILARITY1 = 0.1d;
	public static double TOTALSIMILARITY2 = 0.9d;
	
	/**
	 * ����Ϊ��̬���ڲ���
	 */
	
	/**
	 * 
	 * �������Object����ת��ΪDouble����
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
