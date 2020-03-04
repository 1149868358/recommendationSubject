package com.cn.bjut.pojo;
/**
 * 用户电影评分表
 * @author wkx
 *
 */
public class UserMovieScore {

	private int userId;
	private int movieId;
	private int score;
	private String date;
	private boolean ifTestData; //是否是测试数据 0 - 不是；1 - 是
	
	
	public boolean isIfTestData() {
		return ifTestData;
	}
	public void setIfTestData(boolean ifTestData) {
		this.ifTestData = ifTestData;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
