package com.cn.bjut.tool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

	public static DecimalFormat dfFive = new DecimalFormat("0.0####");
	
	
	/**
	 * ���в���Ϊ�ɵ��ڲ�����
	 */
	
	//��������������5��Ϊ�ֽ�
	public static int range = 5;
	
	//�������ƶ� = a*�������ƶ�+b*�Ա����ƶ�+c*ְҵ���ƶ�
	public static double a = 0.1d;
	public static double b = 0.5d;
	//��ô����  c = 1-a-b;
	
	//ְҵ����ϵ��б�Խǣ�����Զ����
	public static double maxLength = Math.sqrt(Math.pow(2d, 2)+Math.pow(8d, 2));
		
		
	//������û���
	public static int NUser =  30;
	
	//���������ƶ�ʱ�������ƶȵı�����TOTALSIMILARITY1�����ζȵı�����TOTALSIMILARITY2
	public static double TOTALSIMILARITY1 = 0.2d;
	public static double TOTALSIMILARITY2 = 0.8d;
	
	
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
