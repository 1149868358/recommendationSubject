package com.cn.bjut.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.service.TotalSimilarityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TotalSimilarityCal {

	@Autowired
	private TotalSimilarityService totalSimilarityService;
	
	@Test
	public void totalSimilarityCal(){
		totalSimilarityService.TotalSimilarityCalucate();
	}
}
