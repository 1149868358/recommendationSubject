package com.cn.bjut.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.service.MAECalService;
import com.cn.bjut.service.TotalSimilarityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestAlgorithmIsEffective {

	@Autowired
	private TotalSimilarityService totalSimilarityService;
	@Autowired
	private MAECalService maeCalService;
	
	/**
	 * 本例子测试内容为综合相似度计算时，属性相似度与用户信任度的取值系数对结果MAE值的影响。
	 */
	@Test
	public void testAlgorithmIsEffective(){
		totalSimilarityService.TotalSimilarityCalucate();
		maeCalService.maeCal();
	}
	
}
