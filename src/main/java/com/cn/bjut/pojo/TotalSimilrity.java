package com.cn.bjut.pojo;

import java.util.Date;
/**
 * 用户综合相似度
 * @author Administrator
 *
 */
public class TotalSimilrity {

	public int userId1;
	public int userId2;
	public double totalSimilarity;
	public Date date;  //计算时间
	
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
