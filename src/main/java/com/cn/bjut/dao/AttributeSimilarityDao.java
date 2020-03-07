package com.cn.bjut.dao;

import java.util.Map;

import com.cn.bjut.pojo.AttributeSimilarity;

public interface AttributeSimilarityDao {

	void insertAttributeSimilarity(AttributeSimilarity sim);
	
	//给两个用户的id查出他们俩人的属性相似度
	AttributeSimilarity selectAttributeSimilarityById(Map<String, Object>paramsMap);
}
