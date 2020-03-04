package com.cn.bjut.pojo;
/**
 * 用户属性相似度实体类
 * @author wkx
 *
 */
public class AttributeSimilarity {

	private int user1;
	private int user2;
	private double ageSimilarity; //年龄相似度
	private double genderSimilarity; //性别相似度
	private double occupationSimilarity; //职业相似度
	private double attributeSim;  //属性相似度 = a*年龄相似度+b*性别相似度+c*职业相似度
	
	
	public double getAgeSimilarity() {
		return ageSimilarity;
	}
	public void setAgeSimilarity(double ageSimilarity) {
		this.ageSimilarity = ageSimilarity;
	}
	public double getGenderSimilarity() {
		return genderSimilarity;
	}
	public void setGenderSimilarity(double genderSimilarity) {
		this.genderSimilarity = genderSimilarity;
	}
	public double getOccupationSimilarity() {
		return occupationSimilarity;
	}
	public void setOccupationSimilarity(double occupationSimilarity) {
		this.occupationSimilarity = occupationSimilarity;
	}
	public int getUser1() {
		return user1;
	}
	public void setUser1(int user1) {
		this.user1 = user1;
	}
	public int getUser2() {
		return user2;
	}
	public void setUser2(int user2) {
		this.user2 = user2;
	}
	public double getAttributeSim() {
		return attributeSim;
	}
	public void setAttributeSim(double attributeSim) {
		this.attributeSim = attributeSim;
	}
	
}
