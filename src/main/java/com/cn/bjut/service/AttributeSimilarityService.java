package com.cn.bjut.service;

import java.util.List;

import com.cn.bjut.pojo.AttributeSimilarity;
import com.cn.bjut.pojo.User;

public interface AttributeSimilarityService {
 
	public void save(AttributeSimilarity sim);

	public double calculateAgeSimilarity(User user1, User user2);

	public double calculateOccupationSimilarity(User user1, User user2);

	public double calculateGenderSimilarity(User user1, User user2);

	public double calculateAttributeSimilarity(double ageSimilarity, double occupationSimilarity,
			double genderSimilarity);
	
	public AttributeSimilarity getAttributeSimilarityTwoUser(int userId1, int userId2);

	public List<AttributeSimilarity> getAttributeSimilarityByUserId(int userId1);
}
