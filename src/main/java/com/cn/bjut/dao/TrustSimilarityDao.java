package com.cn.bjut.dao;

import java.util.List;
import java.util.Map;

import com.cn.bjut.pojo.TrustSimilarity;

public interface TrustSimilarityDao {

	void insertTrustSimilarity(TrustSimilarity trustSimilarity);

	void updateTrustSimilarity(TrustSimilarity trustSimilarity);

	TrustSimilarity selectTrustSimilarityByUserId(Map<String, Object> paramsMap);

	List<TrustSimilarity> selectDtrustByUserId(int userId);

	List<TrustSimilarity> selectPtrustByUserId(int userId);

	List<TrustSimilarity> selectTrustListByUserId(int userId);

}
