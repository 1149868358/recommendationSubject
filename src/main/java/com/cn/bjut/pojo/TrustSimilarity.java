package com.cn.bjut.pojo;

public class TrustSimilarity {

	public int userId1;
	public int userId2;
	public String mediumUserId; //������ζȵĴ����û�
	public double jacCard; //jacCard���ƶ�
	public double personSimilarity; //person������ƶ�
	public double trustSimilarity; //ֱ�ӻ��߼�����ζ�
	public boolean ifDtrust; //ֱ�����ζ� true ������ζ� false
	
	
	
	public double getJacCard() {
		return jacCard;
	}
	public void setJacCard(double jacCard) {
		this.jacCard = jacCard;
	}
	public double getPersonSimilarity() {
		return personSimilarity;
	}
	public void setPersonSimilarity(double personSimilarity) {
		this.personSimilarity = personSimilarity;
	}
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
	public String getMediumUserId() {
		return mediumUserId;
	}
	public void setMediumUserId(String mediumUserId) {
		this.mediumUserId = mediumUserId;
	}
	public double getTrustSimilarity() {
		return trustSimilarity;
	}
	public void setTrustSimilarity(double trustSimilarity) {
		this.trustSimilarity = trustSimilarity;
	}
	public boolean isIfDtrust() {
		return ifDtrust;
	}
	public void setIfDtrust(boolean ifDtrust) {
		this.ifDtrust = ifDtrust;
	}
	
	
	
}
