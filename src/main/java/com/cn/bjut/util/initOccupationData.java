package com.cn.bjut.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.pojo.Occupation;
import com.cn.bjut.service.OccupationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class initOccupationData {
	
	@Autowired
	private OccupationService occupationService;
	
	/**
	 * 本测试类用来初始化职位表信息 信息存储在Resource下的occupationData.properties文件中
	 * 三行数据分别是： 职位名称，职位在坐标系中的横坐标，职位的纵坐标
	 */
	@Test
	public void initOccupationDate(){
		
		InputStream in = null;
		try {
			//Properties props = new Properties();
			in = initOccupationData.class.getResourceAsStream("/occupationData.properties");
			//props.load(in);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s;
			int i = 0;
			while((s = br.readLine()) != null){
				String[] params = s.split("\t");
				Occupation occ = new Occupation();
				occ.setOccupationId(++i);
				occ.setOccupationName(params[0]);
				occ.setX(Integer.parseInt(params[1]));
				occ.setY(Integer.parseInt(params[2]));
				occupationService.save(occ);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				if( in != null ){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	

}
