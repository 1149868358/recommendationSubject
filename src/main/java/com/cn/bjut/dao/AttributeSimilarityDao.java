package com.cn.bjut.dao;

import java.util.Map;

import com.cn.bjut.pojo.AttributeSimilarity;

public interface AttributeSimilarityDao {

	void insertAttributeSimilarity(AttributeSimilarity sim);
	
	//�������û���id����������˵��������ƶ�
	AttributeSimilarity selectAttributeSimilarityById(Map<String, Object>paramsMap);
}
