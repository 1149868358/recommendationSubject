package com.cn.bjut.dao;

import java.util.List;

import com.cn.bjut.pojo.TotalSimilrity;

public interface TotalSimilarityDao {

	void insertTotalSimilarity(TotalSimilrity total);

	List<TotalSimilrity> selectTotalSimilarityByUserId(int userId);

}
