package com.cn.bjut.service;

import java.util.List;

import com.cn.bjut.pojo.TotalSimilrity;

public interface TotalSimilarityService {

	void TotalSimilarityCalucate();

	List<TotalSimilrity> getTotalSimilarityByUserId(int userId);

}
