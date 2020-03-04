package com.cn.bjut.util;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cn.bjut.pojo.AttributeSimilarity;
import com.cn.bjut.pojo.User;
import com.cn.bjut.service.AttributeSimilarityService;
import com.cn.bjut.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class AttributeSimilarityCal {

	@Resource
	private IUserService userService;
	@Resource
	private AttributeSimilarityService attriService;
	
	/**
	 * 本方法用来计算用户之间的属性相似度：年龄相似度，职业相似度，性别相似度
	 */
	@Test
	public void calculateAttributeSimilarity(){
		List<User> userList = userService.getAllUser();
		int i=0;
		for( ;i < userList.size(); i++){
			for(int j=i+1; j < userList.size(); j++){
				//user[i] 与 user[j] 计算属性相似度
				//第一步：年龄相似度
				double ageSimilarity = attriService.calculateAgeSimilarity(userList.get(i),userList.get(j));
				double occupationSimilarity = attriService.calculateOccupationSimilarity(userList.get(i),userList.get(j));
				double genderSimilarity = attriService.calculateGenderSimilarity(userList.get(i),userList.get(j));
				//属性相似度 = a*年龄相似度+b*性别相似度+c*职业相似度
				double attributeSimilarity = attriService.calculateAttributeSimilarity(ageSimilarity,occupationSimilarity,genderSimilarity);
			    
				AttributeSimilarity attriSim = new AttributeSimilarity();
			    attriSim.setAgeSimilarity(ageSimilarity);
			    attriSim.setAttributeSim(attributeSimilarity);
			    attriSim.setGenderSimilarity(genderSimilarity);
			    attriSim.setOccupationSimilarity(occupationSimilarity);
			    attriSim.setUser1(userList.get(i).getUserId());
			    attriSim.setUser2(userList.get(j).getUserId());
			    attriService.save(attriSim);
			}
		}
	}
	
	
	
	
}
