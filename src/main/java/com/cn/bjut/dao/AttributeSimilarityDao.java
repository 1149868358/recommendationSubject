package com.cn.bjut.dao;

import com.cn.bjut.pojo.AttributeSimilarity;

public interface AttributeSimilarityDao {

	void insertAttributeSimilarity(AttributeSimilarity sim);
	
	//给两个用户的id查出他们俩人的属性相似度
	AttributeSimilarity selectAttributeSimilarityById(int userId1,int userId2);
}
