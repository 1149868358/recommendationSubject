package com.cn.bjut.service;

import com.cn.bjut.pojo.TrustSimilarity;

public interface TrustSimilarityService {

	public void trustSimilaritycal();
	
	public void insertOrUpdateTrustSimilarity(TrustSimilarity trustSimilarity);

	public void pTrustSimilarityCal();

	public TrustSimilarity getTrustSimilarityByUser1AndUser2(int userId1, int userId2);

}
