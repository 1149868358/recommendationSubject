package com.cn.bjut.dao;

import java.util.List;
import java.util.Map;

import com.cn.bjut.pojo.AttributeSimilarity;

public interface AttributeSimilarityDao {

	void insertAttributeSimilarity(AttributeSimilarity sim);
	
	//给两个用户的id查出他们俩人的属性相似度
	AttributeSimilarity selectAttributeSimilarityById(Map<String, Object>paramsMap);

	//给一个userId查出这个人的所有属性相似度的值
	List<AttributeSimilarity> selectAttributeSimByUserId(int userId);
}
