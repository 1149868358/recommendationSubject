package com.cn.bjut.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.service.MAECalService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class MAE_cal {

	@Autowired
	private MAECalService maeCalService;
	
	@Test
	private void Mae_cal(){
		maeCalService.maeCal();
	}
}
