package com.cn.bjut.dao;

import com.cn.bjut.pojo.AttributeSimilarity;

public interface AttributeSimilarityDao {

	void insertAttributeSimilarity(AttributeSimilarity sim);
	
	//�������û���id����������˵��������ƶ�
	AttributeSimilarity selectAttributeSimilarityById(int userId1,int userId2);
}
