package com.cn.bjut.pojo;

/**
 * �û���Ϣʵ����
 * @author wkx
 *
 */
public class User {

	private int userId;
	private int age;
	private String gender; //�Ա�
	private String occupation; //ְҵ
	
	
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
