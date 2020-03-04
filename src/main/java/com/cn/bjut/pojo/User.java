package com.cn.bjut.pojo;

/**
 * 用户信息实体类
 * @author wkx
 *
 */
public class User {

	private int userId;
	private int age;
	private String gender; //性别
	private String occupation; //职业
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String isGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	
}
