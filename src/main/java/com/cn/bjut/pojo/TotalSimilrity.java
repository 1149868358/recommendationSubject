package com.cn.bjut.pojo;

import java.util.Date;
/**
 * 总相似度实体类
 * @author Administrator
 *
 */
public class TotalSimilrity implements Comparable<TotalSimilrity>{

	public int userId1;
	public int userId2;
	public double totalSimilarity;
	public String date;  //插入时间
	
	
	public int getUserId1() {
		return userId1;
	}
	public void setUserId1(int userId1) {
		this.userId1 = userId1;
	}
	public int getUserId2() {
		return userId2;
	}
	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}
	public double getTotalSimilarity() {
		return totalSimilarity;
	}
	public void setTotalSimilarity(double totalSimilarity) {
		this.totalSimilarity = totalSimilarity;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	public int compareTo(TotalSimilrity o) {

		if(this.totalSimilarity > o.getTotalSimilarity()){
			return 1;
		}else if(this.totalSimilarity < o.getTotalSimilarity()){
			return -1;
		}
		return 0;
	}
	
	
}
