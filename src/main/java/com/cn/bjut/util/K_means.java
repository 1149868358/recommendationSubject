package com.cn.bjut.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class K_means {
	
	//������Ϊ����
	private List<float[]> K1 = new ArrayList<float[]>();
	private List<float[]> K2 = new ArrayList<float[]>();
	private List<float[]> K3 = new ArrayList<float[]>();

	public static int num;
	
	private static List<float[]> flowerList = new ArrayList<float[]>();
	
/**
 * ����������ʵ������
 */
	private void initData(){
		FileReader reader = null;
		try {
			reader = new FileReader("C:/Users/Administrator/Desktop/����/movieLens���ݼ�/kmeansText.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String str;
		
		try {
			while((str = br.readLine()) != null){
				
				float[] flower = new float[6];
				String[] strArray =str.split("\\s+");
				flower[0] = Float.parseFloat(strArray[0]);
				flower[1] = Float.parseFloat(strArray[1]);
				flower[2] = Float.parseFloat(strArray[2]);
				flower[3] = Float.parseFloat(strArray[3]);
				flower[4] = Float.parseFloat(strArray[4]);
				flower[5] = Float.parseFloat(strArray[5]);
				flowerList.add(flower);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * �ֱ����ÿһ��ľ�ֵ
	 * @return
	 */
	public float[] kMeans(List<float[]> K){
		float[] mean1 = new float[4];
		DecimalFormat df = new DecimalFormat(".000");
		for(int i = 0; i < K.size();i++){
			mean1[0] += K.get(i)[1];
			mean1[1] += K.get(i)[2];
			mean1[2] += K.get(i)[3];
			mean1[3] += K.get(i)[4];
		}
		
		mean1[0] = mean1[0]/K.size();
		mean1[0] = Float.parseFloat(df.format(mean1[0]));
		mean1[1] = mean1[1]/K.size();
		mean1[1] = Float.parseFloat(df.format(mean1[1]));
		mean1[2] = mean1[2]/K.size();
		mean1[2] = Float.parseFloat(df.format(mean1[2]));
		mean1[3] = mean1[3]/K.size();
		mean1[3] = Float.parseFloat(df.format(mean1[3]));
		return mean1;
	}

	/**
	 * ѡ����С����
	 * @param f1
	 * @param f2
	 * @param f3
	 * @return
	 */
	private static float min(float f1,float f2,float f3) {
		float min = 999f;
		if(f1 < min)
			min = f1;
		if(f2 < min)
			min = f2;
		if(f3 < min)
			min = f3;
		return min;
		
	}
	
	/**
	 * ������벢��������
	 */
	public void kDistance(){
		float[] flowerK1 = new float[]{4.9f,3,1.4f,0.2f}; //��ʼ����
		float[] flowerK2 = new float[]{6.4f,3.2f,4.5f,1.5f};
		float[] flowerK3 = new float[]{5.8f,2.7f,5.1f,1.9f};
		float D1 = 0f;float D2 = 0f;float D3 = 0f;
		
		while(true){
			num = 0;
			for(int i = 0;i < flowerList.size(); i++){
				
				D1 = (float) (Math.pow(flowerList.get(i)[1] - flowerK1[0], 2) + Math.pow(flowerList.get(i)[2] - flowerK1[1], 2) + Math.pow(flowerList.get(i)[3] - flowerK1[2], 2) + Math.pow(flowerList.get(i)[4] - flowerK1[3], 2));
				D2 = (float) (Math.pow(flowerList.get(i)[1] - flowerK2[0], 2) + Math.pow(flowerList.get(i)[2] - flowerK2[1], 2) + Math.pow(flowerList.get(i)[3] - flowerK2[2], 2) + Math.pow(flowerList.get(i)[4] - flowerK2[3], 2));
				D3 = (float) (Math.pow(flowerList.get(i)[1] - flowerK3[0], 2) + Math.pow(flowerList.get(i)[2] - flowerK3[1], 2) + Math.pow(flowerList.get(i)[3] - flowerK3[2], 2) + Math.pow(flowerList.get(i)[4] - flowerK3[3], 2));

				if(D1 == min(D1,D2,D3)){
					
	            	K1.add(flowerList.get(i));
	            	if(flowerList.get(i)[5] == 1.0)
	            		num +=1;
	            }
	            	
	            if(D2 == min(D1,D2,D3)){
	            	K2.add(flowerList.get(i));
	            	if(flowerList.get(i)[5] == 2.0)
	            		num +=1;
	            }
	            	
	            if(D3 == min(D1,D2,D3)){
	            	K3.add(flowerList.get(i));
	            	if(flowerList.get(i)[5] == 3.0)
	            		num +=1;
	            }
	            	
			}
			System.out.println(num);  //ʶ����ȷ�Ļ�������
			double rate = (double)num/(double)flowerList.size();
			System.out.println("��ȷ��Ϊ��"+rate);
			
			if(flowerK1.equals(kMeans(K1)) &&flowerK2.equals(kMeans(K2))&&flowerK3.equals(kMeans(K3))){
				break;
			}
			else  //����µ����ĺ�ԭ������� �㷨ֹͣ
				flowerK1 = kMeans(K1); flowerK2 = kMeans(K2); flowerK3 = kMeans(K3);
				
			System.out.println(kMeans(K1)[0]+" "+kMeans(K1)[1]+" "+kMeans(K1)[2]+" "+kMeans(K1)[3]);
			System.out.println(kMeans(K2)[0]+" "+kMeans(K2)[1]+" "+kMeans(K2)[2]+" "+kMeans(K2)[3]);
			System.out.println(kMeans(K3)[0]+" "+kMeans(K3)[1]+" "+kMeans(K3)[2]+" "+kMeans(K3)[3]);
		}
		
	}
	


	public static void main(String[] args) {
		K_means kmeans = new K_means();
		kmeans.initData();
		kmeans.kDistance();
	}

}

