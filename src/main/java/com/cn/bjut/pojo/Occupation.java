package com.cn.bjut.pojo;

/**
 * 职位信息，以及职位在职位象限中的坐标位置
 * @author wkx
 *
 */
public class Occupation {

	private int occupationId;
	private String occupationName;
	private int x; //横坐标
	private int y; //纵坐标
	
	
	public int getOccupationId() {
		return occupationId;
	}
	public void setOccupationId(int occupationId) {
		this.occupationId = occupationId;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
