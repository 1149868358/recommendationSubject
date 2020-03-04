package com.cn.bjut.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.service.TrustSimilarityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TrustSimilarityCal {

	@Autowired
	private TrustSimilarityService trustSimilarityService;
	
	/**
	 * 本方法计算用户间的信任相似度
	 */
	@Test
	public void TrustSimilarityCalculate(){
		trustSimilarityService.trustSimilaritycal();
	}
}
